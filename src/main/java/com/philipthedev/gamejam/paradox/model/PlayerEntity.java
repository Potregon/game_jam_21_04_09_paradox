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

    public PlayerEntity(int fieldX, int fieldY) {
        super(fieldX, fieldY);
    }

    @Override
    Track getTrackOrNull(Set<Track> tracks) {
        ArrayList<Track> trackList = new ArrayList<>(tracks);
        return trackList.get(random.nextInt(trackList.size()));
    }

    @Override
    public void render(Graphics2D g, ImageObserver observer) {
        g.setColor(Color.YELLOW);
        g.fillOval(getPosX(), getPosY(), TILE_SIZE, TILE_SIZE);
    }
}
