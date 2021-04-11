package com.philipthedev.gamejam.paradox.model.field;

import com.philipthedev.gamejam.paradox.model.FieldAction;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.ui.Meta;

import java.awt.*;
import java.awt.image.ImageObserver;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public abstract class Field {

    private final int x;
    private final int y;
    private FieldAction fieldAction = null;


    public Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    abstract void renderField(Graphics2D g, Meta meta, ImageObserver imageObserver);

    abstract public boolean isBlocked();

    public void validate(Model model) {

    }

    public void render(Graphics2D g, Meta meta, ImageObserver imageObserver) {
        renderField(g, meta, imageObserver);
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

}
