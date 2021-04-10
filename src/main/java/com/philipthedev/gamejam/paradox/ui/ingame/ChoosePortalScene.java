package com.philipthedev.gamejam.paradox.ui.ingame;

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

    private final Model model;
    private final Function<Model, Scene> sceneSupplier;
    private final List<Portal> portalList = new ArrayList<>();
    private int selectIndex = 0;

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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, meta.getSize().width, meta.getSize().height);

        Portal portal = portalList.get(selectIndex);
        BufferedImage image = portal.getPreview();
        g.drawImage(image, meta.getSize().width / 2 - image.getWidth() / 2, 50, imageObserver);

        int buttonBarY = meta.getSize().height - 40;
        int enterButtonX = meta.getSize().width / 2 - 50;
        int mouseX = meta.getMousePosition().x;
        int mouseY = meta.getMousePosition().y;
        if (mouseX >= enterButtonX && mouseX < enterButtonX + 100 && mouseY >= buttonBarY) {
            g.setColor(Color.YELLOW);
            g.fillRect(enterButtonX, buttonBarY, 100, 32);
            if (meta.isMouseDown()) {
                model.enterPortal(portal);
                model.reset();
                MainFrame.get().setScene(sceneSupplier.apply(model));
            }
        }
        else {
            g.setColor(Color.ORANGE);
            g.fillRect(enterButtonX, buttonBarY, 100, 32);
        }
    }
}
