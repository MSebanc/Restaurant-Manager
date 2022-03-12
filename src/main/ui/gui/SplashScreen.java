package ui.gui;

import javax.swing.*;
import java.awt.*;

// Code From Stack OverFlow: https://stackoverflow.com/questions/16134549/how-to-make-a-splash-screen-for-gui
public class SplashScreen {
    private final JWindow window;
    private long startTime;
    private int minimumMilliseconds;

    public SplashScreen() {
        window = new JWindow();
        ImageIcon image = new ImageIcon("./data/images/loading.gif");
        window.getContentPane().add(new JLabel("", image, SwingConstants.CENTER));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setBounds((int) ((screenSize.getWidth() - image.getIconWidth()) / 2),
                (int) ((screenSize.getHeight() - image.getIconHeight()) / 2),
                image.getIconWidth(), image.getIconHeight());
    }

    public void show(int minimumMilliseconds) {
        this.minimumMilliseconds = minimumMilliseconds;
        window.setVisible(true);
        startTime = System.currentTimeMillis();
    }

    public void hide() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        try {
            Thread.sleep(Math.max(minimumMilliseconds - elapsedTime, 0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.dispose();
    }
}