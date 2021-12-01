package dp.module1.lab5.a;

import java.util.ArrayList;

public class ThreadManager {
    private int reachedBarrier;
    private final int numberOfThreads;

    private final ArrayList<Thread> threads;
    private final Recruits recruits;

    public static void main(String[] args) {
        new ThreadManager(new Recruits(160), 50);
    }

    public ThreadManager(Recruits recruits, int minimumNumberPerThread) {
        int recruitsNumber = recruits.getRecruits().length;

        this.numberOfThreads = recruitsNumber / minimumNumberPerThread;
        this.reachedBarrier = 0;
        this.recruits = recruits;
        this.threads = new ArrayList<>();

        int numberPerThread = (int) Math.ceil((double) recruitsNumber / numberOfThreads);

        for (int i = 0; i < numberOfThreads; ++i) {
            int beginIndex = numberPerThread * i;
            int endIndex = Math.min(numberPerThread + beginIndex, recruitsNumber);
            Thread thread = new RecruitsManager(beginIndex, endIndex, recruits, this);
            threads.add(thread);
            thread.start();
        }
    }

    public synchronized void incrementReachedBarrier() {
        reachedBarrier++;
        if (reachedBarrier == numberOfThreads) {
            recruits.printRecruits();
            if (!recruits.isStationary()) {
                reachedBarrier = 0;
                recruits.setPreviousState();
                notifyAll();
            } else {
                System.out.println("Stationary state has been reached!");
                for (Thread thread : threads) {
                    thread.interrupt();
                }
            }
        } else {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
