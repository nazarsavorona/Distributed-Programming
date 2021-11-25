package dp.module1.lab5.a;

import java.util.Objects;
import java.util.Random;

public class Recruits {
    private final int amount;

    private final Boolean[] newbies;
    private final Boolean[] previousState;

    private static final Random random = new Random();
    private static final Object lock = new Object();

    public Recruits(int amount) {
        this.amount = amount;
        this.newbies = new Boolean[amount];
        this.previousState = new Boolean[amount];

        initiate();
        setPreviousState();
    }

    public void setPreviousState() {
        if (amount >= 0)
            System.arraycopy(newbies, 0, previousState, 0, amount);
    }

    public Boolean[] getRecruits() {
        return newbies;
    }

    public void initiate() {
        for (int i = 0; i < amount; ++i) {
            newbies[i] = random.nextBoolean();
        }
    }

    public void printRecruits() {
        System.out.print("[ ");
        for (int i = 0; i < amount; ++i) {
            System.out.print(newbies[i] ? ">" : "<");
        }
        System.out.println(" ]");
    }

    public void partFixing(int begin, int end) {
        for (int i = begin; i < end - 1; ++i) {
            if (newbies[i] && !newbies[i + 1]) {
                newbies[i] = false;
                newbies[i + 1] = true;
            }
        }
        if (end != newbies.length && newbies[end - 1] && !newbies[end]) {
            newbies[end - 1] = false;
            synchronized (lock) {
                newbies[end] = true;
            }
        }

    }

    public synchronized boolean isStationary() {
        for (int i = 0; i < amount; ++i) {
            if (!Objects.equals(newbies[i], previousState[i])) {
                return false;
            }
        }
        return true;
    }
}
