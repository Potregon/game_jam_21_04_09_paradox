package com.philipthedev.gamejam.paradox.ui.ingame;

import com.philipthedev.gamejam.paradox.model.Entity;
import com.philipthedev.gamejam.paradox.model.Field;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.ui.Meta;
import com.philipthedev.gamejam.paradox.ui.Scene;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

/**
 * {@link Scene} to show the ingame of a Model.
 */
public class InGameScene implements Scene{

    private int offsetX, offsetY;
    private final Model model;

    public InGameScene(Model model) {
        this.model = model;
    }


    @Override
    public void render(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        model.calculateModel();
        Entity currentEntity = model.getCurrentEntityOrNull();
        if (currentEntity != null) {
            offsetX = currentEntity.getPosX() + TILE_SIZE / 2;
            offsetY = currentEntity.getPosY() + TILE_SIZE / 2;
        }
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, meta.getSize().width, meta.getSize().height);
        AffineTransform outerTransform = g.getTransform();
        g.translate(-offsetX + meta.getSize().width / 2 , -offsetY + meta.getSize().height / 2);
        meta.translate(-offsetX + meta.getSize().width / 2 , -offsetY + meta.getSize().height / 2);
        AffineTransform transform = g.getTransform();
        for (int x = 0; x < model.getWidth(); x++) {
            for (int y = 0; y < model.getHeight(); y++) {
                Field field = model.getField(x, y);
                g.translate(x * TILE_SIZE, y * TILE_SIZE);
                field.render(g, meta, imageObserver);
                g.setTransform(transform);
            }
        }
        for (var entity : model.listEntities()) {
            entity.render(g, imageObserver);
        }
        meta.clear();
        g.setTransform(outerTransform);
    }
}
