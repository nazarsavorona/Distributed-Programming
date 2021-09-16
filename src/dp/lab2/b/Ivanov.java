package dp.lab2.b;

public class Ivanov implements Runnable {
    private Warehouse warehouse;
    private Buffer commonHands;

    public Ivanov(Warehouse warehouse, Buffer commonHands) {
        this.warehouse = warehouse;
        this.commonHands = commonHands;
    }

    @Override
    public void run() {
        while (!warehouse.isEmpty()) {
            String stolenGood = warehouse.takeGood();
            commonHands.setGood(stolenGood);
        }
    }
}

