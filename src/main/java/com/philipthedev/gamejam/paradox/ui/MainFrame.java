package com.philipthedev.gamejam.paradox.ui;

import com.philipthedev.gamejam.paradox.model.foes.ChronoTroll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Frame to display the game.
 */
public final class MainFrame extends JFrame implements MouseMotionListener, MouseListener, KeyListener {

    private static final MainFrame mainFrame = new MainFrame();

    private Point   mousePosition = new Point(0, 0);
    private boolean mouseDown     = false;

    private boolean alive = true;
    private Scene scene = null;

    //singleton
    private MainFrame() {
        setTitle("Clear the Dungeon in Time");
        setIconImage(ChronoTroll.timeSplitter);
        setBounds(0, 0, 50, 50);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(false);

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

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
        Scene scene = this.scene;
        if (scene != null) {
            Insets insets = getInsets();
            Dimension size = new Dimension(getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom);
            boolean mouseDown = this.mouseDown;
            Meta meta = new Meta(size, mousePosition, mouseDown);
            BufferedImage ghostImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2D = ghostImage.createGraphics();
            scene.render(g2D, meta, this);
            g2D.dispose();
            g.drawImage(ghostImage, 0 ,0, this);
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

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        this.mousePosition = new Point(x, y);
        mouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        this.mousePosition = new Point(x, y);
        mouseDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        this.mousePosition = new Point(x, y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        this.mousePosition = new Point(x, y);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            MainFrame.get().setScene(new OutroScene());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
