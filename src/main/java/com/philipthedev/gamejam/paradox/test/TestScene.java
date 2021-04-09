package com.philipthedev.gamejam.paradox.test;

import com.philipthedev.gamejam.paradox.ui.Meta;
import com.philipthedev.gamejam.paradox.ui.Scene;

import java.awt.*;
import java.awt.image.ImageObserver;

public class TestScene implements Scene {

    @Override
    public void render(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        Color color = new Color(meta.getRandom().nextInt());
        g.setColor(color);
        g.fillRect(0, 0, meta.getSize().width, meta.getSize().height);
    }
}
