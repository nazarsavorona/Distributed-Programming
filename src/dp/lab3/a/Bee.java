package dp.lab3.a;

public class Bee implements Runnable {
    static int BEE_COUNTER = 0;

    String name;
    HoneyPot pot;

    public Bee(HoneyPot pot) {
        this.pot = pot;
        name = "Bee #" + Integer.toString(BEE_COUNTER++);

        new Thread(this, name).start();
    }

    @Override
    public void run() {
        while (true) {
            pot.addHoney();
        }
    }
}