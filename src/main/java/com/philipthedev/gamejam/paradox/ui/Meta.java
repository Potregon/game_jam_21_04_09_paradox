package com.philipthedev.gamejam.paradox.ui;

import com.philipthedev.gamejam.paradox.model.pathfinding.Position;

import java.awt.*;
import java.util.Random;

public class Meta {

    private static final Random random = new Random();

    private final Dimension size;
    private final Point mousePosition;
    private final boolean mouseDown;

    private Point offsetMousePosition;

    public Meta() {
        this(new Dimension(0, 0), new Point(0, 0), false);
    }

    public Meta(Dimension dimension) {
        this(dimension, new Point(0, 0), false);
    }

    public Meta(Dimension size,
                Point mousePosition,
                boolean mouseDown) {
        this.size = size;
        this.mousePosition =  mousePosition;
        this.mouseDown = mouseDown;
        offsetMousePosition = mousePosition;
    }

    public Dimension getSize() {
        return size;
    }

    public Point getMousePosition() {
        return offsetMousePosition;
    }

    public boolean isMouseDown() {
        return mouseDown;
    }

    public void translate(int offsetX, int offsetY) {
        this.offsetMousePosition = new Point(mousePosition.x - offsetX, mousePosition.y - offsetY);
    }

    public void clear() {
        this.offsetMousePosition = mousePosition;
    }

    public Random getRandom() {
        return random;
    }
}

