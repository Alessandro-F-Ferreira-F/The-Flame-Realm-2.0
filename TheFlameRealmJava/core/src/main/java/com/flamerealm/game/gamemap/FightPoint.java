package com.flamerealm.game.gamemap;

import com.badlogic.gdx.math.Vector2;
import com.flamerealm.game.assets.EncounterManifest;

import java.util.List;

/**
 * Port de FightPoint.py. O manifest diz qual EncounterManifest carregar ao
 * ativar este ponto (null para pontos que nao disparam combate, ex.: startPoint).
 */
public class FightPoint {
    private Vector2 point;
    private List<String> allowedDirections;
    private boolean activated;
    private EncounterManifest manifest;

    public FightPoint(Vector2 point, List<String> allowedDirections) {
        this(point, allowedDirections, true, null);
    }

    public FightPoint(Vector2 point, List<String> allowedDirections, boolean activated) {
        this(point, allowedDirections, activated, null);
    }

    public FightPoint(Vector2 point, List<String> allowedDirections, boolean activated, EncounterManifest manifest) {
        this.point = point;
        this.allowedDirections = allowedDirections;
        this.activated = activated;
        this.manifest = manifest;
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

    public EncounterManifest getManifest() {
        return manifest;
    }

    public void setManifest(EncounterManifest manifest) {
        this.manifest = manifest;
    }
}
