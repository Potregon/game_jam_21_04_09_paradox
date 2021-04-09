package com.philipthedev.gamejam.paradox.test;

import com.philipthedev.gamejam.paradox.ui.Meta;
import com.philipthedev.gamejam.paradox.ui.Scene;

import java.awt.*;
import java.awt.image.ImageObserver;

public class TestScene implements Scene {

    @Override
    public void render(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, meta.getSize().width, meta.getSize().height);
        g.setColor(meta.isMouseDown() ? Color.RED : Color.BLACK);
        g.fillOval(meta.getMousePosition().x - 15, meta.getMousePosition().y - 15, 30, 30);
    }
}
