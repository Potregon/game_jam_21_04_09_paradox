package com.philipthedev.gamejam.paradox.model;

import com.philipthedev.gamejam.paradox.model.pathfinding.Track;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Set;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class GhostPlayerEntity extends Entity {

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
    public void render(Graphics2D g, ImageObserver observer) {
        if (isVisible()) {
            g.setColor(Color.YELLOW);
            g.fillOval(getPosX(), getPosY(), TILE_SIZE, TILE_SIZE);
        }
    }

    @Override
    public void killedBy(Entity killer, Model model) {

    }
}
