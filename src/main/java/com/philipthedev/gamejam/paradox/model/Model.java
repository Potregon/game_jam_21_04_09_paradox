package com.philipthedev.gamejam.paradox.model;

import com.philipthedev.gamejam.paradox.model.foes.Trolling;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Model {

    public static final int TILE_SIZE = 64;

    private final Field[][] fields;
    private final int width;
    private final int height;

    private final List<Entity> entities = new ArrayList<>();
    private PlayerEntity playerEntity = null;
    private Entity currentEntity = null;
    private int round = 1;
    private int maxRound = 3;

    private final List<ActionButton> actionButtons = new ArrayList<>();
    private SpecialAction specialAction = null;

    private Portal startPortal = null;
    private final List<Portal> portals = new ArrayList<>();

    public Model() {
        //load fields
        String map = loadMap();
        String[] rows = map.split("\n");
        int rowAmount = rows.length;
        int columnAmount = rows[0].length();
        this.fields = new Field[columnAmount][rowAmount];
        this.width = columnAmount;
        this.height = rowAmount;
        for (int x = 0; x < columnAmount; x++) {
            for (int y = 0; y < rowAmount; y++) {
                String row = rows[y];
                char cell = row.charAt(x);
                switch (cell) {
                    case 'W':
                        fields[x][y] = new Field(Field.FieldType.SOLID, x, y);
                        break;
                    case 'p':
                        fields[x][y] = new Field(Field.FieldType.PASSABLE, x, y);
                        playerEntity = new PlayerEntity(x, y);
                        break;
                    case 't':
                        fields[x][y] = new Field(Field.FieldType.PASSABLE, x, y);
                        entities.add(new Trolling(x, y));
                        break;
                    default:
                        fields[x][y] = new Field(Field.FieldType.PASSABLE, x, y);
                        break;
                }
            }
        }
        startPortal = new Portal(playerEntity.getFieldX(), playerEntity.getFieldY(), 2);
    }

    public boolean isBlocked(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return true;
        }
        if (fields[x][y].isBlocked()) {
            return true;
        }
        for (var entity : entities) {
            if (entity.isBlockingField(x, y)) {
                return true;
            }
        }
        if (playerEntity != null && playerEntity.isBlockingField(x, y)) {
            return true;
        }
        for (var portal : portals) {
            if (portal.isBlocked(this, x, y)) {
                return true;
            }
        }
        if (startPortal.isBlocked(this, x, y)) {
            return true;
        }
        return false;
    }

    public List<Entity> listEntities() {
        return Collections.unmodifiableList(entities);
    }

    public Entity getCurrentEntityOrNull() {
        return currentEntity;
    }

    public void setSpecialAction(SpecialAction specialAction) {
        this.specialAction = specialAction;
    }

    public SpecialAction getSpecialAction() {
        return specialAction;
    }

    public void calculateModel() {
        if (specialAction != null) {
            specialAction.doAction(this);
            if (specialAction != null) {
                return;
            }
        }
        if (entities.isEmpty()) {
            return;
        }
        if (currentEntity == null) {
            currentEntity = entities.get(0);
        }
        while (currentEntity != null) {
            Entity.RoundState roundState = currentEntity.calculateRound(this);
            if (roundState == Entity.RoundState.PENDING) {
                return;
            }
            else {
                if (currentEntity == playerEntity) {
                    currentEntity = null;
                    nextRound();
                }
                else {
                    int nextIndex = entities.indexOf(currentEntity) + 1;
                    if (nextIndex >= entities.size()) {
                        currentEntity = playerEntity;
                    }
                    else {
                        currentEntity = entities.get(nextIndex);
                    }
                }
            }
        }
    }

    public void addPortal(Portal portal) {
        synchronized (portals) {
            this.portals.add(portal);
        }
    }

    public List<Portal> listPortals() {
        synchronized (portals) {
            List<Portal> portals = new ArrayList<>(this.portals);
            portals.add(startPortal);
            return portals;
        }
    }


    public void clearActions() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                fields[x][y].setFieldAction(null);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Field getField(int x, int y) {
        return this.fields[x][y];
    }

    public Field getFieldOrNull(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        else {
            return fields[x][y];
        }
    }

    public int getRound() {
        return round;
    }

    public void addActionButton(ActionButton actionButton) {
        synchronized (this.actionButtons) {
            actionButtons.add(actionButton);
        }
    }

    public void clearActionButtons() {
        synchronized (this.actionButtons) {
            actionButtons.clear();
        }
    }

    public List<ActionButton> listActionButtons() {
        synchronized (this.actionButtons) {
            return Collections.unmodifiableList(actionButtons);
        }
    }

    public int getMaxRound() {
        return maxRound;
    }

    public void setMaxRound(int maxRound) {
        this.maxRound = maxRound;
    }

    private void nextRound() {
        //update portal preview
        if (startPortal.getRound() == round) {
            startPortal.updatePreview(this);
        }
        for (var portal : portals) {
            if (portal.getRound() == round) {
                portal.updatePreview(this);
            }
        }
        // next round
        if (round >= maxRound) {
            entities.forEach(Entity::reset);
            if (playerEntity != null) {
                playerEntity.reset();
            }
            round = 1;
        }
        else {
            round++;
        }
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    public Entity getEntityOrNull(int fieldX, int fieldY) {
        for (var entity : entities) {
            if (entity.getFieldX() == fieldX && entity.getFieldY() == fieldY) {
                return entity;
            }
        }
        return null;
    }

    private String loadMap() {
        String result = null;
        try (
                InputStream inputStream = Model.class.getResourceAsStream("map.txt");
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)
        ){
            byte[] buffer = bufferedInputStream.readAllBytes();
            result = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return result;
    }
}
