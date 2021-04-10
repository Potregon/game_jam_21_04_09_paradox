package com.philipthedev.gamejam.paradox.model.actions;

import com.philipthedev.gamejam.paradox.Utils;
import com.philipthedev.gamejam.paradox.model.*;

import java.awt.*;
import java.awt.image.ImageObserver;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class SkipRoundAction implements ActionButton {

    private final PlayerEntity playerEntity;
    private final Image icon = Utils.loadImage(SkipRoundAction.class, "skip.png");

    public SkipRoundAction(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public void selected(Model model) {
        playerEntity.setAttackAction(AttackAction.NEXT_ROUND);
    }

    @Override
    public void deselected(Model model) {

    }

    @Override
    public void render(Graphics2D g, int tileSize, ImageObserver imageObserver) {
        g.drawImage(icon, 0, 0, tileSize, tileSize, 0, 0, 32, 32, imageObserver);
    }
}
