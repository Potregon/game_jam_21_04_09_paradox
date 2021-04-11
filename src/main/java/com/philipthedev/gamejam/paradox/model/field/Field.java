package com.philipthedev.gamejam.paradox.model.field;

import com.philipthedev.gamejam.paradox.model.FieldAction;
import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.model.pathfinding.Pathfinder;
import com.philipthedev.gamejam.paradox.model.pathfinding.Position;
import com.philipthedev.gamejam.paradox.model.pathfinding.Track;
import com.philipthedev.gamejam.paradox.ui.Meta;

import java.awt.*;
import java.awt.image.*;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public abstract class Field {

    private final int x;
    private final int y;
    private FieldAction fieldAction = null;
    private int redSource, greenSource, blueSource;
    private int redLight, greenLight, blueLight;
    private Set<LightFlow> lightFlows = new HashSet<>();
    private Set<Field> brightenFields = new HashSet<>();
    private BufferedImage ghostField = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    private Image filteredGhostField = null;


    public Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    abstract void renderField(Graphics2D g, Meta meta, ImageObserver imageObserver);

    abstract public boolean isBlocked();

    public void validate(Model model) {
        final Set<Track> paths = Pathfinder.findPath(model, x, y, 20);
        for (var track : paths) {
            Position position = track.getTarget();
            final Field field = model.getField(position.getX(), position.getY());
            field.lightFlows.add(new LightFlow(this, track.getSteps()));
            brightenFields.add(field);
        }
    }

    public int getRedSource() {
        return redSource;
    }

    public int getGreenSource() {
        return greenSource;
    }

    public int getBlueSource() {
        return blueSource;
    }

    public int getRedLight() {
        return redLight;
    }

    public int getGreenLight() {
        return greenLight;
    }

    public int getBlueLight() {
        return blueLight;
    }

    public void setLightSource(int red, int green, int blue) {
        if (red != redSource || green != greenSource || blue != blueSource) {
            redSource = red;
            greenSource = green;
            blueSource = blue;
            for (var brightenField : brightenFields) {
                brightenField.revalidateLight();
            }
        }
    }

    private void revalidateLight() {
        redLight = 0;
        greenLight = 0;
        blueLight = 0;
        for (var lightFlow : lightFlows) {
            int red = lightFlow.getRed();
            int green = lightFlow.getGreen();
            int blue = lightFlow.getBlue();
            if (redLight < red) {
                redLight = red;
            }
            if (greenLight < green) {
                greenLight = green;
            }
            if (blueLight < blue) {
                blueLight = blue;
            }
        }
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
