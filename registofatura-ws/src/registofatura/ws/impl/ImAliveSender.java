package registofatura.ws.impl;

import java.io.IOException;

import javax.xml.soap.SOAPException;

import registofatura.ws.impl.handler.MessageHandler;

public class ImAliveSender extends
        Thread {
    private String nextEndpoint;

    /**
     * @param nextEndpoint the nextEndpoint to set
     */
    public void setNextEndpoint(String nextEndpoint) {
        this.nextEndpoint = nextEndpoint;
    }

    @Override
    public void run() {
        boolean run = true;
        while (run) {
            try {
                MessageHandler.sendMessage(MessageHandler.IM_ALIVE, MessageHandler.IM_ALIVE_VALUE,
                        nextEndpoint);
                System.out.println("enviou im alive");
                sleep(RegistoFaturaMain.MESSAGE_INTERVAL);
            } catch (SOAPException e) {
                System.out.println("Lançou uma SoapException a enviar ImAlive.");
                MessageHandler.setNextEndpoint(MessageHandler.MY_ENDPOINT);
                run = false;
            } catch (IOException e) {
                System.out.println("Lançou uma IOException a enviar ImAlive.");
//                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("ImAliveSender interrompido quando não era suposto.");
//                e.printStackTrace();
            }
        }
    }
}
