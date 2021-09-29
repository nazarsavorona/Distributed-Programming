package dp.lab3.b;

public class Customer implements Runnable {
    static int ID_COUNT = 0;
    int ID;
    Barbershop barbershop;

    public Customer(Barbershop barbershop) {
        this.barbershop = barbershop;
        ID = ID_COUNT++;

        new Thread(this).start();
    }

    public int getID() {
        return ID;
    }

    @Override
    public void run() {
        barbershop.newCustomer(this);
        barbershop.tryToCut(this);
    }
}
