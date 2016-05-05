package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;

import com.mygdx.game.controller.AttackController;
import com.mygdx.game.controller.IController;
//import com.mygdx.game.controller.CombatHandler;
import com.mygdx.game.controller.PlayerController;
import com.mygdx.game.controller.EnemyController;
import com.mygdx.game.model.*;
import com.mygdx.game.model.handler.CollisionHandler2;
import com.mygdx.game.utils.AssetStrings;
import com.sun.org.apache.bcel.internal.generic.IADD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Viktor on 2016-04-19.
 */
public class GameScreen extends AbstractScreen {

    private Adlez adlez = Adlez.getInstance();

    private IPlayer player = adlez.getPlayer();
    private IController playerController;
    private OrthographicCamera playerCam;

    private HashMap<INPC, IController> enemies;

    private SpriteBatch batch;
    private OrthoCachedTiledMapRenderer renderer;
    private TiledMap tileMap;

    private ObstaclesView obstaclesView;
    private ChestView chestView;
    
    private CollisionHandler2 collisionHandler;
    private List<IAttack> attacks;
    
    private HashMap<IAttack, IController> attackControllers;
    private ShapeRenderer debugRenderer = new ShapeRenderer();
    private List<IAttack> newAttacks;
    
    private static final float UNIT_SCALE = 1/2f;
    private static final float WIDTH_SCALE = 2/3f;
    private static final float HEIGHT_SCALE = 2/3f;

    public GameScreen() {
        super();
        batch = new SpriteBatch();
        obstaclesView = new ObstaclesView(batch, AssetStrings.BOX_OBSTACLE_IMAGE);
        chestView = new ChestView(batch, AssetStrings.CHEST_IMAGE);

    }

    @Override
    public void buildStage() {
    
        collisionHandler = adlez.getCollisionHandler();
        
        // Creating camera
        playerCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        // Spawning player.
        playerController = new PlayerController(player, AssetStrings.MOVE_SPRITES_IMAGE);

        // Spawning enemies.
        enemies = new HashMap<INPC, IController>();
        for (IEnemy enemy: adlez.getEnemies()) {
            EnemyController enemyController = new EnemyController((INPC) enemy, AssetStrings.MOVE_SPRITES_IMAGE, player);
            enemies.put((INPC) enemy, enemyController);
        }
        
        attackControllers = new HashMap<>();
        for (IAttack attack: adlez.getAttacks()) {
            AttackController attackController = new AttackController(attack);
            attackControllers.put(attack, attackController);
        }
    
        attacks = adlez.getAttacks();
        newAttacks = adlez.getNewAttacks();

        // temporary things, just testing
        tileMap = new TmxMapLoader().load(AssetStrings.TEST_LEVEL_TMX);
        float unitScale = UNIT_SCALE;

        renderer = new OrthoCachedTiledMapRenderer(tileMap, unitScale);
        playerCam.setToOrtho(false, Gdx.graphics.getWidth() * WIDTH_SCALE, 
                Gdx.graphics.getHeight() * HEIGHT_SCALE);
    }

    @Override
    public void render(float delta) {
        updateGame();

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render Tiled map
        renderer.setView(playerCam);
        renderer.render();

        batch.setProjectionMatrix((playerCam.combined));
        batch.begin();

        // Update camera
        playerCam.update();
        playerCam.position.set(player.getPosX() + (playerController.getView().getCurrentFrame().getRegionWidth() / 2),
                player.getPosY() + (playerController.getView().getCurrentFrame().getRegionHeight() / 2),
                0); // z = 0, non 3D

        // Render player
        playerController.render(batch);
        
        //Render enemies
        for(Map.Entry<INPC, IController> entry : enemies.entrySet()) {
            IController enemyController = entry.getValue();
            enemyController.render(batch);
        }
        
        // Generate obstacles
        obstaclesView.generateObstacles();
        chestView.generateChests();

        batch.end();
    
        debugRender();
    }

    public void updateGame() {
//        /** Remove all attacks for now. If they hit something then the collision handler handled it in previous loop. */
//        if(!attacks.isEmpty()){
//            List<IAttack> attacksToRemove = new ArrayList<>();
//            for(IAttack attack : attacks){
//                if(attack.isFinished()){
//                    attacksToRemove.add(attack);
//                }
//            }
//            for(IAttack attack : attacksToRemove){
//                adlez.removeAttackFromWorld(attack);
//            }
//        }
        
        // Updating player
        playerController.update();
        collisionHandler.updatePlayer();

        // Updating enemies
        List<INPC> killedEnemies = new ArrayList<INPC>();
        for(Map.Entry<INPC, IController> entry : enemies.entrySet()) {
            INPC enemy = entry.getKey();
            IController enemyController = entry.getValue();
    
            enemyController.update();
            if (!enemy.isAlive()) {
                killedEnemies.add(enemy);
            }
        }
        for (INPC deadEnemy: killedEnemies) {
            enemies.remove(deadEnemy);
        }
    
        // Update attacks
        if(!newAttacks.isEmpty()){
            for(IAttack attack : newAttacks){
                AttackController attackController = new AttackController(attack);
                attackControllers.put(attack, attackController);
            }
            newAttacks.clear();
        }
        List<IAttack> finishedAttacks = new ArrayList<IAttack>();
        for(Map.Entry<IAttack, IController> entry : attackControllers.entrySet()){
            IAttack attack = entry.getKey();
            IController attackController = entry.getValue();
        
            if (attack.isFinished()) {
                finishedAttacks.add(attack);
            }
            else {
                attackController.update();
            }
        }
        for (IAttack finishedAttack: finishedAttacks) {
            attackControllers.remove(finishedAttack);
            adlez.removeAttackFromWorld(finishedAttack);
        }
        
        collisionHandler.updateWorld();
    }
    
    private void debugRender(){
        debugRenderer.setProjectionMatrix(playerCam.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(1, 1, 0, 1);
        HitBox hitBox = PlayerController.currentAttack.getHitBox();
        debugRenderer.rect(hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
        debugRenderer.rect(player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
        List <IWall> tempList = adlez.getWalls();
        for(IWall wall : tempList){
            debugRenderer.rect(wall.getPosX(), wall.getPosY(), wall.getWidth(), wall.getHeight());
        }
        debugRenderer.end();
    }
}
