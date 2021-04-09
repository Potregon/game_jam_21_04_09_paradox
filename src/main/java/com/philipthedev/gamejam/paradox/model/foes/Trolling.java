package com.philipthedev.gamejam.paradox.model.foes;

import com.philipthedev.gamejam.paradox.model.Entity;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.model.pathfinding.Track;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Set;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class Trolling extends Entity {

    public Trolling(int fieldX, int fieldY) {
        super(fieldX, fieldY);
    }

    @Override
    public Track getTrackOrNull(Model model, Set<Track> tracks) {
        for (var track : tracks) {
            return track;
        }
        return new Track(getFieldX(), getFieldY());
    }

    @Override
    public void render(Graphics2D g, ImageObserver observer) {
        g.setColor(Color.RED);
        g.fillRect(getPosX() + 8, getPosY() + 8, TILE_SIZE - 16, TILE_SIZE - 16);
    }
}
