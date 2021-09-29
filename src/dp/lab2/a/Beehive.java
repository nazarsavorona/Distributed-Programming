package dp.lab2.a;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Beehive {
    static Logger logger = Logger.getLogger(Beehive.class.getName());

    private final boolean[][] field;
    private final Thread[] bees;
    private boolean isWinnieFound;
    private int lastGivenRow;

    private Instant timer;

    private final Object sync = new Object();


    public Beehive(boolean[][] field, int beesCount) {
        this.field = field;
        this.bees = new Thread[beesCount];
        this.isWinnieFound = false;
        this.lastGivenRow = -1;
    }

    public void createAndStartBees() {
        this.timer = Instant.now();

        for (int i = 0; i < this.bees.length; i++) {
            this.bees[i] = new Thread(new Bee(this));
            this.bees[i].start();
        }
    }

    public boolean checkWinnieFoundStatus() {
        return isWinnieFound;
    }

    public boolean[] getBeeTask() {
        synchronized (sync) {
            if (hasTask()) {
                return field[++lastGivenRow];
            }

            throw new IllegalArgumentException();
        }
    }

    public boolean hasTask() {
        return field.length < lastGivenRow;
    }

    public void setWinnieFound() {
        isWinnieFound = true;

        String message = String.format("Winnie is found successfully%nDuration : %,d nanoseconds%n",
                Duration.between(timer, Instant.now()).toNanos());
        logger.log(Level.INFO, message);
    }
}
