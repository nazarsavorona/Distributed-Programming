package dp.module1.lab2.b;

import java.util.List;

public class Warehouse {
    private boolean isBusy;

    private List<String> goods;

    public Warehouse(List<String> goods) {
        this.goods = goods;
    }

    public synchronized String takeGood() {
        while (isBusy) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        isBusy = true;
        String good = null;

        if (!goods.isEmpty()) {
            good = goods.remove(0);
        }

        isBusy = false;
        notifyAll();

        return good;
    }

    public boolean isEmpty() {
        return goods.isEmpty();
    }
}
