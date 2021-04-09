package com.philipthedev.gamejam.paradox.model;

import java.awt.*;

public final class FieldAction {

    private final Color color;
    private final Color highlightColor;
    private final Runnable action;

    public FieldAction(Color color, Color highlightColor, Runnable action) {
        this.color = color;
        this.highlightColor = highlightColor;
        this.action = action;
    }

    public Color getColor() {
        return color;
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public Runnable getAction() {
        return action;
    }
}
