package com.flamerealm.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flamerealm.game.instances.GameInstances;
import com.flamerealm.game.screens.CombatScreen;
import com.flamerealm.game.screens.DeathScreen;
import com.flamerealm.game.screens.HistoryScreen;
import com.flamerealm.game.screens.MainMenuScreen;
import com.flamerealm.game.screens.PauseScreen;
import com.flamerealm.game.screens.PlayScreen;
import com.flamerealm.game.screens.TutorialScreen;

public class FlameRealmGame extends Game {
    public Assets assets;
    public GameInstances instances;
    public SpriteBatch batch;
    public OrthographicCamera camera;

    // As 7 telas sao singletons reaproveitados (nunca recriados a cada troca),
    // para que estado transiente (ex: pausar no meio de um movimento no mapa,
    // ou o turno em andamento no combate) sobreviva a troca de tela, do mesmo
    // jeito que o loop unico do main.py original.
    public MainMenuScreen mainMenu;
    public HistoryScreen history;
    public TutorialScreen tutorial;
    public PlayScreen play;
    public CombatScreen combat;
    public PauseScreen pause;
    public DeathScreen death;

    @Override
    public void create() {
        assets = new Assets();
        batch = new SpriteBatch();

        // yDown = true: (0,0) no canto superior-esquerdo, Y cresce para baixo,
        // igual ao Pygame - permite reusar todas as posicoes de GameConstants
        // (portadas literalmente de parameters.py) sem inverter nenhuma formula.
        camera = new OrthographicCamera();
        camera.setToOrtho(true, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        batch.setProjectionMatrix(camera.combined);

        instances = new GameInstances(assets);

        mainMenu = new MainMenuScreen(this);
        history = new HistoryScreen(this);
        tutorial = new TutorialScreen(this);
        play = new PlayScreen(this);
        combat = new CombatScreen(this);
        pause = new PauseScreen(this);
        death = new DeathScreen(this);

        setScreen(mainMenu);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        assets.dispose();
    }
}
