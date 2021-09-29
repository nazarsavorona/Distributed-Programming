package dp.lab3.b;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Barbershop {
    private static final Logger LOGGER = Logger.getLogger(Barbershop.class.getName());

    ArrayBlockingQueue<Customer> queue;
    int currentTarget;
    boolean startCut;

    public Barbershop() {
        queue = new ArrayBlockingQueue<>(50);
        LOGGER.setLevel(Level.INFO);
        this.currentTarget = -1;
        this.startCut = false;
    }

    public synchronized void newCustomer(Customer newCustomer) {
        try {
            queue.put(newCustomer);
            LOGGER.log(Level.INFO, "Customer " + Integer.toString(newCustomer.getID()) + " came to barbershop");
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public synchronized void tryToCut(Customer customer) {
        while(currentTarget != customer.getID()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }

        LOGGER.log(Level.INFO, "Customer " + Integer.toString(customer.getID()) + "`s waiting is over");
        startCut = true;
        notifyAll();
    }

    public void nextCustomer() {
        synchronized (this) {
            while (queue.isEmpty()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }

            try {
                Customer currentCustomer = queue.take();
                currentTarget = currentCustomer.getID();
                this.notifyAll();
                while (!startCut) {
                    this.wait();
                }
                startCut = false;
                LOGGER.log(Level.INFO, "Customer " + Integer.toString(currentCustomer.getID()) + " is cutting");
                Thread.sleep(3000);
                LOGGER.log(Level.INFO, "Customer " + Integer.toString(currentCustomer.getID()) + " cutting is finished");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

}
