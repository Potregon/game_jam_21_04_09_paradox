package com.philipthedev.gamejam.paradox.model;

import com.philipthedev.gamejam.paradox.ui.Meta;

import java.awt.*;
import java.awt.image.ImageObserver;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class Field {

    private final FieldType type;
    private final int x;
    private final int y;
    private Action action = null;

    public Field(FieldType fieldType, int x, int y) {
        this.type = fieldType;
        this.x = x;
        this.y = y;
    }

    public void render(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        g.setColor(type.getColor());
        g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        if (action != null) {
            int mouseX = meta.getMousePosition().x / TILE_SIZE;
            int mouseY = meta.getMousePosition().y / TILE_SIZE;
            if (mouseX == x && mouseY == y) {
                g.setColor(action.getHighlightColor());
                if (meta.isMouseDown()) {
                    action.getAction().run();
                }
            }
            else {
                g.setColor(action.getColor());
            }
            g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        }
    }

    public void setAction(Action action) {
        this.action = action;
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
