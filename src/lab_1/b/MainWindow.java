package lab_1.b;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    static Thread thread1;
    static Thread thread2;

    static JButton startBtn1;
    static JButton finishBtn1;
    static JButton startBtn2;
    static JButton finishBtn2;

    static JSlider slider;
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

        slider = new JSlider();
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
        startBtn1.addActionListener(e -> {
            if (semaphore == 0) {
                thread1 = new Thread(new SemaphoreThread(10, Thread.MIN_PRIORITY, slider));
                thread1.start();
                semaphore = 1;
            } else {
                if (thread1 != null && thread1.isAlive()) {
                    return;
                }
                text.setText("Thread 2 is working");
            }
        });

        finishBtn1.addActionListener(e -> {
            if (semaphore == 1 && thread1 != null && thread1.isAlive()) {
                text.setText("");
                thread1.interrupt();

                semaphore = 0;
            } else {
                text.setText("Stopping wrong thread");
            }
        });


        startBtn2.addActionListener(e -> {
            if (semaphore == 0) {
                thread2 = new Thread(new SemaphoreThread(90, Thread.MAX_PRIORITY, slider));
                thread2.start();
                semaphore = 1;
            } else {
                if (thread2 != null && thread2.isAlive()) {
                    return;
                }
                text.setText("Thread 1 is working");
            }
        });

        finishBtn2.addActionListener(e -> {
            if (semaphore == 1 && thread2 != null && thread2.isAlive()) {
                text.setText("");
                thread2.interrupt();

                semaphore = 0;
            } else {
                text.setText("Stopping wrong thread");
            }
        });
    }
}
