package com.philipthedev.gamejam.paradox.ui.ingame;

import com.philipthedev.gamejam.paradox.Utils;
import com.philipthedev.gamejam.paradox.model.*;
import com.philipthedev.gamejam.paradox.model.field.Field;
import com.philipthedev.gamejam.paradox.model.pathfinding.Position;
import com.philipthedev.gamejam.paradox.ui.MainFrame;
import com.philipthedev.gamejam.paradox.ui.Meta;
import com.philipthedev.gamejam.paradox.ui.Scene;

import javax.print.DocFlavor;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

/**
 * {@link Scene} to show the ingame of a Model.
 */
public class InGameScene implements Scene {

    private static final Image clockIcon = Utils.loadImage(InGameScene.class, "clock.png");
    private static final BufferedImage hpBar = Utils.loadImage(InGameScene.class, "HP-Bar.png");

    private ActionButton selectedActionButton = null;
    private int offsetX, offsetY;
    private final Model model;
    private final Position positionOrNull;
    private String tooltip = null;


    public InGameScene(Model model) {
        this(model, null);
    }

    public InGameScene(Model model, Position positionOrNull) {
        this.model = model;
        this.positionOrNull = positionOrNull;
    }


    @Override
    public void render(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        tooltip = null;
        PlayerEntity playerEntity = model.getPlayerEntity();
        if (positionOrNull == null) {
            if (model.getRound() > model.getMaxRound()) {
                MainFrame.get().setScene(new ChoosePortalScene(model, InGameScene::new));
                return;
            }
            model.calculateModel();
        }
        if (positionOrNull != null) {
            offsetX = positionOrNull.getX() + TILE_SIZE / 2;
            offsetY = positionOrNull.getY() + TILE_SIZE / 2;
        }
        else if (playerEntity != null) {
            if (playerEntity.getHP() <= 0) {
                MainFrame.get().setScene(new YouLoseScene(model));
            }
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
        //special action
        SpecialAction specialAction = model.getSpecialAction();
        if (specialAction != null) {
            specialAction.renderBackground(g, imageObserver);
        }
        // portals
        for (var portal : model.listPortals()) {
            portal.render(g, model, imageObserver);
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
        specialAction = model.getSpecialAction();
        if (specialAction != null) {
            specialAction.renderForeground(g, imageObserver);
        }
        meta.clear();
        g.setTransform(outerTransform);

        //timer
            int availableRounds = model.getMaxRound() - model.getRound();
        for (int clockIndex = 0; clockIndex < model.getMaxRound(); clockIndex++) {
            if (clockIndex > availableRounds) {
                g.drawImage(clockIcon, clockIndex * 32, 0, clockIndex * 32 + 32, 32, 32, 0, 64, 32, imageObserver);
            }
            else {
                g.drawImage(clockIcon, clockIndex * 32, 0, clockIndex * 32 + 32, 32, 0, 0, 32, 32,imageObserver);
            }
        }
        if (meta.getMousePosition().y < 40 && meta.getMousePosition().x < model.getMaxRound() * 32 ) {
            tooltip = (availableRounds + 1) + " Round(s) remaining. " + model.listPortals().size() + " portals available to travel back in Time.";
        }

        //hp
        g.setColor(Color.RED);
        g.fillRect(44, 40, 100, 32);
        int liveWidth = playerEntity.getHP() * 100 / playerEntity.getMaxHP();
        g.setColor(Color.GREEN);
        g.fillRect(44, 40, liveWidth, 32);
        g.drawImage(hpBar, 0, 40, imageObserver);

        if (meta.getMousePosition().y > 40 && meta.getMousePosition().y < 80  && meta.getMousePosition().x < hpBar.getWidth()) {
            tooltip = playerEntity.getHP() + " of " + playerEntity.getMaxHP() + " life points remaining. Use it sparingly.";
        }

        // action buttons
        int actionButtonIndex = 0;
        int mouseX = meta.getMousePosition().x;
        int mouseY = meta.getMousePosition().y;
        for (var actionButton : model.listActionButtons()) {
            boolean hovered = false;
            if (mouseX > actionButtonIndex * 74 && mouseX <= actionButtonIndex * 74 + 74 && mouseY > meta.getSize().height - 74 && actionButton != selectedActionButton) {
                hovered = true;
                tooltip = actionButton.tooltip();
                if (meta.isMouseDown()) {
                    if (selectedActionButton != null) {
                        selectedActionButton.deselected(model);
                        selectedActionButton = null;
                    }
                    selectedActionButton = actionButton;
                    selectedActionButton.selected(model);
                }
            }
            g.translate(10 + actionButtonIndex * 74, meta.getSize().height - 74);
            actionButton.render(g, 64, hovered, imageObserver);
            g.setTransform(outerTransform);
            actionButtonIndex++;
        }

        if (tooltip != null && positionOrNull == null) {
            ArrayList<String> lines = new ArrayList<>();
            FontMetrics fontMetrics = g.getFontMetrics();
            int maxLineWidth = 0;
            String line = "";
            for (var word : tooltip.split(" ")) {
                int width = fontMetrics.stringWidth(line + ' ' + word);
                if (width > 300) {
                    lines.add(line);
                    line = "";
                } else {
                    line += ' ' + word;
                    if (width > maxLineWidth) {
                        maxLineWidth = width;
                    }
                }
            }
            if (!line.isBlank()) {
                lines.add(line);
            }
            int tooltipWidth = maxLineWidth + 10;
            int tooltipHeight = lines.size() * fontMetrics.getHeight() + 10;
            int tooltipX = meta.getMousePosition().x + 5;
            int tooltipY = 0;
            AffineTransform affineTransform = g.getTransform();
            if (meta.getMousePosition().getY() > meta.getSize().getHeight() / 2) {
                tooltipY = meta.getMousePosition().y - tooltipHeight;
            } else {
                tooltipY = meta.getMousePosition().y + 5;
            }
            g.translate(tooltipX, tooltipY);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, tooltipWidth, tooltipHeight);
            g.setColor(Color.WHITE);
            g.translate(5, 5 + fontMetrics.getAscent());
            for (var currentLine : lines) {
                g.drawString(currentLine, 0, 0);
                g.translate(0, fontMetrics.getHeight());
            }
            g.setTransform(affineTransform);
        }
    }
}
