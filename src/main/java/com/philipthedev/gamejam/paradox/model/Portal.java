package com.philipthedev.gamejam.paradox.model;

import com.philipthedev.gamejam.paradox.ui.MainFrame;
import com.philipthedev.gamejam.paradox.ui.Meta;

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
        preview = new BufferedImage(TILE_SIZE * 7, TILE_SIZE * 7, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = preview.createGraphics();
        AffineTransform transform = g.getTransform();
        int previewX = 0;
        int previewY = 0;
        for (int x = fieldX - 2; x < fieldX + 3; x++) {
            for (int y = fieldY - 2; y < fieldY + 3; y++) {
                Field field = model.getFieldOrNull(x, y);
                if (field == null) {
                    previewY += TILE_SIZE;
                    continue;
                }
                g.translate(previewX, previewY);
                field.render(g, new Meta(), MainFrame.get());
                g.setTransform(transform);
                Entity entity = model.getEntityOrNull(x, y);
                if (entity != null) {
                    g.translate(- x*TILE_SIZE, - y * TILE_SIZE);
                    entity.render(g, MainFrame.get());
                    g.setTransform(transform);
                }
                previewY += TILE_SIZE;
            }
            previewX += TILE_SIZE;
            previewY = 0;
        }
        g.dispose();
    }

}
