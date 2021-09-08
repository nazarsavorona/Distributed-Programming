package lab_1.b;

import javax.swing.*;

public class SemaphoreThread implements Runnable {
    private int value;
    private JSlider slider;

    public SemaphoreThread(int value, int priority, JSlider slider) {
        Thread.currentThread().setPriority(priority);

        this.value = value;
        this.slider = slider;
    }

    @Override
    public void run() {
        slider.setValue(value);

        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(0, 0);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
