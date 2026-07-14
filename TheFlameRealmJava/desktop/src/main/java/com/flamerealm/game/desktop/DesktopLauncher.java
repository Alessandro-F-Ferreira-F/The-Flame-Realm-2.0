package com.flamerealm.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.flamerealm.game.FlameRealmGame;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("The Flame Realm 2.0");
        config.setWindowedMode(900, 600);
        config.setResizable(false);
        new Lwjgl3Application(new FlameRealmGame(), config);
    }
}
