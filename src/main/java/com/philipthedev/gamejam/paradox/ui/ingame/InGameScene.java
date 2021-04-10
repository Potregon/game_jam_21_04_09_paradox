package com.philipthedev.gamejam.paradox.ui.ingame;

import com.philipthedev.gamejam.paradox.model.*;
import com.philipthedev.gamejam.paradox.ui.Meta;
import com.philipthedev.gamejam.paradox.ui.Scene;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

/**
 * {@link Scene} to show the ingame of a Model.
 */
public class InGameScene implements Scene {

    private ActionButton selectedActionButton = null;
    private int offsetX, offsetY;
    private final Model model;

    public InGameScene(Model model) {
        this.model = model;
    }


    @Override
    public void render(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        PlayerEntity playerEntity = model.getPlayerEntity();
        model.calculateModel();
        if (playerEntity != null) {
            offsetX = playerEntity.getPosX() + TILE_SIZE / 2;
            offsetY = playerEntity.getPosY() + TILE_SIZE / 2;
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
        if (playerEntity != null) {
            playerEntity.render(g, imageObserver);
        }
        for (var entity : model.listEntities()) {
            entity.renderAttack(g, imageObserver);
        }
        if (playerEntity != null) {
            playerEntity.renderAttack(g, imageObserver);
        }
        //special action
        SpecialAction specialAction = model.getSpecialAction();
        if (specialAction != null) {
            specialAction.render(g, imageObserver);
        }
        meta.clear();
        g.setTransform(outerTransform);

        int actionButtonIndex = 0;
        int mouseX = meta.getMousePosition().x;
        int mouseY = meta.getMousePosition().y;
        for (var actionButton : model.listActionButtons()) {
            if (meta.isMouseDown() && mouseX > actionButtonIndex * 74 && mouseX <= actionButtonIndex * 74 + 74 && mouseY > meta.getSize().height - 74 && actionButton != selectedActionButton) {
                if (selectedActionButton != null) {
                    selectedActionButton.deselected(model);
                    selectedActionButton = null;
                }
                selectedActionButton = actionButton;
                selectedActionButton.selected(model);
            }
            g.translate(10 + actionButtonIndex * 74, meta.getSize().height - 74);
            actionButton.render(g, 64, imageObserver);
            g.setTransform(outerTransform);
            actionButtonIndex++;
        }
    }
}
