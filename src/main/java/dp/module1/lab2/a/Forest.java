package dp.module1.lab2.a;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Forest {
    static Logger logger = Logger.getLogger(Forest.class.getName());

    private static final int ROWS = 30000;
    private static final int COLUMNS = 30000;
    private static final int THREAD_COUNT = 3;

    private static final boolean[][] field = new boolean[ROWS][COLUMNS];

    public static void main(String[] args) {
        Beehive hive;

        logger.log(Level.INFO, "Winnie is being wanted");
        generateField();
        hive = new Beehive(field, THREAD_COUNT);
        hive.createAndStartBees();
    }

    private static void generateField() {
        int rowsWinnieLocation = ThreadLocalRandom.current().nextInt(0, ROWS);
        int columnsWinnieLocation = ThreadLocalRandom.current().nextInt(0, COLUMNS);

        field[rowsWinnieLocation][columnsWinnieLocation] = true;
    }
}
