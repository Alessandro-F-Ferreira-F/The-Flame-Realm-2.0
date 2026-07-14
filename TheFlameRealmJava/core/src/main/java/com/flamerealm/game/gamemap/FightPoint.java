package com.flamerealm.game.gamemap;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Port de FightPoint.py.
 */
public class FightPoint {
    private Vector2 point;
    private List<String> allowedDirections;
    private boolean activated;

    public FightPoint(Vector2 point, List<String> allowedDirections) {
        this(point, allowedDirections, true);
    }

    public FightPoint(Vector2 point, List<String> allowedDirections, boolean activated) {
        this.point = point;
        this.allowedDirections = allowedDirections;
        this.activated = activated;
    }

    public void activate() {
        setActivated(true);
    }

    public void deactivate() {
        setActivated(false);
    }

    public Vector2 getPoint() {
        return point;
    }

    public void setPoint(Vector2 point) {
        this.point = point;
    }

    public List<String> getAllowedDirections() {
        return allowedDirections;
    }

    public void setAllowedDirections(List<String> allowedDirections) {
        this.allowedDirections = allowedDirections;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
