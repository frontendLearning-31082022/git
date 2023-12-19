package MouseAlive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;

public class MouseAlive extends Thread {

    Point UPleft = null;
    Point DOWNleft = null;
    Point UPright = null;
    Point DOWNright = null;
    Robot robot = null;

    boolean mouseStop = false;

    public static void main(String[] args) {
        new MouseAlive().start();
    }

    public MouseAlive() {


    }


    void setArea_hardcode() {
        UPleft = new Point(331, 286);
        DOWNleft = new Point(295, 815);
        UPright = new Point(1622, 231);
        DOWNright = new Point(1600, 913);
    }


    void showFrame() {

        Thread thread = new Thread() {
            public void run() {
                JFrame frame = new JFrame("JFrame Example");
                JPanel panel = new JPanel();
                panel.setLayout(new FlowLayout());
                JLabel label = new JLabel("mouse working");
                JButton button = new JButton();
                button.setText("Стоп Mouse");

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mouseStop = !mouseStop;
                        label.setText("mouseStop " + mouseStop);
                    }
                });
                panel.add(label);
                panel.add(button);
                frame.add(panel);
                frame.setSize(120, 100);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        };
        thread.start();


    }

    void mouseMove(int x, int y) {
        Point pointCurrent = MouseInfo.getPointerInfo().getLocation();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }

        Point pointNow = MouseInfo.getPointerInfo().getLocation();
        if (pointNow.x != pointCurrent.x || pointNow.y != pointNow.y) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }

        checkNeedStop();
        robot.mouseMove(x, y);
    }

    private void checkNeedStop() {
        while (mouseStop) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void run() {
        setArea_hardcode();
        showFrame();

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            Point rndPoint = new Point(ThreadLocalRandom.current().nextInt(UPleft.x, UPright.x)
                    , ThreadLocalRandom.current().nextInt(UPleft.y, DOWNright.y));

            Point pointCurrent = MouseInfo.getPointerInfo().getLocation();
            mouseGlide(pointCurrent.x, pointCurrent.y, rndPoint.x, rndPoint.y, 300, 100);

            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 5000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void mouseGlide(int x1, int y1, int x2, int y2, int t, int n) {
        try {
            Robot r = new Robot();
            double dx = (x2 - x1) / ((double) n);
            double dy = (y2 - y1) / ((double) n);
            double dt = t / ((double) n);
            for (int step = 1; step <= n; step++) {
                Thread.sleep((int) dt);
                mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));

            }
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
