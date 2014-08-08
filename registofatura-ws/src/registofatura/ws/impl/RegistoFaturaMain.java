package registofatura.ws.impl;

import java.io.IOException;

import javax.xml.registry.JAXRException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Endpoint;

import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import registofatura.ws.domain.RegistoFatura;
import registofatura.ws.impl.handler.MessageHandler;
import registofatura.ws.uddi.UDDINaming;

public class RegistoFaturaMain {

    private static String uddiURL;
    private static UDDINaming uddiNaming;
    private static String name;
    private static String url;

    /* 
     * Representa a janela maxima, em ms, em que uma réplica fica à espera 
     * de Im Alives da replica que se encontra acima. Após expirar, regista-se
     * novamente na fila.
     * */
    public static long MAX_TIMEOUT = 2000;

    /*
     * Intervalo de envio de mensagens de Im Alive.
     */
    public static long MESSAGE_INTERVAL = MAX_TIMEOUT / 4;

    public RegistoFaturaMain() {
        // TODO Auto-generated constructor stub
    }

    public static void main(final String[] args) {
        // Check arguments
        if (args.length < 4) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s url%n", RegistoFaturaMain.class.getName());
            return;
        }

        uddiURL = args[0];
        name = args[1];
        url = args[2];
        final String db = args[3];
        System.out
                .println("uddiURL: " + uddiURL + " name: " + name + " url: " + url + " db: " + db);

        Endpoint endpoint = null;
        try {
            FenixFramework.initialize(new Config() {
                {
                    dbAlias = db;
                    dbUsername = "rest";
                    dbPassword = "r3st";
                    domainModelPath = "src/registofatura/dml/domain.dml";
                    rootClass = RegistoFatura.class;
                }
            });

            endpoint = Endpoint.create(new RegistoFaturaImpl());


            System.out.println("Publishing endpoint.");


            endpoint.publish(url);
            MessageHandler.setMyEndpoint(url);
            MessageHandler.setNextEndpoint(url);

            System.out.println("My and Next are set.");
            uddiNaming = new UDDINaming(uddiURL);
            //uddiNaming.unbind(name);
            registerReplica();

            // wait
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();
            MessageHandler.sender.stop();
            MessageHandler.receiver.stop();

        } catch (Exception e) {
            System.out.printf("Caught exception: %s%n", e);
            //            e.printStackTrace();

        } finally {
            try {
                if (endpoint != null) {
                    // stop endpoint
                    endpoint.stop();
                    System.out.printf("Stopped %s%n", url);
                }
            } catch (Exception e) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
            try {
                if (uddiNaming != null) {
                    // delete from UDDI
                    uddiNaming.unbind(name);
                    System.out.printf("Deleted '%s' from UDDI%n", name);
                }
            } catch (Exception e) {
                System.out.printf("Caught exception when deleting: %s%n", e);
            }
        }
    }

    public static void registerReplica() throws JAXRException, SOAPException, IOException {
        System.out.println("Looking up...");
        String possiblePrimary = uddiNaming.lookup(name);
        System.out.println("After Look up");
        System.out.println("Possible primary : " + possiblePrimary);
        if (possiblePrimary == null) {
            System.out.println("IS PRIMARY");
            // publish endpoint
            System.out.printf("Starting %s%n", url);
            // publish to UDDI
            System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
            uddiNaming.rebind(name, url);
        } else if (!possiblePrimary.equals(MessageHandler.MY_ENDPOINT)) {
            System.out.println("IS SECUNDARY");
            MessageFactory mf = MessageFactory.newInstance();
            // request adress messages
            SOAPMessage message = mf.createMessage();
            // pede o próximo endpoint e envia logo o seu, caso o endpoint para onde envia
            // nao tenha proximo para que lhe comece a enviar imediatamente "I'm alives" 
            message.getSOAPHeader().setAttribute(MessageHandler.GET_NEXT_ADDRESS,
                    MessageHandler.MY_ENDPOINT);

            if (MessageHandler.DEBUG) {
                System.out.println("Calling message to " + possiblePrimary + "...");
            }

            SOAPConnectionFactory cf = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = cf.createConnection();

            try {
                soapConnection.call(message, possiblePrimary);
            } catch (Exception e) {
                System.out.println(e.getMessage()
                        + "\nTentou contactar o primario; primario morreu ---> REBINDA-SE");
                uddiNaming.rebind(name, url);
            }

            if (MessageHandler.DEBUG) {
                System.out.println("Sent message to primary >>");
                message.writeTo(System.out);
            }
        } else {
            //Ele próprio morreu enquanto primário e foi lançado novamente sem que ninguém o tenha substituído; não precisa de fazer rebind
            System.out.println("IS PRIMARY AGAIN");
        }
    }
}
