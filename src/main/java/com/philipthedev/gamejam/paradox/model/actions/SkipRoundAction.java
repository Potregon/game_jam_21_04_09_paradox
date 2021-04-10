package com.philipthedev.gamejam.paradox.model.actions;

import com.philipthedev.gamejam.paradox.model.AttackAction;
import com.philipthedev.gamejam.paradox.model.Entity;
import com.philipthedev.gamejam.paradox.model.Model;

import java.awt.*;
import java.awt.image.ImageObserver;

public class SkipRoundAction implements AttackAction {

    @Override
    public boolean executeAction(Entity entity, Model model) {
        return false;
    }

    @Override
    public void render(Graphics2D g, ImageObserver imageObserver) {

    }
}
