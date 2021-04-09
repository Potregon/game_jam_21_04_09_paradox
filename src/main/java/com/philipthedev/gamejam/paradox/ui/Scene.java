package com.philipthedev.gamejam.paradox.ui;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * A {@link Scene} can be rendered by the {@link MainFrame}.
 */
public interface Scene {

    /**
     * Render a scene.
     * @param g {@link Graphics2D} to paint
     * @param meta input events and dimensions
     * @param imageObserver {@link ImageObserver} to draw image
     */
    void render(Graphics2D g, Meta meta, ImageObserver imageObserver);

}
