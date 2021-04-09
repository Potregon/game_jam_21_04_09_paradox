package com.philipthedev.gamejam.paradox.model.actions;

import com.philipthedev.gamejam.paradox.model.*;
import com.philipthedev.gamejam.paradox.model.attacks.Hit;

import java.awt.*;
import java.awt.image.ImageObserver;


public class HitAction implements ActionButton {

    private final PlayerEntity provoker;
    boolean selected = false;

    public HitAction(PlayerEntity provoker) {
        this.provoker = provoker;
    }

    @Override
    public void selected(Model model) {
        selected = true;
        int fieldX = provoker.getFieldX();
        int fieldY = provoker.getFieldY();
        apply(model, fieldX - 1, fieldY - 1);
        apply(model, fieldX, fieldY - 1);
        apply(model, fieldX + 1, fieldY - 1);
        apply(model, fieldX + 1, fieldY);
        apply(model, fieldX + 1, fieldY + 1);
        apply(model, fieldX, fieldY + 1);
        apply(model, fieldX - 1, fieldY + 1);
        apply(model, fieldX - 1, fieldY);
    }

    @Override
    public void deselected(Model model) {
        selected = false;
        model.clearActions();
    }

    @Override
    public void render(Graphics2D g, int tileSize, ImageObserver imageObserver) {
        g.setColor(selected ? Color.LIGHT_GRAY : Color.DARK_GRAY);
        g.fillRect(0, 0, tileSize, tileSize);
    }

    private void apply(Model model, int x, int y) {
        Field field = model.getFieldOrNull(x, y);
        if (field == null) {
            return;
        }
        Entity entity = model.getEntityOrNull(x, y);
        if (entity == null) {
            field.setFieldAction(new FieldAction(new Color(250, 200, 0, 50), new Color(250, 200, 0, 250), () -> {}));
        }
        else {
            field.setFieldAction(new FieldAction(new Color(0, 255, 0, 50), new Color(0, 255, 0, 250), () -> {
                provoker.setAttackAction(new Hit(entity));
            }));
        }
    }


}
