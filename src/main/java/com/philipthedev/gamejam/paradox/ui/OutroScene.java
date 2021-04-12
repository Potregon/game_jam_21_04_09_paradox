package com.philipthedev.gamejam.paradox.ui;

import com.philipthedev.gamejam.paradox.Utils;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.ui.ingame.InGameScene;
import com.philipthedev.gamejam.paradox.ui.ingame.YouWinScene;

import java.awt.*;
import java.awt.image.ImageObserver;

public class OutroScene implements Scene {



        private int counter = 0;
        String firstLine = "Thank you for playing :D";
        String secondLine = "~ PhilipTheDev";

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
                    g.drawString(secondLine, meta.getSize().width / 2, dy);
                }
                else {
                    g.setColor(Color.WHITE);
                    int width = g.getFontMetrics().stringWidth(secondLine);
                    g.drawString(secondLine, meta.getSize().width / 2, dy);
                }
            }
            dy += lineHeight;

            if (counter >= 200) {
                System.exit(0);
            }
            else {
                counter++;
            }
        }

}
