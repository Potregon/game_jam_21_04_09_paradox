package com.philipthedev.gamejam.paradox.model.foes;

import com.philipthedev.gamejam.paradox.Utils;
import com.philipthedev.gamejam.paradox.model.AttackAction;
import com.philipthedev.gamejam.paradox.model.Entity;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.model.PlayerEntity;
import com.philipthedev.gamejam.paradox.model.attacks.Hit;
import com.philipthedev.gamejam.paradox.model.pathfinding.Position;
import com.philipthedev.gamejam.paradox.model.pathfinding.Track;
import com.philipthedev.gamejam.paradox.model.special.GetTimeSplitterAction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Set;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class ChronoTroll extends Entity {

    private final static BufferedImage image = Utils.loadImage(ChronoTroll.class, "chronotroll.png");
    public final static BufferedImage timeSplitter = Utils.loadImage(ChronoTroll.class, "crystal.png");
    private int actionPoints = 1;

    public ChronoTroll(int fieldX, int fieldY) {
        super(fieldX, fieldY, 1, 3, 0, 1);
    }

    @Override
    public Track getTrackOrNull(Model model, Set<Track> tracks) {
        Track bestTrack = null;
        int bestDistance = Integer.MAX_VALUE;
        final PlayerEntity playerEntity = model.getPlayerEntity();
        if (!playerEntity.isVisible()) {
            return new Track(-1, -1);
        }
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
            return new Track(-1, -1);
        }
    }

    @Override
    public void render(Graphics2D g, ImageObserver observer) {
        if (!isKilled()) {
            int x = getPosX();
            int y = getPosY() - TILE_SIZE / 4;
            g.drawImage(image, x, y, x + TILE_SIZE, y + TILE_SIZE, 0, 0, image.getWidth(), image.getHeight(), observer);
        }
    }

    @Override
    public void killedBy(Entity killer, Model model) {
        if (killer instanceof PlayerEntity) {
            model.setSpecialAction(new GetTimeSplitterAction(this, killer));
        }
        model.foeKilled();
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
            else {
                return AttackAction.SKIP_ROUND;
            }
        }
        actionPoints = 1;
        return AttackAction.NEXT_ROUND;
    }
}
