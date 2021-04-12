package com.philipthedev.gamejam.paradox.model;

import java.awt.*;
import java.awt.image.ImageObserver;

public interface AttackAction {

    AttackAction NEXT_ROUND = new AttackAction() {
        @Override
        public boolean executeAction(Entity entity, Model model) {
            return true;
        }
    };

    AttackAction SKIP_ROUND = new AttackAction() {
        @Override
        public boolean executeAction(Entity entity, Model model) {
            return true;
        }
    };

    /**
     *
     * @param entity
     * @param model
     * @return {@code true} iff this action was successful
     */
    boolean executeAction(Entity entity, Model model);

    default void render(Graphics2D g, ImageObserver imageObserver) {

    }

}
