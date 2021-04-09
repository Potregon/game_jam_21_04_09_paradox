package com.philipthedev.gamejam.paradox.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class Model {

    public static final int TILE_SIZE = 64;

    private final Field[][] fields;
    private final int width;
    private final int height;

    private final List<Entity> entities = new ArrayList<>();
    private Entity currentEntity = null;
    private int round = 1;

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
                        entities.add(new PlayerEntity(x, y));
                        break;
                    default:
                        fields[x][y] = new Field(Field.FieldType.PASSABLE, x, y);
                        break;
                }
            }
        }
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
        return false;
    }

    public List<Entity> listEntities() {
        return Collections.unmodifiableList(entities);
    }

    public Entity getCurrentEntityOrNull() {
        return currentEntity;
    }

    public void calculateModel() {
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
                int nextIndex = entities.indexOf(currentEntity) + 1;
                if (nextIndex >= entities.size()) {
                    currentEntity = null;
                    nextRound();
                }
                else {
                    currentEntity = entities.get(nextIndex);
                }
            }
        }
    }

    public void clearActions() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                fields[x][y].setAction(null);
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

    public int getRound() {
        return round;
    }

    private void nextRound() {
        if (round >= 3) {
            entities.forEach(Entity::reset);
            round = 1;
        }
        else {
            round++;
        }
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
