package registofatura.ws.impl;


public class ImAliveReceiver extends
        Thread {
    private boolean alive = true;

    @Override
    public void run() {
        while (alive) {
            try {
                sleep(10000);
                // timeout
                RegistoFaturaMain.registerReplica();
                alive = false;
            } catch (InterruptedException e) {
                // recebeu um im alive
                // continue
                System.out.println("recebeu im alive");
            } catch (Exception e) {
                System.out.println("Caught exception while recovering from re-setup"
                        + e.getMessage());
            }
        }
    }

    public void setAlive(boolean val) {
        this.alive = val;
    }
}
