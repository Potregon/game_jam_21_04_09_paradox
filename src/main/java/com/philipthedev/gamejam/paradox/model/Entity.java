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
    private final int startHP;
    private final int startRange;
    private final int startTimeSplitter;
    private final int startRound;

    private int posX = 0;
    private int posY = 0;
    private int hP   = 0;
    private int range = 5;
    private int timeSplitter = 0;

    private Phase phase = Phase.IDLE;
    private Map<Integer, Track> roundToTrack = new HashMap<>();
    private Map<Integer, List<AttackAction>> roundToAction = new HashMap<>();

    private Track currentTrack;
    private Position moveToPosition = null;
    private final List<AttackAction> savedAttackActions = new ArrayList<>();
    private AttackAction currentAttackAction = null;
    private boolean arrived;
    private boolean disposed = false;

    public Entity(int fieldX, int fieldY,
                  int startHP,
                  int startRange, int startTimeSplitter,
                  int startRound) {
        this.startPosX = fieldX * TILE_SIZE;
        this.startPosY = fieldY * TILE_SIZE;
        this.startHP = startHP;
        this.startRange = startRange;
        this.startTimeSplitter = startTimeSplitter;
        this.startRound = startRound;
        reset();
    }

    /**
     * Copy constructor
     * @param origin original
     */
    public Entity(Entity origin) {
        this.startPosX = origin.startPosX;
        this.startPosY = origin.startPosY;
        this.startHP = origin.startHP;
        this.startRange = origin.startRange;
        this.startTimeSplitter = origin.startTimeSplitter;
        this.startRound = origin.startRound;
        this.roundToAction = origin.roundToAction;
        this.roundToTrack = origin.roundToTrack;
        reset();
    }


    /**
     * Is called each round. Returns {@link RoundState#FINISHED} if its round is finished. {@link RoundState#PENDING} otherwise.
     * @param model current {@link Model}
     * @return {@link RoundState#FINISHED} or {@link RoundState#PENDING}
     */
    RoundState calculateRound(Model model) {
        if (startRound > model.getRound()) {
            this.arrived = false;
            return RoundState.FINISHED;
        }
        arrived = true;
        if (hP <= 0) {
            return RoundState.FINISHED;
        }
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
                        if (disposed) {
                            this.currentTrack = null;
                            phase = Phase.IDLE;
                            return RoundState.FINISHED;
                        }
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
                if (currentAttackAction == null) {
                    currentAttackAction = attackActions.get(0);
                }
                if (currentAttackAction != null) {
                    boolean successful = currentAttackAction.executeAction(this, model);
                    if (successful) {
                        int nextIndex = attackActions.indexOf(currentAttackAction) + 1;
                        if (nextIndex >= attackActions.size()) {
                            currentAttackAction = null;
                            phase = Phase.IDLE;
                            return RoundState.FINISHED;
                        }
                        else {
                            currentAttackAction = attackActions.get(nextIndex);
                            return RoundState.PENDING;
                        }
                    } else {
                        return RoundState.PENDING;
                    }
                }
            } else {
                if (currentAttackAction == null) {
                    currentAttackAction = getAttackActionOrNull(model);
                    if (currentAttackAction == null) {
                        return RoundState.PENDING;
                    }
                    else if (currentAttackAction == AttackAction.NEXT_ROUND) {
                        savedAttackActions.add(AttackAction.NEXT_ROUND);
                        roundToAction.put(model.getRound(), new ArrayList<>(savedAttackActions));
                        savedAttackActions.clear();
                        currentAttackAction = null;
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

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getTimeSplitter() {
        return timeSplitter;
    }

    public void setTimeSplitter(int timeSplitter) {
        this.timeSplitter = timeSplitter;
    }

    public int getHP() {
        return hP;
    }

    public boolean isKilled() {
        return hP <= 0;
    }

    public boolean isVisible() {
        return arrived && !disposed && hP > 0;
    }

    public void setHP(int hP) {
        this.hP = hP;
    }

    public void dispose() {
        disposed = true;
    }

    void reset() {
        posX = startPosX;
        posY = startPosY;
        hP = startHP;
        range = startRange;
        timeSplitter = startTimeSplitter;
        disposed = false;
        phase = Phase.IDLE;
    }

    public void damage(Entity aggressor, DamageType damageType, int damage, Model model) {
        if (hP <= 0) {
            return;
        }
        hP-= damage;
        if (hP <= 0) {
            killedBy(aggressor, model);
        }
    }

    abstract public Track getTrackOrNull(Model model, Set<Track> tracks);

    /**
     * @param model
     * @return {@code true} iff the game should continue.
     */
    abstract public AttackAction getAttackActionOrNull(Model model);

    abstract public void render(Graphics2D g, ImageObserver observer);

    abstract public void killedBy(Entity killer, Model model);

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

    public Position getFieldPosition() {
        return new Position(getFieldX(), getFieldY());
    }

    public int getFieldY() {
        return posY / TILE_SIZE;
    }

    public int getStartRound() {
        return startRound;
    }

    public boolean isBlockingField(Model model, int x, int y) {
        return isVisible() && startRound <= model.getRound() && phase != Phase.MOVEMENT && getFieldX() == x && getFieldY() == y;
    }

    public int getMaxHP() {
        return startHP;
    }

    protected void delegateDamage(int damage) {
        this.hP -= damage;
    }

    public enum RoundState {
        PENDING, FINISHED;
    }

    private enum  Phase {
        MOVEMENT, ATTACK, IDLE;
    }

}
