package dp.module1.module.task1;

/*
    Аукцион.
    На торги выставляется несколько лотов.
    Участники аукциона делают заявки. Заявку
    можно корректировать в сторону увеличения
    несколько раз за торги одного лота. Аукцион
    определяет победителя и переходит
    к следующему лоту. Участник, не заплативший
    за лот в заданный промежуток времени,
    отстраняется на несколько лотов от торгов.
*/

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

class Auction implements Runnable {
    private int item_index = 0;

    private Lot lot;

    public Auction(Lot lot) {
        this.lot = lot;
    }

    @Override
    public void run() {
        while (true) {
            lot.newLot("Lot" + Integer.toString(item_index++));
        }
    }
}

class Participant implements Runnable {
    private Lot lot;
    private int money;
    private int lastBet;

    public Participant(Lot lot, int money) {
        this.lot = lot;
        this.money = money;
    }

    @Override
    public void run() {
        while (money > 0) {
            lastBet = task9Java.random.nextInt(money);
            try {
                lot.makeBet(lastBet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String result;

            try {
                result = lot.getLot(lastBet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Lot {
    private SynchronousQueue<String> lotsQueue;
    private SynchronousQueue<String> participantsQueue;
    private SynchronousQueue<Integer> maxBetQueue;
    private SynchronousQueue<Integer> winnerQueue;
    private SynchronousQueue<Integer> bannedQueue;

    boolean lotTaken;

    private CountDownLatch cdl;
    private CountDownLatch bannedCdl;


    public void newLot(String name) {
        cdl = new CountDownLatch(1);

        System.out.println("Item " + name + " on the spot");

        try {
            maxBetQueue.put(50);
            winnerQueue.put(-1);
            lotsQueue.put(name);

            lotTaken = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        try {
            while (!cdl.await(5, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        if(!lotTaken) {
            int winner = -1;
            try {
                winner = winnerQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            bannedCdl = new CountDownLatch(5);
            try {
                bannedQueue.put(winner);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getLot(int money) throws InterruptedException {
        int id = winnerQueue.take();
        int requiredMoney = maxBetQueue.take();

        if (Thread.currentThread().getId() == id && money == requiredMoney) {
            lotTaken = true;
            return lotsQueue.take();
        }

        winnerQueue.put(id);
        maxBetQueue.put(requiredMoney);
        return "";
    }

    public void makeBet(int bet) throws InterruptedException {
        int currentMaxBet = maxBetQueue.take();

        if (bet > currentMaxBet) {
            maxBetQueue.put(bet);

            winnerQueue.take();
            winnerQueue.put((int) Thread.currentThread().getId());
        }
    }

}

public class task9Java {
    public static final Random random = new Random();

    private static final int participantsCount = 10;

    public static void main(String[] args) {
        Lot lot = new Lot();

        for (int i = 0; i < participantsCount; i++) {
            new Thread(new Participant(lot, random.nextInt(500, 10000))).start();
        }
        new Thread(new Auction(lot)).start();
    }
}
