package com.philipthedev.gamejam.paradox.model;

import com.philipthedev.gamejam.paradox.model.actions.HitAction;
import com.philipthedev.gamejam.paradox.model.actions.SkipRoundAction;
import com.philipthedev.gamejam.paradox.model.pathfinding.Track;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Random;
import java.util.Set;

import static com.philipthedev.gamejam.paradox.model.Model.TILE_SIZE;

public class PlayerEntity extends Entity {

    Random random = new Random();

    private Track selectedTrack = null;
    private AttackAction attackAction = null;
    private AttackPhase attackPhase = AttackPhase.IDLE;

    public PlayerEntity(int fieldX, int fieldY, int startRound) {
        super(fieldX, fieldY, 50, 5, 0, startRound);
    }
    public PlayerEntity(PlayerEntity original, int fieldX, int fieldY, int round) {
        super(fieldX, fieldY, 50, original.getRange(), original.getTimeSplitter(), round);
    }

    @Override
    public Track getTrackOrNull(Model model, Set<Track> tracks) {
        if (selectedTrack != null) {
            Track result = selectedTrack;
            selectedTrack = null;
            return result;
        }
        for (var track : tracks) {
            Field field = model.getField(track.getTarget().getX(), track.getTarget().getY());
            field.setFieldAction(new FieldAction(new Color(0, 255, 0, 50), new Color(0, 255, 0, 250),
                    () -> {
                        selectedTrack = track;
                        model.clearActions();
                    }));
        }
        return null;
    }

    @Override
    public AttackAction getAttackActionOrNull(Model model) {
        switch (attackPhase) {
            case IDLE:
                model.addActionButton(new HitAction(this));
                model.addActionButton(new SkipRoundAction(this));
                attackPhase = AttackPhase.SHOW_BUTTONS;
                break;
            case SHOW_BUTTONS:
                AttackAction attackAction = this.attackAction;
                if (attackAction != null) {
                    this.attackAction = null;
                    model.clearActions();
                    model.clearActionButtons();
                    attackPhase = attackAction == AttackAction.NEXT_ROUND ? AttackPhase.IDLE : AttackPhase.ATTACK_SELECTED;
                    return attackAction;
                }
                break;
            case ATTACK_SELECTED:
                attackPhase = AttackPhase.IDLE;
                return AttackAction.NEXT_ROUND;
            default:
                attackPhase = AttackPhase.IDLE;
                break;
        }
        return null;
    }

    public void setAttackAction(AttackAction attackAction) {
        this.attackAction = attackAction;
    }

    @Override
    public void render(Graphics2D g, ImageObserver observer) {
        if (isVisible()) {
            g.setColor(Color.YELLOW);
            g.fillOval(getPosX(), getPosY(), TILE_SIZE, TILE_SIZE);
        }
    }

    @Override
    public void killedBy(Entity killer, Model model) {

    }

    private enum AttackPhase {
        IDLE, SHOW_BUTTONS, ATTACK_SELECTED;
    }
}
