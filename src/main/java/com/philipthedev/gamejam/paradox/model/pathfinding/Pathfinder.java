package com.philipthedev.gamejam.paradox.model.pathfinding;

import com.philipthedev.gamejam.paradox.model.Model;

import java.util.*;

public final class Pathfinder {

    private Pathfinder() {
        //singleton
    }

    public static Set<Track> findPath(Model model, int startX, int startY, int range) {
        Track startTrack = new Track(startX, startY);
        Queue<Track> queue = new ArrayDeque<>();
        queue.add(startTrack);
        Map<Position, Track> positionTrackMap = new HashMap<>();
        if (! model.isBlocked(startX, startY)) {
            positionTrackMap.put(startTrack.getTarget(), startTrack);
        }
        while (! queue.isEmpty()) {
            Track currentTrack = queue.poll();
            for (var nextTrack : currentTrack.createNextSteps()) {
                Track previousCalculatedTrack = positionTrackMap.get(nextTrack.getTarget());
                if (previousCalculatedTrack != null) {
                    if (previousCalculatedTrack.getSteps() > nextTrack.getSteps()) {
                        positionTrackMap.put(nextTrack.getTarget(), nextTrack);
                        queue.add(nextTrack);
                    }
                }
                else {
                    if (nextTrack.getSteps() <= range && !model.isBlocked(nextTrack.getTarget().getX(), nextTrack.getTarget().getY())) {
                        positionTrackMap.put(nextTrack.getTarget(), nextTrack);
                        queue.add(nextTrack);
                    }
                }
            }
        }
        return new HashSet<>(positionTrackMap.values());
    }

}
