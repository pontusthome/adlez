package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.model.obstacles.IObstacle;

import java.util.List;

/**
 * Created by martinso on 27/04/16.
 */
public class ObstaclesView {

    private Texture obstacleTexture;

    public ObstaclesView(String obstacleImg) {
        obstacleTexture = new Texture((Gdx.files.internal((obstacleImg))));
    }

    public void generateObstacles(List<IObstacle> obstacles, SpriteBatch batch) {
        for(IObstacle obst : obstacles) {
            batch.draw(obstacleTexture, obst.getPosX(), obst.getPosY());
        }
    }
}
