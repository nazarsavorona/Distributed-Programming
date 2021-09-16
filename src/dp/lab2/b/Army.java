package dp.lab2.b;

import java.util.ArrayList;
import java.util.Arrays;

public class Army {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse(new ArrayList<>(Arrays.asList("a", "b", "c", "d")));
        Buffer hands = new Buffer();
        Truck truck = new Truck();

        Thread ivanov = new Thread(new Ivanov(warehouse, hands));
        Thread petrov = new Thread(new Petrov(warehouse, hands, truck));
        Thread nechyporchuk = new Thread(new Nechyporchuk(warehouse, truck));

        ivanov.start();
        petrov.start();
        nechyporchuk.start();
    }
}
