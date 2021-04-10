package com.philipthedev.gamejam.paradox.model.foes;

import com.philipthedev.gamejam.paradox.model.AttackAction;
import com.philipthedev.gamejam.paradox.model.Entity;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.model.PlayerEntity;
import com.philipthedev.gamejam.paradox.model.attacks.Hit;
import com.philipthedev.gamejam.paradox.model.pathfinding.Position;
import com.philipthedev.gamejam.paradox.model.pathfinding.Track;
import com.philipthedev.gamejam.paradox.model.special.GetTimeSplitterAction;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Set;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class Trolling extends Entity {

    private int actionPoints = 1;

    public Trolling(int fieldX, int fieldY) {
        super(fieldX, fieldY, 1, 3, 0, 1);
    }

    @Override
    public Track getTrackOrNull(Model model, Set<Track> tracks) {
        Track bestTrack = null;
        int bestDistance = Integer.MAX_VALUE;
        final PlayerEntity playerEntity = model.getPlayerEntity();
        Position playerPosition = new Position(playerEntity.getFieldX(), playerEntity.getFieldY());
        for (var track : tracks) {
            int distance = playerPosition.euclideanDistance(track.getTarget());
            if (distance <= 3 && (bestTrack == null || distance < bestDistance)) {
                bestTrack = track;
                bestDistance = distance;
            }
        }
        if (bestTrack != null) {
            return bestTrack;
        }
        else {
            return new Track(getFieldX(), getFieldY());
        }
    }

    @Override
    public void render(Graphics2D g, ImageObserver observer) {
        if (!isKilled()) {
            g.setColor(Color.RED);
            g.fillRect(getPosX() + 8, getPosY() + 8, TILE_SIZE - 16, TILE_SIZE - 16);
        }
    }

    @Override
    public void killedBy(Entity killer, Model model) {
        if (killer instanceof PlayerEntity) {
            model.setSpecialAction(new GetTimeSplitterAction(this, killer));
        }
    }

    @Override
    public AttackAction getAttackActionOrNull(Model model) {
        if (actionPoints > 0) {
            PlayerEntity playerEntity = model.getPlayerEntity();
            Position position = playerEntity.getFieldPosition();
            if (position.euclideanDistance(getFieldPosition()) <= 1) {
                actionPoints--;
                return new Hit(playerEntity.getFieldPosition());
            }
        }
        actionPoints = 1;
        return AttackAction.NEXT_ROUND;
    }
}
