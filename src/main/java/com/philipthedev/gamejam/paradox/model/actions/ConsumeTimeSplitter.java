package com.philipthedev.gamejam.paradox.model.actions;

import com.philipthedev.gamejam.paradox.Utils;
import com.philipthedev.gamejam.paradox.model.ActionButton;
import com.philipthedev.gamejam.paradox.model.AttackAction;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.model.PlayerEntity;
import com.philipthedev.gamejam.paradox.ui.ingame.ChoosePortalScene;

import java.awt.*;
import java.awt.image.ImageObserver;

public class ConsumeTimeSplitter implements ActionButton {

    private final PlayerEntity playerEntity;
    private final static Image icon = Utils.loadImage(ConsumeTimeSplitter.class, "timesplitter.png");

    public ConsumeTimeSplitter(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public void selected(Model model) {
        final int timeSplitter = playerEntity.getTimeSplitter();
        if (timeSplitter > 0) {
            playerEntity.setTimeSplitter(timeSplitter - 1);
            model.setMaxRound(model.getMaxRound() + 1);
            playerEntity.setAttackAction(AttackAction.NEXT_ROUND);
        }
    }

    @Override
    public void deselected(Model model) {

    }

    @Override
    public void render(Graphics2D g, int tileSize, boolean hovered, ImageObserver imageObserver) {
        if (hovered) {
            g.drawImage(icon, 0, 0, tileSize, tileSize, 32, 0, 64, 32, imageObserver);
        }
        else {
            g.drawImage(icon, 0, 0, tileSize, tileSize, 0, 0, 32, 32, imageObserver);
        }
    }

    @Override
    public String tooltip() {
        return "Consumes a time splinter to gain extra rounds. You have currently " + playerEntity.getTimeSplitter() + " time splinter.";
    }
}
