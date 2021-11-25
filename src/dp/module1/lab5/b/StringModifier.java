package dp.module1.lab5.b;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;

public class StringModifier {
    private static final Random random = new Random();

    public synchronized void modifyString(StringWithCounting string) {
        for (int i = 0; i < string.getString().length(); ++i) {
            if (string.areABEqual()) {
                break;
            }
            switch (string.getCharAtPos(i)) {
                case 'A' -> {
                    if (random.nextBoolean()) {
                        string.setCharAtPos('C', i);
                    }
                }
                case 'B' -> {
                    if (random.nextBoolean()) {
                        string.setCharAtPos('D', i);
                    }
                }
                case 'C' -> {
                    if (random.nextBoolean()) {
                        string.setCharAtPos('A', i);
                    }
                }
                case 'D' -> {
                    if (random.nextBoolean()) {
                        string.setCharAtPos('B', i);
                    }
                }
                default -> {
                    Main.logger.log(Level.WARNING, "Unknown letter\n");
                }
            }
        }
    }

    public synchronized void tryToJoinTheBarrier(StringWithCounting string, CyclicBarrier barrier) {
        try {
            if (string.areABEqual() && barrier.getParties() != barrier.getNumberWaiting()) {
                Main.logger.log(Level.INFO, Thread.currentThread().getName() + " has reached barrier.\nA's = " +
                        string.getAs() + "; B's = " +
                        string.getBs() + "\n" +
                        "Waiting threads: " + barrier.getNumberWaiting() + "\n");

                barrier.await();

                Main.logger.log(Level.INFO, Thread.currentThread().getName() + " has finished its work.\n");
            }

        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
    }
}
