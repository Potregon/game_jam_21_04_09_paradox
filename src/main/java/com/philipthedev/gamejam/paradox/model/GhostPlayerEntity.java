package com.philipthedev.gamejam.paradox.model;

import com.philipthedev.gamejam.paradox.Utils;
import com.philipthedev.gamejam.paradox.model.pathfinding.Track;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Set;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class GhostPlayerEntity extends Entity {

    private static final BufferedImage playerImage = Utils.loadImage(PlayerEntity.class, "ghostPlayer.png");

    public GhostPlayerEntity(PlayerEntity origin) {
        super(origin);
    }

    @Override
    public Track getTrackOrNull(Model model, Set<Track> tracks) {
        dispose();
        return null;
    }

    @Override
    public AttackAction getAttackActionOrNull(Model model) {
        dispose();
        return AttackAction.NEXT_ROUND;
    }

    @Override
    public void damage(Entity aggressor, DamageType damageType, int damage, Model model) {
        model.getPlayerEntity().delegateDamage(damage);
    }

    @Override
    public void render(Graphics2D g, ImageObserver observer) {
        if (isVisible()) {
            g.drawImage(playerImage, getPosX(), getPosY(), getPosX() + TILE_SIZE, getPosY() + TILE_SIZE, 0, 0, playerImage.getWidth(), playerImage.getHeight(), observer);
        }
    }

    @Override
    public void killedBy(Entity killer, Model model) {

    }
}
