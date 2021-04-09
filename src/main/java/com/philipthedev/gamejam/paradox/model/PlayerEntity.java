package com.philipthedev.gamejam.paradox.model;

import com.philipthedev.gamejam.paradox.model.pathfinding.Track;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class PlayerEntity extends Entity {

    Random random = new Random();

    private Track selectedTrack = null;

    public PlayerEntity(int fieldX, int fieldY) {
        super(fieldX, fieldY);
    }

    @Override
    Track getTrackOrNull(Model model, Set<Track> tracks) {
        if (selectedTrack != null) {
            Track result = selectedTrack;
            selectedTrack = null;
            return result;
        }
        for (var track : tracks) {
            Field field = model.getField(track.getTarget().getX(), track.getTarget().getY());
            field.setAction(new Action(new Color(0, 255, 0, 50), new Color(0, 255, 0, 250),
                    () -> {
                        selectedTrack = track;
                        model.clearActions();
                    }));
        }
        return null;
    }

    @Override
    public void render(Graphics2D g, ImageObserver observer) {
        g.setColor(Color.YELLOW);
        g.fillOval(getPosX(), getPosY(), TILE_SIZE, TILE_SIZE);
    }
}
