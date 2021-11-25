package dp.module1.lab2.b;

public class Petrov implements Runnable {
    private Buffer hands;
    private Truck truck;
    private Warehouse warehouse;

    public Petrov(Warehouse warehouse, Buffer hands, Truck truck) {
        this.hands = hands;
        this.truck = truck;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        while (!warehouse.isEmpty() || !hands.isEmpty()) {
            truck.setGood(hands.getGood());
        }

    }
}
