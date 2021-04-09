package com.philipthedev.gamejam.paradox.model;

import com.philipthedev.gamejam.paradox.model.pathfinding.Pathfinder;
import com.philipthedev.gamejam.paradox.model.pathfinding.Position;
import com.philipthedev.gamejam.paradox.model.pathfinding.Track;

import java.util.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.List;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

/**
 * Something which acts in the world of this game.
 */
public abstract class Entity {

    private final int startPosX;
    private final int startPosY;

    private int posX = 0;
    private int posY = 0;
    private int range = 5;

    private Phase phase = Phase.IDLE;
    private Map<Integer, Track> roundToTrack = new HashMap<>();
    private Map<Integer, List<AttackAction>> roundToAction = new HashMap<>();

    private Track currentTrack;
    private Position moveToPosition = null;
    private final List<AttackAction> savedAttackActions = new ArrayList<>();
    private AttackAction currentAttackAction = null;

    public Entity(int fieldX, int fieldY) {
        posX = fieldX * TILE_SIZE;
        posY = fieldY * TILE_SIZE;
        this.startPosX = posX;
        this.startPosY = posY;
    }


    /**
     * Is called each round. Returns {@link RoundState#FINISHED} if its round is finished. {@link RoundState#PENDING} otherwise.
     * @param model current {@link Model}
     * @return {@link RoundState#FINISHED} or {@link RoundState#PENDING}
     */
    RoundState calculateRound(Model model) {
        if (phase == Phase.IDLE) {
            phase = Phase.MOVEMENT;
        }

        if (phase == Phase.MOVEMENT) {
            if (currentTrack == null) {
                Track track = roundToTrack.get(model.getRound());
                if (track != null) {
                    this.currentTrack = track;
                    this.posX = currentTrack.getStart().getX() * TILE_SIZE;
                    this.posY = currentTrack.getStart().getY() * TILE_SIZE;
                } else {
                    Set<Track> tracks = Pathfinder.findPath(model, getFieldX(), getFieldY(), range);
                    if (tracks.isEmpty()) {
                        phase = Phase.ATTACK;
                    }
                    else {
                        this.currentTrack = getTrackOrNull(model, tracks);
                        if (currentTrack == null) {
                            return RoundState.PENDING;
                        }
                        else {
                            roundToTrack.put(model.getRound(), currentTrack);
                        }
                    }
                }
            }
            if (currentTrack != null) {
                Position target = currentTrack.getTarget();
                if (getPosX() == target.getX() * TILE_SIZE && getPosY() == target.getY() * TILE_SIZE) {
                    moveToPosition = null;
                    currentTrack = null;
                    phase = Phase.ATTACK;
                }
                else {
                    if (moveToPosition == null) {
                        moveToPosition = currentTrack.getStart();
                    }
                    int moveX = moveToPosition.getX() * TILE_SIZE;
                    int moveY = moveToPosition.getY() * TILE_SIZE;
                    if (moveX > posX) {
                        posX++;
                    }
                    if (moveX < posX) {
                        posX--;
                    }
                    if (moveY > posY) {
                        posY++;
                    }
                    if (moveY < posY) {
                        posY--;
                    }
                    if (posX == moveX && posY == moveY) {
                        int nextPositionIndex = currentTrack.getStepIndex(moveToPosition) + 1;
                        if (nextPositionIndex < currentTrack.getSteps()) {
                            moveToPosition = currentTrack.getStep(nextPositionIndex);
                        }
                    }
                    return RoundState.PENDING;
                }
            }
        }

        if (phase == Phase.ATTACK) {
            List<AttackAction> attackActions = roundToAction.get(model.getRound());
            if (attackActions != null && !attackActions.isEmpty()) {
                if (currentAttackAction != null) {
                    boolean successful = currentAttackAction.executeAction(this, model);
                    if (successful) {
                        int nextIndex = attackActions.indexOf(currentAttackAction) + 1;
                        if (nextIndex >= attackActions.size()) {
                            phase = Phase.IDLE;
                            currentAttackAction = null;
                            return RoundState.FINISHED;
                        }
                    }
                    else {
                        return RoundState.PENDING;
                    }
                }
                else {
                    currentAttackAction = attackActions.get(0);
                }
            }
            else {
                if (currentAttackAction == null) {
                    currentAttackAction = getAttackActionOrNull(model);
                    if (currentAttackAction == null) {
                        return RoundState.PENDING;
                    }
                    else if (currentAttackAction == AttackAction.NEXT_ROUND) {
                        roundToAction.put(model.getRound(), new ArrayList<>(savedAttackActions));
                        savedAttackActions.clear();
                        phase = Phase.IDLE;
                        return RoundState.FINISHED;
                    }
                    else  {
                        savedAttackActions.add(currentAttackAction);
                    }
                }
                if (currentAttackAction != null) {
                  boolean successful = currentAttackAction.executeAction(this, model);
                  if (successful) {
                      currentAttackAction = null;
                  }
                }
                return RoundState.PENDING;
            }
        }
        throw new IllegalStateException("Something went wrong in Entity. Phase was: " + phase.toString());
    };

    void reset() {
        this.posX = startPosX;
        this.posY = startPosY;
    }

    abstract public Track getTrackOrNull(Model model, Set<Track> tracks);

    /**
     * @param model
     * @return {@code true} iff the game should continue.
     */
    abstract public AttackAction getAttackActionOrNull(Model model);

    abstract public void render(Graphics2D g, ImageObserver observer);

    public void renderAttack(Graphics2D g, ImageObserver observer) {
        if (currentAttackAction != null) {
            currentAttackAction.render(g, observer);
        }
    }


    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getFieldX() {
        return posX / TILE_SIZE;
    }

    public int getFieldY() {
        return posY / TILE_SIZE;
    }

    public boolean isBlockingField(int x, int y) {
        return phase != Phase.MOVEMENT && getFieldX() == x && getFieldY() == y;
    }

    public enum RoundState {
        PENDING, FINISHED;
    }

    private enum  Phase {
        MOVEMENT, ATTACK, IDLE;
    }

}
