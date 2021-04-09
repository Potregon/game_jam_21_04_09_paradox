package com.philipthedev.gamejam.paradox.ui;

import java.awt.*;
import java.util.Random;

public class Meta {

    private static final Random random = new Random();

    private final Dimension size;
    private final Point mousePosition;
    private final boolean mouseDown;

    public Meta(Dimension size,
                Point mousePosition,
                boolean mouseDown) {
        this.size = size;
        this.mousePosition =  mousePosition;
        this.mouseDown = mouseDown;
    }

    public Dimension getSize() {
        return size;
    }

    public Point getMousePosition() {
        return mousePosition;
    }

    public boolean isMouseDown() {
        return mouseDown;
    }

    public Random getRandom() {
        return random;
    }
}

