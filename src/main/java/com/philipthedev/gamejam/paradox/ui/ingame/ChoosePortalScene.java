package com.philipthedev.gamejam.paradox.ui.ingame;

import com.philipthedev.gamejam.paradox.Main;
import com.philipthedev.gamejam.paradox.Utils;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.model.Portal;
import com.philipthedev.gamejam.paradox.ui.MainFrame;
import com.philipthedev.gamejam.paradox.ui.Meta;
import com.philipthedev.gamejam.paradox.ui.Scene;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.function.Function;
import java.util.function.Supplier;

public class ChoosePortalScene implements Scene {


    private static final Image embark = Utils.loadImage(ChoosePortalScene.class, "embark.png");

    private final String label = "Time is up! Choose a portal to travel back.";

    private final Model model;
    private final Function<Model, Scene> sceneSupplier;
    private final List<Portal> portalList = new ArrayList<>();
    private int selectIndex = 0;
    private boolean mouseWasDown;

    public ChoosePortalScene(Model model, Function<Model, Scene> sceneSupplier) {
        this.model = model;
        this.sceneSupplier = sceneSupplier;
        for (var portal : model.listPortals()) {
            if (portal.getRound() <= model.getMaxRound()) {
                portalList.add(portal);
            }
        }
    }

    @Override
    public void render(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, meta.getSize().width, meta.getSize().height);
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        g.drawString(label, meta.getSize().width / 2 - g.getFontMetrics().stringWidth(label) / 2, 20 + g.getFontMetrics().getAscent());
        if (portalList.isEmpty()) {
            MainFrame.get().setScene(new YouLoseScene(model));
            return;
        }

        Portal portal = portalList.get(selectIndex);
        BufferedImage image = portal.getPreview();
        g.drawImage(image, meta.getSize().width / 2 - image.getWidth() / 2, 40 + g.getFontMetrics().getHeight(), imageObserver);

        int buttonBarY = meta.getSize().height - 80;
        int enterButtonX = meta.getSize().width / 2 - 269 / 2;
        int mouseX = meta.getMousePosition().x;
        int mouseY = meta.getMousePosition().y;
        if (mouseX >= enterButtonX && mouseX < enterButtonX + 269 && mouseY >= buttonBarY) {
            g.drawImage(embark, enterButtonX, buttonBarY, enterButtonX + 269, buttonBarY + 56, 0, 56, 269, 112, imageObserver);
            if (meta.isMouseDown()) {
                model.enterPortal(portal);
                model.reset();
                MainFrame.get().setScene(sceneSupplier.apply(model));
            }
        }
        else {
            g.drawImage(embark, enterButtonX, buttonBarY, enterButtonX + 269, buttonBarY + 56, 0, 0, 269, 56, imageObserver);
        }
        if (portalList.size() > 1) {
            if (mouseX < meta.getSize().width / 2) {
                g.setColor(Color.RED);
                g.fillPolygon(new int[]{enterButtonX - 5 - 15, enterButtonX - 5, enterButtonX -5},
                        new int[] {buttonBarY  + 28, buttonBarY, buttonBarY + 56},
                        3);
                if (meta.isMouseDown() && !mouseWasDown) {
                    selectIndex -= 1;
                    if (selectIndex < 0) {
                        selectIndex = portalList.size() -1;
                    }
                }
            }
            else {
                g.setColor(Color.ORANGE);
                g.fillPolygon(new int[]{enterButtonX - 5 - 15, enterButtonX - 5, enterButtonX -5},
                        new int[] {buttonBarY  + 28, buttonBarY, buttonBarY + 56},
                        3);
            }

            if (mouseX >= meta.getSize().width / 2) {
                g.setColor(Color.RED);
                g.fillPolygon(new int[]{enterButtonX + 269 + 5, enterButtonX + 269 + 5, enterButtonX + 269 + 15},
                        new int[] {buttonBarY, buttonBarY + 56, buttonBarY + 28},
                        3);
                if (meta.isMouseDown() && !mouseWasDown) {
                    selectIndex += 1;
                    if (selectIndex >= portalList.size()) {
                        selectIndex = 0;
                    }
                }
            }
            else {
                g.setColor(Color.ORANGE);
                g.fillPolygon(new int[]{enterButtonX + 269 + 5, enterButtonX + 269 + 5, enterButtonX + 269 + 15},
                        new int[] {buttonBarY, buttonBarY + 56, buttonBarY + 28},
                        3);
            }
            mouseWasDown = meta.isMouseDown();
        }
    }
}
