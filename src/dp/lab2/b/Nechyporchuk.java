package dp.lab2.b;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Nechyporchuk implements Runnable {
    private static final Logger logger =
            Logger.getLogger(Nechyporchuk.class.getName());

    private Warehouse warehouse;
    private Truck truck;
    private int totalIncome;

    public Nechyporchuk(Warehouse warehouse, Truck truck) {
        this.truck = truck;
        this.warehouse = warehouse;
        this.totalIncome = 0;
    }

    @Override
    public void run() {
        while (!warehouse.isEmpty() || !truck.isEmpty()) {
            String stolenGood = truck.getGood();
            if (stolenGood != null) {
                totalIncome += getCost(stolenGood);
            }
        }
        String message = "Total : " + totalIncome;

        logger.log(Level.INFO, message);
    }

    private int getCost(String item) {
        int cost = 0;

        for (char ch : item.toCharArray()) {
            cost += ch;
        }

        return cost;
    }
}
