package dp.module1.lab3.a;

public class WinniePooh implements Runnable {
    HoneyPot pot;

    public WinniePooh(HoneyPot pot) {
        this.pot = pot;

        new Thread(this, "Bear").start();
    }

    @Override
    public void run() {
        while (true) {
            pot.empty();
        }
    }
}
