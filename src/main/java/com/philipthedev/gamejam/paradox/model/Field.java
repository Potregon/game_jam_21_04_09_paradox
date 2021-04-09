package com.philipthedev.gamejam.paradox.model;

import com.philipthedev.gamejam.paradox.ui.Meta;

import java.awt.*;
import java.awt.image.ImageObserver;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class Field {

    private final FieldType type;
    private final int x;
    private final int y;
    private FieldAction fieldAction = null;

    public Field(FieldType fieldType, int x, int y) {
        this.type = fieldType;
        this.x = x;
        this.y = y;
    }

    public void render(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        g.setColor(type.getColor());
        g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        if (fieldAction != null) {
            int mouseX = meta.getMousePosition().x / TILE_SIZE;
            int mouseY = meta.getMousePosition().y / TILE_SIZE;
            if (mouseX == x && mouseY == y) {
                g.setColor(fieldAction.getHighlightColor());
                if (meta.isMouseDown()) {
                    fieldAction.getAction().run();
                }
            }
            else {
                g.setColor(fieldAction.getColor());
            }
            g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        }
    }

    public void setFieldAction(FieldAction fieldAction) {
        this.fieldAction = fieldAction;
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
