package dp.lab3.b;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Barbershop barbershop = new Barbershop();
        new Barber(barbershop);

        Random r = new Random(100);

        new Thread(() -> {
            while (true) {
                try {
                    Thread.currentThread().sleep(Math.abs(100L * r.nextInt()) % 5000);
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    exception.printStackTrace();
                }
                new Customer(barbershop);
            }
        }).start();
    }
}
