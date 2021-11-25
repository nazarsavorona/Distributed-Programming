package dp.module1.lab4.b;

import java.util.Random;

public class Nature {
    public static void main(String[] args) throws InterruptedException {
        Garden garden = new Garden("GardenConditionsDatabase.bin", 5, 5);
        garden.clearFile();

        Random random = new Random();
        int iterationCount = 5;

        Thread gardener = makeGardener(garden, random, iterationCount);
        Thread nature = makeNature(garden, iterationCount);
        Thread monitor1 = makeFileMonitor(garden, iterationCount);
        Thread monitor2 = makePrintMonitor(garden, iterationCount);

        gardener.start();
        nature.start();
        monitor1.start();
        monitor2.start();

        gardener.join();
        nature.join();
        monitor1.join();
        monitor2.join();
    }

    private static Thread makePrintMonitor(Garden garden, int iterationCount) {
        return new Thread(() -> {
            for (int i = 0; i < iterationCount; ++i) {
                garden.printGarden();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private static Thread makeFileMonitor(Garden garden, int iterationCount) {
        return new Thread(() -> {
            for (int i = 0; i < iterationCount; ++i) {
                garden.writeToFile();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private static Thread makeNature(Garden garden, int iterationCount) {
        return new Thread(() -> {
            for (int i = 0; i < iterationCount * 2; ++i) {
                garden.motherNature();
                System.out.println("Garden has been changed by Mother Nature.\n");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private static Thread makeGardener(Garden garden, Random random, int iterationCount) {
        return new Thread(() -> {
            for (int i = 0; i < iterationCount; ++i) {
                garden.pourPlants();
                System.out.println("Garden has been poured.\n");
                try {
                    Thread.sleep(2000L + random.nextInt() % 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}
