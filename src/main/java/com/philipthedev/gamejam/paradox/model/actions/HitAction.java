package com.philipthedev.gamejam.paradox.model.actions;

import com.philipthedev.gamejam.paradox.Utils;
import com.philipthedev.gamejam.paradox.model.*;
import com.philipthedev.gamejam.paradox.model.attacks.Hit;
import com.philipthedev.gamejam.paradox.model.field.Field;

import java.awt.*;
import java.awt.image.ImageObserver;


public class HitAction implements ActionButton {

    private final PlayerEntity provoker;
    boolean selected = false;
    private static final Image icon = Utils.loadImage(HitAction.class, "hit_action.png");

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
    public void render(Graphics2D g, int tileSize, boolean hovered, ImageObserver imageObserver) {
        if (selected || hovered) {
            g.drawImage(icon, 0, 0, tileSize, tileSize, 32, 0, 64, 32, imageObserver);
        }
        else {
            g.drawImage(icon, 0, 0, tileSize, tileSize, 0, 0, 32, 32, imageObserver);
        }
    }

    @Override
    public String tooltip() {
        return "Deals 1 mundane damage to a foe.";
    }

    private void apply(Model model, int x, int y) {
        Field field = model.getFieldOrNull(x, y);
        if (field == null) {
            return;
        }
        Entity entity = model.getEntityOrNull(x, y);
        if (entity == null) {
            if (!field.isBlocked()) {
                field.setFieldAction(new FieldAction(new Color(250, 200, 0, 50), new Color(250, 200, 0, 250), () -> {}));
            }
        }
        else {
            field.setFieldAction(new FieldAction(new Color(0, 255, 0, 50), new Color(0, 255, 0, 250), () -> {
                provoker.setAttackAction(new Hit(entity.getFieldPosition()));
            }));
        }
    }


}
