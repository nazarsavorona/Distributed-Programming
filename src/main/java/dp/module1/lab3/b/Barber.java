package dp.module1.lab3.b;

public class Barber implements Runnable {
    Barbershop barbershop;

    public Barber(Barbershop barbershop) {
        this.barbershop = barbershop;

        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            barbershop.nextCustomer();
        }
    }
}
