package com.philipthedev.gamejam.paradox.model;

import com.philipthedev.gamejam.paradox.Utils;
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
public final class Portal implements SpecialAction {

    private final static Image image = Utils.loadImage(Portal.class, "portal_attack.png");

    private final int round;
    private final int fieldX;
    private final int fieldY;
    private BufferedImage preview = null;
    private int index = -1;

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
            if (index == -1) {
                model.setSpecialAction(this);
            }
            if (index >= 14) {
                g.drawImage(image,
                        fieldX * TILE_SIZE, fieldY * TILE_SIZE, fieldX * TILE_SIZE + TILE_SIZE, fieldY * TILE_SIZE + TILE_SIZE,
                        0, index * 64, 64, index * 64 + 64,
                        observer);
            }
        }
        else {
            index = -1;
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

    @Override
    public void doAction(Model model) {
        index++;
        if (index >= 14) {
            model.setSpecialAction(null);
        }
    }

    @Override
    public void renderBackground(Graphics2D g, ImageObserver imageObserver) {
        g.drawImage(image,
                fieldX * TILE_SIZE, fieldY * TILE_SIZE, fieldX * TILE_SIZE + TILE_SIZE, fieldY * TILE_SIZE + TILE_SIZE,
                    0, index * 64, 64, index * 64 + 64,
                imageObserver);
    }
}
