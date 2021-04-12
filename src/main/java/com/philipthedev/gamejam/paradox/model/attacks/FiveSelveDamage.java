package com.philipthedev.gamejam.paradox.model.attacks;

import com.philipthedev.gamejam.paradox.model.AttackAction;
import com.philipthedev.gamejam.paradox.model.DamageType;
import com.philipthedev.gamejam.paradox.model.Entity;
import com.philipthedev.gamejam.paradox.model.Model;

import java.awt.*;
import java.awt.image.ImageObserver;

public class FiveSelveDamage implements AttackAction {

    @Override
    public boolean executeAction(Entity entity, Model model) {
        entity.damage(entity, DamageType.TRIBUTE, 5, model);
        return true;
    }

    @Override
    public void render(Graphics2D g, ImageObserver imageObserver) {

    }
}
