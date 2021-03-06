package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.builder.GameBuilder;
import com.mygdx.game.builder.AreaHandler;
import com.mygdx.game.builder.GameIO;
import com.mygdx.game.model.Adlez;
import com.mygdx.game.model.exceptions.InventoryFullException;
import com.mygdx.game.model.exceptions.ItemNotFoundException;

import java.io.IOException;

/**
 * Created by Viktor on 2016-04-19.
 */
public abstract class AbstractScreen extends Stage implements Screen {

    protected AbstractScreen() {
        super(new StretchViewport(1280, 720, new OrthographicCamera()) );
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Calling to Stage methods
        super.act(delta);
        super.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height);
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    public abstract void buildStage();

    protected void loadSavedGame() {
        try {
            loadGame();
        } catch (Exception e) {
            System.out.println("Could not find saved game.");
            System.out.println("Starting new game.");
            newGame();
        } finally {
            initiateGame();
        }
    }

    protected void loadGame() throws IOException, InventoryFullException, ItemNotFoundException {
        // Load the AreaHandler
        GameIO areaBuilder = new GameBuilder();
        areaBuilder.loadAreaHandler();

        // Load the Player
        areaBuilder.loadPlayer();
    }

    public void newGame() {
        Adlez.getInstance().resetPlayer();
        AreaHandler.getInstance().resetAreaHandler();
    }

    protected void initiateGame() {
        // Initiate game
        Adlez adlez = Adlez.getInstance();
        adlez.initiateArea(AreaHandler.getInstance().getCurrentArea());

        ScreenManager.getInstance().showScreen(ScreenEnum.GAME);
    }
}

