package com.philipthedev.gamejam.paradox.model.field;

import com.philipthedev.gamejam.paradox.Utils;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.ui.Meta;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class PassableField extends Field {

    private static final TileMap mapImage = new TileMap(Utils.loadImage(PassableField.class, "ground2.png"));
    private static final BufferedImage backgroundImage = Utils.loadImage(PassableField.class, "ground_background.png");
    private final int x;
    private final int y;
    private BufferedImage image;

    public PassableField(int x, int y) {
        super(x, y);
        this.x = x;
        this.y = y;
    }

    @Override
    public void validate(Model model) {
        boolean northWest = model.getFieldOrNull(x - 1, y - 1) instanceof PassableField;
        boolean north = model.getFieldOrNull(x, y - 1) instanceof PassableField;
        boolean northEast = model.getFieldOrNull(x + 1, y - 1) instanceof PassableField;
        boolean east = model.getFieldOrNull(x + 1, y ) instanceof PassableField;
        boolean southEast = model.getFieldOrNull(x + 1, y + 1) instanceof PassableField;
        boolean south = model.getFieldOrNull(x, y + 1) instanceof PassableField;
        boolean southWest = model.getFieldOrNull(x - 1, y + 1) instanceof PassableField;
        boolean west = model.getFieldOrNull(x - 1, y) instanceof PassableField;
        image = mapImage.getTile(northWest, north, northEast, east, southEast, south, southWest, west);
    }

    @Override
    void renderField(Graphics2D g, Meta meta, ImageObserver imageObserver) {

        g.drawImage(backgroundImage, 0, 0, TILE_SIZE, TILE_SIZE, 0, 0, backgroundImage.getWidth(), backgroundImage.getHeight(), imageObserver);
        g.drawImage(image, 0, 0, TILE_SIZE, TILE_SIZE, 0, 0, image.getWidth(), image.getHeight(), imageObserver);
    }

    @Override
    public boolean isBlocked() {
        return false;
    }
}
