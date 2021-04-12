package com.philipthedev.gamejam.paradox.ui;

import com.philipthedev.gamejam.paradox.Utils;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.ui.ingame.InGameScene;
import com.philipthedev.gamejam.paradox.ui.ingame.YouWinScene;

import java.awt.*;
import java.awt.image.ImageObserver;

public class IntroScene implements Scene {


    private static final Image newGame = Utils.loadImage(YouWinScene.class, "new_game.png");

    private int counter = 0;
    String firstLine = "Time is running out, the clock is ticking.";
    String secondLine = "Clear the dungeon before it's to late!";
    String thirdLine = "Collect time splinter to extend your time.";
    String fourthLine = "Open time portals to travel back in time.";

    @Override
    public void render(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, meta.getSize().width, meta.getSize().height);
        g.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        int lineHeight = g.getFontMetrics().getHeight() + 20;
        int dy = lineHeight;

        if (counter < 100) {
            g.setColor(new Color(255, 255, 255, (counter % 100) * 255 / 100));
            int width = g.getFontMetrics().stringWidth(firstLine);
            g.drawString(firstLine, meta.getSize().width / 2 - width / 2, dy);
        }
        else {
            g.setColor(Color.WHITE);
            int width = g.getFontMetrics().stringWidth(firstLine);
            g.drawString(firstLine, meta.getSize().width / 2 - width / 2, dy);
        }
        dy += lineHeight;

        if (counter > 100) {
            if (counter < 200) {
                g.setColor(new Color(255, 255, 255, (counter % 100) * 255 / 100));
                int width = g.getFontMetrics().stringWidth(secondLine);
                g.drawString(secondLine, meta.getSize().width / 2 - width / 2, dy);
            }
            else {
                g.setColor(Color.WHITE);
                int width = g.getFontMetrics().stringWidth(secondLine);
                g.drawString(secondLine, meta.getSize().width / 2 - width / 2, dy);
            }
        }
        dy += lineHeight;

        if (counter > 200) {
            if (counter < 300) {
                g.setColor(new Color(255, 255, 255, (counter % 100) * 255 / 100));
                int width = g.getFontMetrics().stringWidth(thirdLine);
                g.drawString(thirdLine, meta.getSize().width / 2 - width / 2, dy);
            }
            else {
                g.setColor(Color.WHITE);
                int width = g.getFontMetrics().stringWidth(thirdLine);
                g.drawString(thirdLine, meta.getSize().width / 2 - width / 2, dy);
            }
        }
        dy += lineHeight;

        if (counter > 300) {
            if (counter < 400) {
                g.setColor(new Color(255, 255, 255, (counter % 100) * 255 / 100));
                int width = g.getFontMetrics().stringWidth(fourthLine);
                g.drawString(fourthLine, meta.getSize().width / 2 - width / 2, dy);
            }
            else {
                g.setColor(Color.WHITE);
                int width = g.getFontMetrics().stringWidth(fourthLine);
                g.drawString(fourthLine, meta.getSize().width / 2 - width / 2, dy);
            }
        }
        dy += lineHeight;



        if (counter >= 400) {
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
        }
        else {
            counter++;
        }
    }
}
