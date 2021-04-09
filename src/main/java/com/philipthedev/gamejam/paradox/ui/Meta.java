package com.philipthedev.gamejam.paradox.ui;

import java.awt.*;
import java.util.Random;

public class Meta {

    private final Dimension size;
    private static final Random random = new Random();

    public Meta(Dimension size) {
        this.size = size;
    }

    public Dimension getSize() {
        return size;
    }

    public Random getRandom() {
        return random;
    }
}

