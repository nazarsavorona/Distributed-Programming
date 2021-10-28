package dp.lab5.b;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Main {
    public static final Logger logger = Logger.getLogger("Main");

    private static final List<Thread> threads = new ArrayList<>();
    private static final CyclicBarrier gate = new CyclicBarrier(5);

    public static void main(String[] args) {
        final CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            Main.logger.log(Level.INFO, "3 threads have equal A and B symbols number\n");

            for (Thread thread : threads) {
                thread.interrupt();
            }
        });

        initializeThreads(barrier);
    }

    public static void initializeThreads(CyclicBarrier barrier) {
        IntStream.range(0, 4).forEach(i -> {
            threads.add(new ThreadsManager(gate, barrier));
            threads.get(i).start();
        });

        try {
            gate.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
