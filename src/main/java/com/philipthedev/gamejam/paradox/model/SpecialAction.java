package com.philipthedev.gamejam.paradox.model;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Something like get goods.
 */
public interface SpecialAction {

    void doAction(Model model);

    void render(Graphics2D g, ImageObserver imageObserver);

}
