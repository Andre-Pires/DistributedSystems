package registofatura.ws.impl.handler;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import registofatura.ws.impl.ImAliveReceiver;
import registofatura.ws.impl.ImAliveSender;

//@formatter:off
/**
* 
* Implementa o protocolo que controla a replicação das mensagens do cliente para os servidores
* secundários (réplicas) e o mecanismo de tolerância a faltas.
* O sistema de replicação pode ser visto como uma lista simplesmente ligada desde o servidor primário
* para cada um dos secundários:
* (Ex:) Primário ---> 1º Sec ---> 2º Sec ---> 3º Sec
* A ordem de ligação é dada pela ordem em que as réplicas são lançadas.
* 
******  REGISTO DE NOVAS RÉPLICAS
* Cada secundário, ao ser iniciado, vai buscar o endpoint do servidor primário e pede-lhe o endpoint
* para o qual este aponta. Caso o primário aponte para alguém (um secundário já lançado), pergunta a 
* esse secundário para quem é que este aponta e assim sucessivamente. Percorre a lista e 
* regista-se no final como réplica.
* 
******  TOLERANCIA A FALTAS
* Quando um secundário se regista no fim da fila, o penúltimo elemento começa a enviar-lhe notificações
* "IM ALIVE". Assim, cada secundário recebe notificações "IM ALIVE" do elemento anterior, garantindo que
* cada secundário se apercebe quando um servidor falha (Assume que este falha após uma janela de tempo
* sem receber IM ALIVES).
* Quando a janela expira, a réplica volta a iterar sobre a fila para se encaixar no lugar do elemento
* que falhou. 
* Ex:
* P ---> S1 ---> S2 ---> S3
* (após um tempo, S2 falha e a janela expira)
* P ---> S1 ---> S3
*  
*  1. S3 pergunta ao primário para quem aponta; 
*  2. P devolve S1; 
*  3. S3 pergunta a S1 para quem aponta; 
*  4. S1 percebe que já não aponta para ninguém;
*  5. S1 aponta para S3 e inicia stream de IM ALIVES;
* 
* 
* As constantes IM_ALIVE, GET_NEXT_ADRESS, GIVE_NEXT_ADRESS são atributos escritos no header da mensagem SOAP 
* que identificam o tipo de mensagem SOAP que está a ser tratada.
*/
//@formatter:on
public class MessageHandler
        implements SOAPHandler<SOAPMessageContext> {


    public static final String IM_ALIVE = "imalive";

    /** Valor pre-definido para identificar o atributo */
    public static final String IM_ALIVE_VALUE = "1";

    public static final String GET_NEXT_ADDRESS = "getaddress";
    public static String MY_ENDPOINT = null;

    public static final String GIVE_NEXT_ADDRESS = "giveaddress";
    private static String nextEndpoint = null;
    public static boolean DEBUG = false;

    public static ImAliveReceiver receiver = new ImAliveReceiver();
    public static ImAliveSender sender = new ImAliveSender();

    private static int seqNum = 1;
    private static SOAPMessage lastMessage;

    public MessageHandler() {
    }

    @Override
    public void close(MessageContext context) {
        //Do nothing
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext smc) {
        Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (!outbound) {

            try {
                if (DEBUG) {
                    System.out.println("Handling Message:");
                    smc.getMessage().writeTo(System.out);
                    System.out.println();
                }

                SOAPMessage inboundMessage = smc.getMessage();
                if (isGetNextAdress(inboundMessage)) {

                    System.out.println("isGetNextAdress...");

                    String incomingEndpoint = inboundMessage.getSOAPHeader().getAttribute(
                            GET_NEXT_ADDRESS);

                    if (DEBUG) {
                        System.out.println("INCOMING ENDPOINT IS " + incomingEndpoint);
                    }

                    /*
                     * Não aponta para nenhuma réplica (última da fila); Começa a enviar
                     * Im Alives para a réplica que fez o pedido.
                     */
                    if (nextEndpoint.equals(MY_ENDPOINT)) {
                        nextEndpoint = incomingEndpoint;
                        sender = new ImAliveSender();
                        sender.setNextEndpoint(nextEndpoint);
                        sender.start();
                    }

                    sendMessage(GIVE_NEXT_ADDRESS, nextEndpoint, incomingEndpoint);
                    return false;
                }

                if (isGiveNextAdress(inboundMessage)) {
                    System.out.println("isGiveNextAdress...");
                    String incomingEndpoint = inboundMessage.getSOAPHeader().getAttribute(
                            GIVE_NEXT_ADDRESS);
                    if (incomingEndpoint.equals(MY_ENDPOINT)) {
                        setNextEndpoint(MY_ENDPOINT);
                        receiver.setAlive(false);
                        receiver.interrupt();
                        receiver = new ImAliveReceiver();
                        receiver.start();
                    } else {
                        sendMessage(GET_NEXT_ADDRESS, MY_ENDPOINT, incomingEndpoint);
                    }

                    return false;
                }

                if (isImAlive(inboundMessage)) {
                    receiver.interrupt();
                    return false;
                }

                if (isCheckAvailable(inboundMessage)) {
                    //O cliente verificou que realmente o servidor está disponível e a sua mensagem é descartada
                    return false;
                }

                int sentSeqNum = -1;

                Iterator elements = inboundMessage.getSOAPHeader().getChildElements();
                while (elements.hasNext()) {
                    SOAPElement element = (SOAPElement) elements.next();
                    if (element.getLocalName().equals("seqNum")) {
                        sentSeqNum = Integer.parseInt(element.getValue());
                        break;
                    }
                }
                System.out.println("Recebido pedido " + sentSeqNum);
                System.out.println("Valor de seq actual " + seqNum);

                if (seqNum == sentSeqNum) {
                    seqNum++;
                } else {
                    System.out.println("Pedido repetido, a repetir resposta");
                    lastMessage.writeTo(System.out);
                    System.out.println();
                    smc.setMessage(lastMessage);
                    return false;
                }
                // Envia a mensagem de replicação ao próximo servidor
                if (!nextEndpoint.equals(MY_ENDPOINT)) {
                    System.out.println("A enviar mensagem para o secundario...");

                    SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory
                            .newInstance();
                    SOAPConnection soapConnection = soapConnectionFactory.createConnection();
                    soapConnection.call(inboundMessage, new URL(nextEndpoint));
                    if (DEBUG) {
                        System.out.println("Message:");
                        inboundMessage.writeTo(System.out);
                        System.out.println();
                    }
                }

            } catch (SOAPException e) {
                System.out.println("Lançou uma SoapException.");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Lançou uma IOException.");
                e.printStackTrace();
            }
        } else {
            lastMessage = smc.getMessage();
            System.out.println("Sleeping");
            try {
                Thread.sleep(1500);
            } catch (Exception e) {
                System.out.println("Problema a fazer sleep");
            }
            System.out.println("Awake");
            if (DEBUG) {
                System.out.println("Sending reply");
                try {
                    smc.getMessage().writeTo(System.out);
                } catch (SOAPException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println();
            }
        }
        return true;
    }

    // Verifica se a mensagem é um pedido de next Adress do próximo servidor secundário na fila.
    // Retorna true caso seja e false em caso contrário.
    private boolean isGetNextAdress(SOAPMessage inboundMessage) throws SOAPException {
        String value = inboundMessage.getSOAPHeader().getAttribute(GET_NEXT_ADDRESS);
        if (!value.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isGiveNextAdress(SOAPMessage inboundMessage) throws SOAPException {
        String value = inboundMessage.getSOAPHeader().getAttribute(GIVE_NEXT_ADDRESS);
        if (!value.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isImAlive(SOAPMessage inboundMessage) throws SOAPException {
        String value = inboundMessage.getSOAPHeader().getAttribute(IM_ALIVE);
        if (!value.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCheckAvailable(SOAPMessage inboundMessage) throws SOAPException {
        String value = inboundMessage.getSOAPHeader().getAttribute("CheckAvailable");
        if (!value.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static void setMyEndpoint(String endpoint) {
        if (MY_ENDPOINT == null) {
            MY_ENDPOINT = endpoint;
        }
    }

    public static void setNextEndpoint(String endpoint) {
        nextEndpoint = endpoint;
    }

    public static void sendMessage(String property, String value, String endpoint) throws SOAPException,
            IOException {
        MessageFactory mf = MessageFactory.newInstance();
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        SOAPMessage m = mf.createMessage();
        m.getSOAPHeader().setAttribute(property, value);
        SOAPMessage returnmessage = soapConnection.call(m, endpoint);

        if (DEBUG) {
            System.out.println("Mensagem que é enviada pelo call:");
            m.writeTo(System.out);
            System.out.println("Mensagem (depois do call):");
            returnmessage.writeTo(System.out);
            System.out.println("Fim da mensagem.");
        }
    }

}
