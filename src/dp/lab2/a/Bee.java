package dp.lab2.a;

public class Bee implements Runnable {
    private final Beehive hive;

    public Bee(Beehive hive) {
        this.hive = hive;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (hive) {
                boolean[] currentRow = hive.getBeeTask();

                if (currentRow.length == 0 || hive.checkWinnieFoundStatus()) {
                    break;
                }

                for (var cell : currentRow) {
                    if (cell) {
                        hive.setWinnieFound();
                        break;
                    }
                }
            }
        }
    }
}
