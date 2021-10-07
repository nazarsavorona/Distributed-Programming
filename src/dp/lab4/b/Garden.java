package dp.lab4.b;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Garden {
    private static final Object lock = new Object();

    private final Boolean[][] conditions;
    private final int length;
    private final int width;
    private final String fileName;
    private Random random;

    private BufferedWriter output;
    private int counter = 0;

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    public Garden(String fileName, int length, int width) {
        this.fileName = fileName;
        this.length = length;
        this.width = width;
        this.conditions = new Boolean[length][width];
        this.random = new Random();

        motherNature();
    }

    public void motherNature() {
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < width; ++j) {
                w.lock();
                try {
                    conditions[i][j] = random.nextBoolean();
                } finally {
                    w.unlock();
                }
            }
        }
    }

    public void printGarden() {
        r.lock();
        try {
            for (Boolean[] row : conditions) {
                System.out.print("| ");
                for (boolean element : row) {
                    System.out.print((element ? 1 : 0) + " ");
                }
                System.out.println("|");
            }
        } finally {
            r.unlock();
        }
        System.out.println();
    }

    public void pourPlants() {
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < width; ++j) {
                synchronized (lock) {
                    if (Boolean.FALSE.equals(conditions[i][j])) {
                        w.lock();
                        try {
                            conditions[i][j] = true;
                        } finally {
                            w.unlock();
                        }
                    }
                }
            }
        }
    }

    public void writeToFile() {
        try {
            output = new BufferedWriter(new FileWriter(fileName, true));
            output.write("Garden condition #" + counter++ + ":\n");
            r.lock();
            try {
                for (Boolean[] row : conditions) {
                    output.write("| ");
                    for (boolean element : row) {
                        output.write((element ? 1 : 0) + " ");
                    }
                    output.write("|\n");
                }
                output.write("\n");
                output.close();
            } finally {
                r.unlock();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearFile() {
        try {
            output = new BufferedWriter(new FileWriter(fileName));
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

