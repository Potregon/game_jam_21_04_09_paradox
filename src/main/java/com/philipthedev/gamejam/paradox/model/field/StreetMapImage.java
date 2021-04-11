package com.philipthedev.gamejam.paradox.model.field;

import com.philipthedev.gamejam.paradox.ui.MainFrame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StreetMapImage {

    private BufferedImage[] tiles = new BufferedImage[16];

    public StreetMapImage(Image rawImage) {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                BufferedImage bufferedImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = bufferedImage.createGraphics();
                g.drawImage(rawImage, 0, 0, 64, 64, x * 64, y * 64, x * 64 + 64, y * 64 + 64, MainFrame.get());
                g.dispose();
                tiles[x * 4 + y] = bufferedImage;
            }
        }
    }

    public BufferedImage getTile (boolean northWest, boolean north, boolean northEast, boolean east, boolean southEast, boolean south, boolean southWest, boolean west) {
        if (north && east && south && west) {
            return tiles[2];
        }
        else if (north && east && south && !west) {
            return tiles[6];
        }
        else if (north && east && !south && west) {
            return tiles[10];
        }
        else if (north && east && !south && !west) {
            return tiles[7];
        }
        else if (north && !east && south && west) {
            return tiles[9];
        }
        else if (north && !east && south && !west) {
            return tiles[13];
        }
        else if (north && !east && !south && west) {
            return tiles[14];
        }
        else if (north && !east && !south && !west) {
            return tiles[3];
        }
        else if (!north && east && south && west) {
            return tiles[5];
        }
        else if (!north && east && south && !west) {
            return tiles[4];
        }
        else if (!north && east && !south && west) {
            return tiles[1];
        }
        else if (!north && east && !south && !west) {
            return tiles[0];
        }
        else if (!north && !east && south && west) {
            return tiles[8];
        }
        else if (!north && !east && south && !west) {
            return tiles[12];
        }
        else if (!north && !east && !south && west) {
            return tiles[11];
        }
        else if (!north && !east && !south && !west) {
            return tiles[15];
        }
        return null;
    }

}
