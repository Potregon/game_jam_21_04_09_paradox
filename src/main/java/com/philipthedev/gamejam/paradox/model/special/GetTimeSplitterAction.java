package com.philipthedev.gamejam.paradox.model.special;

import com.philipthedev.gamejam.paradox.model.Entity;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.model.SpecialAction;

import java.awt.*;
import java.awt.image.ImageObserver;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class GetTimeSplitterAction implements SpecialAction {

    private final static int SPEED = 5;

    private final Entity start;
    private final Entity target;
    private final int targetX;
    private final int targetY;

    private int posX;
    private int posY;

    public GetTimeSplitterAction(Entity start, Entity target) {
        this.start = start;
        this.target = target;
        posX = start.getPosX();
        posY = start.getPosY();
        targetX = target.getPosX();
        targetY = target.getPosY();
    }

    @Override
    public void doAction(Model model) {
        if (posX < targetX) {
            posX += SPEED;
            if (posX > targetX) {
                posX = targetX;
            }
        }
        if (posX > targetX) {
            posX -= SPEED;
            if (posX < targetX) {
                posX = targetX;
            }
        }
        if (posY < targetY) {
            posY += SPEED;
            if (posY > targetY) {
                posY = targetY;
            }
        }
        if (posY > targetY) {
            posY -= SPEED;
            if (posY < targetY) {
                posY = targetY;
            }
        }
        if (posX == targetX && posY == targetY) {
            target.setTimeSplitter(target.getTimeSplitter() + 1);
            model.setSpecialAction(null);
        }
    }

    @Override
    public void render(Graphics2D g, ImageObserver imageObserver) {
        g.setColor(Color.ORANGE);
        g.fillOval(posX + TILE_SIZE / 2 - 10, posY, 20, TILE_SIZE);
    }
}
