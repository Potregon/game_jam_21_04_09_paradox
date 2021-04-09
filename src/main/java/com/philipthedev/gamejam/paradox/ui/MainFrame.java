package com.philipthedev.gamejam.paradox.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Frame to display the game.
 */
public final class MainFrame extends JFrame {

    private static final MainFrame mainFrame = new MainFrame();

    private boolean alive = true;
    private Scene scene = null;

    //singleton
    private MainFrame() {
        setTitle("The Paradox that Ends the World");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);

        Thread renderInvocationLoop = new Thread(this::renderInvocationLoop);
        renderInvocationLoop.setName("Render Invocation Loop");
        renderInvocationLoop.start();
    }

    public static MainFrame get() {
        return mainFrame;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        Scene scene = this.scene;
        if (scene != null) {
            Insets insets = getInsets();
            Dimension size = new Dimension(getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom);
            Meta meta = new Meta(size);
            scene.render(graphics2D, meta, this);
        }
    }

    private void renderInvocationLoop() {
        long currentTimeStamp = System.currentTimeMillis();
        long nextTimeStamp = currentTimeStamp + 10;
        while (alive) {
            if (currentTimeStamp >= nextTimeStamp) {
                repaint(10);
                nextTimeStamp += 10;
            }
            else {
                long dt = nextTimeStamp - currentTimeStamp;
                try {
                    Thread.sleep(dt);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    alive = false;
                }
            }
            currentTimeStamp = System.currentTimeMillis();
        }
    }
}
