package com.philipthedev.gamejam.paradox.model;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Field {

    private final FieldType type;

    public Field(FieldType fieldType) {
        this.type = fieldType;
    }

    public void render(Graphics2D g, int tileSize, ImageObserver imageObserver) {
        g.setColor(type.getColor());
        g.fillRect(0, 0, tileSize, tileSize);
    }

    public boolean isBlocked() {
        return type.isBlocked();
    }

    public enum FieldType {
        PASSABLE(Color.WHITE, false),
        SOLID(Color.BLACK, true);

        private final Color color;
        private final boolean blocked;

        FieldType(Color color, boolean blocked) {
            this.color = color;
            this.blocked = blocked;
        }

        public Color getColor() {
            return color;
        }

        public boolean isBlocked() {
            return blocked;
        }
    }

}
