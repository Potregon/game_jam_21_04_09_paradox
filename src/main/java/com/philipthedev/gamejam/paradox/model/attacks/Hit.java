package com.philipthedev.gamejam.paradox.model.attacks;

import com.philipthedev.gamejam.paradox.model.AttackAction;
import com.philipthedev.gamejam.paradox.model.DamageType;
import com.philipthedev.gamejam.paradox.model.Entity;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.model.pathfinding.Position;

import java.awt.*;
import java.awt.image.ImageObserver;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class Hit implements AttackAction {

    private final Position targetPosition;
    private int step = 0;

    public Hit(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    @Override
    public boolean executeAction(Entity entity, Model model) {
        step++;
        if (step > 5) {
            step = 0;
            Entity target = model.getEntityOrNull(targetPosition.getX(), targetPosition.getY());
            if (target != null) {
                target.damage(entity, DamageType.MUNDANE, 1, model);
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void render(Graphics2D g, ImageObserver imageObserver) {
        if (step != 0) {
            g.setColor(Color.WHITE);
            g.fillOval(targetPosition.getX() * TILE_SIZE + TILE_SIZE  / 2 - 10, targetPosition.getY() * TILE_SIZE + TILE_SIZE / 2 - 10, 20, 20);
        }
    }
}
