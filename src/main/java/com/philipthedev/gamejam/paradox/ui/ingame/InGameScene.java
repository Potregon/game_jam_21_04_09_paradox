package com.philipthedev.gamejam.paradox.ui.ingame;

import com.philipthedev.gamejam.paradox.Utils;
import com.philipthedev.gamejam.paradox.model.*;
import com.philipthedev.gamejam.paradox.model.field.Field;
import com.philipthedev.gamejam.paradox.model.pathfinding.Position;
import com.philipthedev.gamejam.paradox.ui.MainFrame;
import com.philipthedev.gamejam.paradox.ui.Meta;
import com.philipthedev.gamejam.paradox.ui.Scene;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

/**
 * {@link Scene} to show the ingame of a Model.
 */
public class InGameScene implements Scene {

    private static final Image clockIcon = Utils.loadImage(InGameScene.class, "clock.png");
    private static final Image hpBar = Utils.loadImage(InGameScene.class, "HP-Bar.png");

    private ActionButton selectedActionButton = null;
    private int offsetX, offsetY;
    private int cornerX, cornerY;
    private final Model model;
    private final Position positionOrNull;


    public InGameScene(Model model) {
        this(model, null);
    }

    public InGameScene(Model model, Position positionOrNull) {
        this.model = model;
        this.positionOrNull = positionOrNull;
    }


    @Override
    public void render(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        if (model.getRound() > model.getMaxRound()) {
            MainFrame.get().setScene(new ChoosePortalScene(model, InGameScene::new));
            return;
        }
        PlayerEntity playerEntity = model.getPlayerEntity();
        if (positionOrNull == null) {
            model.calculateModel();
            model.calculateLight();
        }
        if (positionOrNull != null) {
            offsetX = positionOrNull.getX() + TILE_SIZE / 2;
            offsetY = positionOrNull.getY() + TILE_SIZE / 2;
        }
        else if (playerEntity != null) {
            offsetX = playerEntity.getPosX() + TILE_SIZE / 2;
            offsetY = playerEntity.getPosY() + TILE_SIZE / 2;
        }
        BufferedImage bufferedImage = new BufferedImage(meta.getSize().width, meta.getSize().height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        renderPlayground(graphics2D, meta, imageObserver, playerEntity);
        graphics2D.dispose();
        ImageProducer ip = new FilteredImageSource(bufferedImage.getSource(), new RGBCap(200, 100, 100));
        g.drawImage(Toolkit.getDefaultToolkit().createImage(ip), 0, 0, imageObserver);

        //timer
        for (int clockIndex = 0; clockIndex < model.getMaxRound(); clockIndex++) {
            int availableRounds = model.getMaxRound() - model.getRound();
            if (clockIndex > availableRounds) {
                g.drawImage(clockIcon, clockIndex * 32, 0, clockIndex * 32 + 32, 32, 32, 0, 64, 32, imageObserver);
            }
            else {
                g.drawImage(clockIcon, clockIndex * 32, 0, clockIndex * 32 + 32, 32, 0, 0, 32, 32,imageObserver);
            }
        }

        //hp
        g.setColor(Color.RED);
        g.fillRect(44, 40, 100, 32);
        int liveWidth = playerEntity.getHP() * 100 / playerEntity.getMaxHP();
        g.setColor(Color.GREEN);
        g.fillRect(44, 40, liveWidth, 32);
        g.drawImage(hpBar, 0, 40, imageObserver);

        int actionButtonIndex = 0;
        int mouseX = meta.getMousePosition().x;
        int mouseY = meta.getMousePosition().y;
        for (var actionButton : model.listActionButtons()) {
            boolean hovered = false;
            if (mouseX > actionButtonIndex * 74 && mouseX <= actionButtonIndex * 74 + 74 && mouseY > meta.getSize().height - 74 && actionButton != selectedActionButton) {
                hovered = true;
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
            actionButtonIndex++;
        }
    }

    private void renderPlayground(Graphics2D g, Meta meta, ImageObserver imageObserver, PlayerEntity playerEntity) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, meta.getSize().width, meta.getSize().height);
        AffineTransform outerTransform = g.getTransform();
        cornerX = offsetX - meta.getSize().width/ 2;
        cornerY = offsetY - meta.getSize().height / 2;
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
        // portals
        for (var portal : model.listPortals()) {
            portal.render(g, model, imageObserver);
        }
        //special action
        SpecialAction specialAction = model.getSpecialAction();
        if (specialAction != null) {
            specialAction.render(g, imageObserver);
        }
        meta.clear();
        g.setTransform(outerTransform);
    }

    private class RGBCap extends RGBImageFilter {

        private final int maxRed;
        private final int maxGreen;
        private final int maxBlue;

        public RGBCap(int maxRed, int maxGreen, int maxBlue) {
            this.maxRed = maxRed;
            this.maxGreen = maxGreen;
            this.maxBlue = maxBlue;
        }

        @Override
        public int filterRGB(int x, int y, int rgb) {
            int fieldX = (x + cornerX) / TILE_SIZE;
            int fieldY = (y + cornerY) / TILE_SIZE;
            final Field field = model.getFieldOrNull(fieldX, fieldY);
            if (field == null) {
                return rgb;
            }

            int a = (rgb>>24)&0xff;
            int r = (rgb>>16)&0xff;
            int g = (rgb>>8)&0xff;
            int b = rgb&0xff;

            int redLight = field.getRedLight();
            int greenLight = field.getGreenLight();
            int blueLight = field.getBlueLight();

            r = Math.min(255, redLight == 0 ? 0 : (r * redLight) / 100);
            g = Math.min(255, greenLight == 0 ? 0 : (g * greenLight) / 100);
            b = Math.min(255, blueLight == 0 ? 0 : (b * blueLight) / 100);

            //set new RGB
            int result = (a<<24) | (r<<16) | (g<<8) | b;
            return result;
        }
    }
}
