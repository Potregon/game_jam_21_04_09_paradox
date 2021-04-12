package com.philipthedev.gamejam.paradox.ui.ingame;

import com.philipthedev.gamejam.paradox.Utils;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.model.pathfinding.Position;
import com.philipthedev.gamejam.paradox.ui.MainFrame;
import com.philipthedev.gamejam.paradox.ui.Meta;
import com.philipthedev.gamejam.paradox.ui.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class YouLoseScene implements Scene {

    private static final Image image = Utils.loadImage(YouLoseScene.class, "YOU LOSE.png");
    private static final Image newGame = Utils.loadImage(YouWinScene.class, "new_game.png");
    private final Model model;
    private BufferedImage background;
    private int round = 0;

    public YouLoseScene(Model model) {
        this.model = model;
    }

    @Override
    public void render(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        if (round == 0) {
            background = new BufferedImage(meta.getSize().width, meta.getSize().height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D bg = background.createGraphics();
            InGameScene inGameScene = new InGameScene(model, new Position(model.getPlayerEntity().getPosX(), model.getPlayerEntity().getPosY()));
            inGameScene.render(bg, new Meta(new Dimension(background.getWidth(), background.getHeight())), imageObserver);
            bg.dispose();
        }
        g.drawImage(background, 0, 0, imageObserver);
        g.setColor(new Color(255, 255, 255, round * 255 / 51));
        g.fillRect(0, 0, meta.getSize().width, meta.getSize().height);

        int buttonBarY = meta.getSize().height - 80;
        int enterButtonX = meta.getSize().width / 2 - 356 / 2;
        int mouseX = meta.getMousePosition().x;
        int mouseY = meta.getMousePosition().y;
        if (mouseX >= enterButtonX && mouseX < enterButtonX + 356 && mouseY >= buttonBarY) {
            g.drawImage(newGame, enterButtonX, buttonBarY, enterButtonX + 356, buttonBarY + 56, 0, 56, 356, 112, imageObserver);
            if (meta.isMouseDown()) {
                Model model = new Model();
                MainFrame.get().setScene(new InGameScene(model));
            }
        }
        else {
            g.drawImage(newGame, enterButtonX, buttonBarY, enterButtonX + 356, buttonBarY + 56, 0, 0, 356, 56, imageObserver);
        }

        if (round > 0) {
            int targetY = meta.getSize().height / 2 - 27;
            int x = meta.getSize().width / 2 - 162;
            int y = round == 50 ? targetY : (targetY * round / 50);
            g.drawImage(image, x, y, imageObserver);
        }
        if (round < 51) {
            round++;
        }
    }
}
