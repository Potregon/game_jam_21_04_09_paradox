package com.philipthedev.gamejam.paradox.model.pathfinding;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Track {

    private final Position[] positions;

    public Track(int x, int y) {
        positions = new Position[]{new Position(x, y)};
    }

    private Track(Track track, int x, int y) {
        positions = new Position[track.getSteps() + 1];
        for (int stepIndex = 0; stepIndex < track.getSteps(); stepIndex++) {
            positions[stepIndex] = track.getStep(stepIndex);
        }
        positions[positions.length - 1] = new Position(x, y);
    }

    public Position getStart() {
        return positions[0];
    }

    public Position getTarget() {
        return positions[positions.length - 1];
    }

    public int getSteps() {
        return positions.length;
    }

    public Position getStep(int stepIndex) {
        return positions[stepIndex];
    }

    public Set<Track> createNextSteps() {
        Position target = getTarget();
        return Set.of(
                new Track(this, target.getX() - 1, target.getY()),
                new Track(this, target.getX(), target.getY() - 1),
                new Track(this, target.getX() + 1, target.getY()),
                new Track(this, target.getX(), target.getY() + 1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return Arrays.equals(positions, track.positions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(positions);
    }

    public int getStepIndex(Position moveToPosition) {
        for (int stepIndex = 0; stepIndex < positions.length; stepIndex++) {
            if (positions[stepIndex].equals(moveToPosition)) {
                return stepIndex;
            }
        }
        throw new IllegalArgumentException("Position is not part of this track.");
    }
}
