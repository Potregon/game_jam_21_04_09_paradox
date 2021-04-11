package com.philipthedev.gamejam.paradox.model.field;

public final class LightFlow {


    private final Field field;
    private final int distance;

    public LightFlow(Field field, int distance) {
        this.field = field;
        this.distance = distance;
    }

    public int getRed() {
        return Math.max(0, field.getRedSource() - distance * 5);
    }

    public int getGreen() {
        return Math.max(0, field.getGreenSource() - distance * 5);
    }

    public int getBlue() {
        return Math.max(0, field.getBlueSource() - distance * 5);
    }

}
