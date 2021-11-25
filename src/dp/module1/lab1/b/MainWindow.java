package dp.module1.lab1.b;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    static Thread thread1;
    static Thread thread2;

    static JButton startBtn1;
    static JButton finishBtn1;
    static JButton startBtn2;
    static JButton finishBtn2;

    static final JSlider slider = new JSlider();
    static JTextField text;

    static int semaphore = 0;

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(650, 150);

        JPanel panel = getPanel();

        window.setContentPane(panel);
        window.setVisible(true);
    }

    private static JPanel getPanel() {
        JPanel panel = new JPanel();

        text = new JTextField(16);
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setEnabled(false);
        text.setDisabledTextColor(Color.BLACK);

        slider.setEnabled(false);

        startBtn1 = new JButton("1. Start");
        finishBtn1 = new JButton("1. Finish");
        startBtn2 = new JButton("2. Start");
        finishBtn2 = new JButton("2. Finish");

        assignThreadBtns();

        panel.add(startBtn1);
        panel.add(finishBtn1);
        panel.add(slider);
        panel.add(startBtn2);
        panel.add(finishBtn2);
        panel.add(text);

        return panel;
    }

    private static void assignThreadBtns() {
        startBtn1.addActionListener(e -> thread1 = startThreadSynchronize(thread1, 10, Thread.MIN_PRIORITY));
        finishBtn1.addActionListener(e -> finishThreadSynchronize(thread1));

        startBtn2.addActionListener(e -> thread2 = startThreadSynchronize(thread2, 90, Thread.MAX_PRIORITY));
        finishBtn2.addActionListener(e -> finishThreadSynchronize(thread2));
    }

    private static Thread startThreadSynchronize(Thread thread, int value, int priority) {
        synchronized (slider) {
            if (semaphore == 0) {
                thread = new Thread(new SemaphoreThread(value, priority, slider));
                thread.start();
                semaphore = 1;
            } else {
                if (thread != null && thread.isAlive()) {
                    return thread;
                }
                text.setText("Another thread is working");
            }
            return thread;
        }
    }

    private static void finishThreadSynchronize(Thread thread) {
        synchronized (slider) {
            if (semaphore == 1 && thread != null && thread.isAlive()) {
                text.setText("");
                thread.interrupt();

                semaphore = 0;
            } else {
                text.setText("Stopping wrong thread");
            }
        }
    }
}
