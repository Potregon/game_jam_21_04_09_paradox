package com.philipthedev.gamejam.paradox.model;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Something like get goods.
 */
public interface SpecialAction {

    void doAction(Model model);

    default void renderBackground(Graphics2D g, ImageObserver imageObserver) {

    }

    default void renderForeground(Graphics2D g, ImageObserver imageObserver) {

    }

}
