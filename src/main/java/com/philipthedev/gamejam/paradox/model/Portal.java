package com.philipthedev.gamejam.paradox.model;

import com.philipthedev.gamejam.paradox.model.pathfinding.Position;
import com.philipthedev.gamejam.paradox.ui.MainFrame;
import com.philipthedev.gamejam.paradox.ui.Meta;
import com.philipthedev.gamejam.paradox.ui.ingame.InGameScene;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

/**
 * Entry portal.
 */
public final class Portal {

    private final int round;
    private final int fieldX;
    private final int fieldY;
    private BufferedImage preview = null;

    public Portal(int fieldX, int fieldY, int round) {
        this.round = round;
        this.fieldX = fieldX;
        this.fieldY = fieldY;
    }

    public int getRound() {
        return round;
    }

    public int getFieldX() {
        return fieldX;
    }

    public int getFieldY() {
        return fieldY;
    }

    public boolean isBlocked(Model model, int fieldX, int fieldY) {
        if (model.getRound() == round || model.getRound() + 1 == round) {
            return this.fieldX == fieldX && this.fieldY == fieldY;
        }
        return false;
    }

    public void render(Graphics2D g, Model model, ImageObserver observer) {
        if (model.getRound() == round) {
            g.setColor(Color.BLUE);
            g.drawOval(fieldX * TILE_SIZE, fieldY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    public void updatePreview(Model model) {
        preview = new BufferedImage(TILE_SIZE * 11, TILE_SIZE * 11, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = preview.createGraphics();
        InGameScene inGameScene = new InGameScene(model, new Position(fieldX * TILE_SIZE, fieldY * TILE_SIZE));
        inGameScene.render(g, new Meta(new Dimension(preview.getWidth(), preview.getHeight())), MainFrame.get());
        g.dispose();
    }

    public BufferedImage getPreview() {
        return preview;
    }
}
