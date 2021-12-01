package dp.module1.lab2.b;

import java.util.ArrayList;

public class Buffer {
    protected ArrayList<String> goods;
    protected boolean isBusy;

    public Buffer() {
        this.goods = new ArrayList<>();
    }

    public boolean isEmpty() {
        return goods.isEmpty();
    }

    public synchronized void setGood(String good) {
        while (isBusy) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        isBusy = true;
        this.goods.add(good);
        isBusy = false;

        notifyAll();
    }

    public synchronized String getGood() {
        while (isBusy) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        isBusy = true;
        String goodToPass = null;

        if (!goods.isEmpty()) {
            goodToPass = goods.remove(0);
        }

        isBusy = false;
        notifyAll();

        return goodToPass;
    }
}
