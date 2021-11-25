package dp.module1.lab2.b;

public class Truck extends Buffer {
    private int lastGivenGoodIndex;

    public Truck() {
        super();
        this.lastGivenGoodIndex = -1;
    }

    @Override
    public synchronized String getGood() {
        String goodToPass = null;

        if (goods.size() > lastGivenGoodIndex + 1) {
            goodToPass = goods.get(++lastGivenGoodIndex);
        }

        isBusy = false;

        return goodToPass;
    }

    @Override
    public boolean isEmpty() {
        return goods.size() == lastGivenGoodIndex + 1;
    }
}
