package dp.module1.lab3.a;

public class HoneyPot {
    final int capacity;

    int currentSize;
    boolean isBusy;

    public HoneyPot(int capacity) {
        this.capacity = capacity;
        this.currentSize = 0;
        this.isBusy = false;
    }

    public synchronized void addHoney() {
        while (isBusy || isFull()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        isBusy = true;

        if (!isFull()) {
            currentSize++;
            System.out.println(Thread.currentThread().getName() + " added honey. Now it`s size is " + Integer.toString(size()));
        }

        if (isFull()) {
            notifyAll();
        }

        isBusy = false;
    }

    public synchronized void empty() {
        while (isBusy || !isFull()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Honey pot is empty");

        isBusy = true;
        currentSize = 0;
        isBusy = false;

        notifyAll();
    }

    public synchronized int size() {
        return currentSize;
    }

    public boolean isFull() {
        return currentSize == capacity;
    }
}
