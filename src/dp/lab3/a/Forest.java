package dp.lab3.a;

public class Forest {
    static final int BEE_COUNT = 10;
    static final int HONEY_POT_CAPACITY = 100;

    public static void main(String[] args) {
        HoneyPot pot = new HoneyPot(HONEY_POT_CAPACITY);

        Bee[] bees = new Bee[BEE_COUNT];

        for (int i = 0; i < BEE_COUNT; i++) {
            bees[i] = new Bee(pot);
        }

        new WinniePooh(pot);
    }
}
