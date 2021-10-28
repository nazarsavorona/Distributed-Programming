package dp.lab5.b;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;

public class ThreadsManager extends Thread {
    private static final Random random = new Random();

    private StringWithCounting string;
    private final StringModifier modifier;
    private final CyclicBarrier barrier;
    private final CyclicBarrier gate;

    public ThreadsManager(CyclicBarrier gate, CyclicBarrier barrier) {
        this.modifier = new StringModifier();
        this.barrier = barrier;
        this.gate = gate;

        int coef = (int) 1e+4;
        initializeString(5 * coef, 10 * coef);
    }

    private void initializeString(int minLength, int maxLength) {
        int length = random.nextInt(maxLength - minLength) + minLength;

        char[] stringChars = new char[length];
        char[] chars = {'A', 'B', 'C', 'D'};

        for (int i = 0; i < length; ++i) {
            stringChars[i] = chars[random.nextInt(chars.length)];
        }

        string = new StringWithCounting(String.valueOf(stringChars));
    }

    @Override
    public void run() {
        Main.logger.log(Level.INFO, Thread.currentThread().getName() + " is waiting for other threads to begin " +
                "simultaneously.\n");
        try {
            gate.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        Main.logger.log(Level.INFO, Thread.currentThread().getName() + " has started running.\n");

        while (!isInterrupted()) {
            modifier.modifyString(string);
            modifier.tryToJoinTheBarrier(string, barrier);
        }
    }
}