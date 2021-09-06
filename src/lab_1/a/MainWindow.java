package lab_1.a;

import javax.swing.*;

public class MainWindow {
    static Thread thread1;
    static Thread thread2;
    static Thread controlThread;
    static JSlider slider;

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(450, 150);

        JPanel panel = getPanel();

        window.setContentPane(panel);
        window.setVisible(true);
    }

    private static synchronized void addToSliderValue(int value) {
        slider.setValue(slider.getValue() + value);
    }

    private static JPanel getPanel() {
        JPanel panel = new JPanel();
        JTextField text = new JTextField(5);
        text.setHorizontalAlignment(SwingConstants.CENTER);
        slider = new JSlider();
        JButton startBtn = new JButton("Start");

        JButton plusBtn1 = new JButton("+");
        JButton plusBtn2 = new JButton("+");
        JButton minusBtn1 = new JButton("-");
        JButton minusBtn2 = new JButton("-");

        startBtn.addActionListener(e -> {
            slider.setValue(50);

            if (startBtn.getText().equals("Finish")) {
                thread1.interrupt();
                thread2.interrupt();

                plusBtn1.setEnabled(false);
                minusBtn1.setEnabled(false);
                plusBtn2.setEnabled(false);
                minusBtn2.setEnabled(false);

                text.setText("");

                startBtn.setText("Start");
            } else {
                controlThread = new Thread(() -> {
                    thread1 = new Thread(() -> {
                        boolean isInterrupted = false;
                        while (!isInterrupted) {
                            addToSliderValue(1);
                            try {
                                Thread.sleep(0, 1);
                            } catch (InterruptedException ex) {
                                isInterrupted = true;
                                Thread.currentThread().interrupt();
                            }
                        }
                    });

                    thread2 = new Thread(() -> {
                        boolean isInterrupted = false;
                        while (!isInterrupted) {
                            addToSliderValue(-1);
                            try {
                                Thread.sleep(0, 1);
                            } catch (InterruptedException ex) {
                                isInterrupted = true;
                                Thread.currentThread().interrupt();
                            }
                        }
                    });

                    thread1.setPriority(Thread.NORM_PRIORITY);
                    thread2.setPriority(Thread.NORM_PRIORITY);
                    thread1.start();
                    thread2.start();

                    while (!Thread.currentThread().isInterrupted()) {
                        text.setText(thread1.getPriority() + " : " + thread2.getPriority());
                    }
                });

                plusBtn1.setEnabled(true);
                minusBtn1.setEnabled(true);
                plusBtn2.setEnabled(true);
                minusBtn2.setEnabled(true);

                startBtn.setText("Finish");

                controlThread.start();
            }
        });

        plusBtn1.setEnabled(false);
        plusBtn1.addActionListener(e -> thread1.setPriority(Math.min(Thread.MAX_PRIORITY, thread1.getPriority() + 1)));

        plusBtn2.setEnabled(false);
        plusBtn2.addActionListener(e -> thread2.setPriority(Math.min(Thread.MAX_PRIORITY, thread2.getPriority() + 1)));

        minusBtn1.setEnabled(false);
        minusBtn1.addActionListener(e -> thread1.setPriority(Math.max(Thread.MIN_PRIORITY, thread1.getPriority() - 1)));

        minusBtn2.setEnabled(false);
        minusBtn2.addActionListener(e -> thread2.setPriority(Math.max(Thread.MIN_PRIORITY, thread2.getPriority() - 1)));

        panel.add(plusBtn1);
        panel.add(minusBtn1);
        panel.add(slider);
        panel.add(plusBtn2);
        panel.add(minusBtn2);
        panel.add(startBtn);
        panel.add(text);

        return panel;
    }
}
