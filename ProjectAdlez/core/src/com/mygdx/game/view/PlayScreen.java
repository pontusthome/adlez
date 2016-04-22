package com.mygdx.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.controller.EnemyController;
import com.mygdx.game.controller.PlayerController;
import com.mygdx.game.model.Adlez;
import com.mygdx.game.model.NPC;
import com.mygdx.game.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by martinso on 27/03/16.
 */
public class PlayScreen implements Screen {

    private Adlez adlez = Adlez.getInstance();

    private Player player = adlez.getPlayer();
    private CharacterView playerView;
    private PlayerController playerController;

    private HashMap<NPC, CharacterView> enemies;
    private List<EnemyController> enemyControllers;

    private Game game;

    private OrthographicCamera playerCam;

    private SpriteBatch batch;

    private TiledMap testing1;
    private OrthoCachedTiledMapRenderer renderer;
    private Stage stage;

    public PlayScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        playerCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        stage = new Stage(new ScreenViewport());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        // Spawning player.
        playerView = new CharacterView("playerSpritesMove.png");
        playerController = new PlayerController(player, playerView);

        // Spawning enemies.
        enemies = new HashMap<NPC, CharacterView>();
        enemyControllers = new ArrayList<EnemyController>();
        for (NPC enemy: adlez.getEnemies()) {
            CharacterView enemyView = new CharacterView("playerSpritesMove.png");
            EnemyController enemyController = new EnemyController(enemy, enemyView, player);
            enemyControllers.add(enemyController);
            enemies.put(enemy, enemyView);
        }

        // temporary things, just testing
        testing1 = new TmxMapLoader().load("test1.tmx");
        renderer = new OrthoCachedTiledMapRenderer(testing1);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render Tiled map
        renderer.setView(playerCam);
        renderer.render();
        stage.act();

        stage.draw();
        batch.setProjectionMatrix((playerCam.combined));
        playerCam.update();

        batch.begin();

        // Updating player
        playerController.update();
        playerCam.position.set(player.getPosX() + (playerView.getCurrentFrame().getRegionWidth() / 2),
                               player.getPosY() + (playerView.getCurrentFrame().getRegionHeight() / 2),
                               0); // z = 0, non 3D
        batch.draw(playerView.getCurrentFrame(),
                   player.getPosX(),
                   player.getPosY());

        // Updating enemies
        for (EnemyController enemyController: enemyControllers) {
            enemyController.update();
        }
        for(Map.Entry<NPC, CharacterView> entry : enemies.entrySet()) {
            NPC enemy = entry.getKey();
            CharacterView view = entry.getValue();
            batch.draw(view.getCurrentFrame(),
                    enemy.getPosX(),
                    enemy.getPosY());
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
