package com.flamerealm.game.gamemap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Port de GameMap.py. As posicoes sempre sao substituidas por um Vector2 novo
 * (nunca mutadas in-place via .set()), do mesmo jeito que o Python original
 * sempre substituia a tupla inteira - assim currentPoint/previousPoint nunca
 * corrompem os Vector2 constantes compartilhados de GameConstants.
 */
public class GameMap {
    private final FightPoint currentPoint;
    private final FightPoint previousPoint;
    private Texture image;
    private final List<FightPoint> fightPoints;
    private float tol;

    public GameMap(Texture image, List<FightPoint> fightPoints, float pointDistTollerance) {
        FightPoint first = fightPoints.get(0);
        this.currentPoint = new FightPoint(new Vector2(first.getPoint()), first.getAllowedDirections(), first.getActivated());
        this.previousPoint = new FightPoint(new Vector2(first.getPoint()), first.getAllowedDirections(), first.getActivated());
        this.image = image;
        this.fightPoints = fightPoints;
        this.tol = pointDistTollerance;
    }

    public boolean pointMatchTest(FightPoint point) {
        return point.getPoint().x - getTol() <= getCurrentPoint().getPoint().x
                && getCurrentPoint().getPoint().x <= point.getPoint().x + getTol()
                && point.getPoint().y - getTol() <= getCurrentPoint().getPoint().y
                && getCurrentPoint().getPoint().y <= point.getPoint().y + getTol();
    }

    public boolean pointMatch(FightPoint point) {
        boolean test = pointMatchTest(point);
        if (test) {
            getCurrentPoint().setPoint(point.getPoint());
            getCurrentPoint().setAllowedDirections(point.getAllowedDirections());
            getCurrentPoint().setActivated(point.getActivated());
        }
        return test;
    }

    public boolean fightPointTest() {
        for (FightPoint fightPoint : getFightPoints()) {
            if (pointMatch(fightPoint)) {
                return true;
            }
        }
        return false;
    }

    public void disableCurrentPoint() {
        for (FightPoint fightPoint : getFightPoints()) {
            if (pointMatchTest(fightPoint)) {
                fightPoint.deactivate();
                getCurrentPoint().deactivate();
            }
        }
    }

    public FightPoint getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(FightPoint point) {
        getCurrentPoint().setPoint(point.getPoint());
        getCurrentPoint().setAllowedDirections(point.getAllowedDirections());
        getCurrentPoint().setActivated(point.getActivated());
    }

    public Texture getImage() {
        return image;
    }

    public void setImage(Texture image) {
        this.image = image;
    }

    public List<FightPoint> getFightPoints() {
        return fightPoints;
    }

    public float getTol() {
        return tol;
    }

    public void setTol(float tol) {
        this.tol = tol;
    }

    public FightPoint getPreviousPoint() {
        return previousPoint;
    }

    public void setPreviousPoint(FightPoint point) {
        getPreviousPoint().setPoint(point.getPoint());
        getPreviousPoint().setAllowedDirections(point.getAllowedDirections());
        getPreviousPoint().setActivated(point.getActivated());
    }
}
