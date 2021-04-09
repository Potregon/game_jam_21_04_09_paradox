package com.philipthedev.gamejam.paradox.model;

import java.awt.*;
import java.awt.image.ImageObserver;

public interface ActionButton {

    void selected(Model model);

    void deselected(Model model);

    void render(Graphics2D g, int tileSize, ImageObserver imageObserver);

}
