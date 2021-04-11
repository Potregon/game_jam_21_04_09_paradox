package com.philipthedev.gamejam.paradox.model.field;

import com.philipthedev.gamejam.paradox.ui.Meta;

import java.awt.*;
import java.awt.image.ImageObserver;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class WallField extends Field {

    public WallField(int x, int y) {
        super(x, y);
    }

    @Override
    void renderField(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public boolean isBlocked() {
        return true;
    }
}
