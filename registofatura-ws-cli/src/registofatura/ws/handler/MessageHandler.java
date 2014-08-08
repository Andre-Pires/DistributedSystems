package registofatura.ws.handler;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.IOException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.registry.JAXRException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import registofatura.ws.ClienteMain;

public class MessageHandler
        implements SOAPHandler<SOAPMessageContext> {

    private static int seqNum = 1;

    public MessageHandler() {
    }

    @Override
    public boolean handleMessage(SOAPMessageContext smc) {
        Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound) {
            try {
                String endpoint = (String) smc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);

                SOAPConnectionFactory soapConnectionFactory;
                soapConnectionFactory = SOAPConnectionFactory.newInstance();
                SOAPConnection soapConnection = soapConnectionFactory.createConnection();
                MessageFactory mf = MessageFactory.newInstance();
                SOAPMessage message = mf.createMessage();
                message.getSOAPHeader().setAttribute("CheckAvailable", "Hello");

                SOAPHeader soapHeader = null;
                try {
                    soapHeader = smc.getMessage().getSOAPHeader();
                } catch (SOAPException e2) {
                    smc.getMessage().getSOAPPart().getEnvelope().addHeader();
                    soapHeader = smc.getMessage().getSOAPHeader();
                }
                if (soapHeader == null) {
                    smc.getMessage().getSOAPPart().getEnvelope().addHeader();
                    soapHeader = smc.getMessage().getSOAPHeader();
                }
                Name seqName = smc.getMessage().getSOAPPart().getEnvelope()
                        .createName("seqNum", "sq", "http://www.RegistoFatura.com");
                SOAPElement element = soapHeader.addHeaderElement(seqName);
                element.addTextNode("" + seqNum);

                System.out.println("Mensagem a sair");

                while (true) {
                    try {
                        System.out.println("Checking if alive");
                        soapConnection.call(message, endpoint);
                        System.out.println("Is alive, sending message " + seqNum);
                        System.out.println("Sending message:");
                        smc.getMessage().writeTo(System.out);
                        break;
                    } catch (SOAPException e) {
                        endpoint = ClienteMain.getUddiNaming().lookup(
                                ClienteMain.getWebserviceName());
                        smc.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
                        ((BindingProvider) ClienteMain.port).getRequestContext().put(
                                ENDPOINT_ADDRESS_PROPERTY, endpoint);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SOAPException se) {
                se.printStackTrace();
            } catch (JAXRException e1) {
                e1.printStackTrace();
            }
            return true;
        } else {
            System.out.println("\nReceived message:");
            try {
                smc.getMessage().writeTo(System.out);
            } catch (SOAPException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
            seqNum++;
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        seqNum++;
        return true;
    }

    @Override
    public void close(MessageContext context) {
        //Do nothing
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

}
