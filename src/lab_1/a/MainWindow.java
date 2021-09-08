package lab_1.a;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindow {
    private static final Logger logger = Logger.getLogger(MainWindow.class.getName());
    private static final String START_LABEL = "Start";
    private static final String FINISH_LABEL = "Finish";

    static Thread thread1;
    static Thread thread2;
    static Thread controlThread;

    static JButton plusBtn1;
    static JButton plusBtn2;
    static JButton minusBtn1;
    static JButton minusBtn2;
    static JButton startBtn;

    static JSlider slider;
    static JTextField text;

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(450, 150);

        JPanel panel = getPanel();

        window.setContentPane(panel);
        window.setVisible(true);
    }

    private static void addToSliderValue(int value) {
        synchronized (slider) {
            logger.log(Level.INFO, String.format("%s %s : %d", Thread.currentThread().getName(), slider.getValue(), Thread.currentThread().getPriority()));
            if(value < 0) {
                slider.setValue(Math.max(10, slider.getValue() + value));
            } else {
                slider.setValue(Math.min(90, slider.getValue() + value));
            }
            logger.log(Level.INFO, String.format("%s %s : %d", Thread.currentThread().getName(), slider.getValue(), Thread.currentThread().getPriority()));
        }
    }

    private static JPanel getPanel() {
        JPanel panel = new JPanel();

        text = new JTextField(5);
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setEnabled(false);
        text.setDisabledTextColor(Color.BLACK);

        slider = new JSlider();
        slider.setEnabled(false);

        startBtn = new JButton(START_LABEL);

        plusBtn1 = new JButton("+");
        plusBtn2 = new JButton("+");
        minusBtn1 = new JButton("-");
        minusBtn2 = new JButton("-");

        startBtn.addActionListener(e -> {
            slider.setValue(50);

            if (startBtn.getText().equals(START_LABEL)) {
                start();
            } else {
                finish();
            }
        });

        plusBtn1.addActionListener(e -> setThreadPriority(thread1, thread1.getPriority() + 1));
        plusBtn2.addActionListener(e -> setThreadPriority(thread2, thread2.getPriority() + 1));
        minusBtn1.addActionListener(e -> setThreadPriority(thread1, thread1.getPriority() - 1));
        minusBtn2.addActionListener(e -> setThreadPriority(thread2, thread2.getPriority() - 1));

        setEnabledUI(false);

        panel.add(plusBtn1);
        panel.add(minusBtn1);
        panel.add(slider);
        panel.add(plusBtn2);
        panel.add(minusBtn2);
        panel.add(startBtn);
        panel.add(text);

        return panel;
    }

    private static void setEnabledUI(boolean isEnabled) {
        plusBtn1.setEnabled(isEnabled);
        minusBtn1.setEnabled(isEnabled);
        plusBtn2.setEnabled(isEnabled);
        minusBtn2.setEnabled(isEnabled);
    }

    private static void start() {
        controlThread = new Thread(() -> {
            thread1 = new Thread(() -> {
                boolean isInterrupted = false;
                while (!isInterrupted) {
                    try {
                        Thread.sleep(0, 1);
                        addToSliderValue(-1);
                    } catch (InterruptedException ex) {
                        isInterrupted = true;
                        Thread.currentThread().interrupt();
                    }
                }
            });

            thread2 = new Thread(() -> {
                boolean isInterrupted = false;
                while (!isInterrupted) {
                    try {
                        Thread.sleep(0, 1);
                        addToSliderValue(1);
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

            text.setText(thread1.getPriority() + " : " + thread2.getPriority());
        });

        setEnabledUI(true);
        startBtn.setText(FINISH_LABEL);

        controlThread.start();
    }

    private static void finish() {
        thread1.interrupt();
        thread2.interrupt();

        setEnabledUI(false);
        text.setText("");
        startBtn.setText(START_LABEL);
    }

    private static void setThreadPriority(Thread thread, int priority) {
        if (priority < Thread.MIN_PRIORITY) {
            priority = Thread.MIN_PRIORITY;
        }

        if (priority > Thread.MAX_PRIORITY) {
            priority = Thread.MAX_PRIORITY;
        }

        thread.setPriority(priority);
        text.setText(thread1.getPriority() + " : " + thread2.getPriority());
    }
}
