package com.mygdx.game.model;

import com.mygdx.game.model.handler.CollisionHandler2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pontus
 *
 * Singleton of the model Adlez
 */
public class Adlez {
    private static Adlez adlez = new Adlez();

    public static Adlez getInstance() {
        return adlez;
    }

    private List<IWorldObject> worldObjects;

    private IPlayer player;
    private List<IEnemy> enemies;
    private List<IFriendlyNPC> friendlyNPCs;
    private List<IWorldObject> stationaryObjects;
    private List<IWall> walls;
    private List<IObstacle> obstacles;
    private List<IChest> chests;
    private CollisionHandler2 collisionHandler;
    private List<IAttack> attacks = new ArrayList<>();

    private Adlez() {
        /** Set players's width & height to size of texture for debugging purposes */
        player = new Player(Direction.NORTH, 2f,
                            17, 17,
                            0, 0,
                            100, 20, 0, 100);
    }

    public void initiateArea(Area area) {
        // Reset the world and then set it up for the new area
        worldObjects = new ArrayList<IWorldObject>();
    
        collisionHandler = new CollisionHandler2();

        // add the player and set him to the new position
        worldObjects.add(player);
        player.setPosX(area.getPlayerXposition());
        player.setPosY(area.getPlayerYposition());

        enemies = area.getEnemies();
        List<IWorldObject> tempList = new ArrayList<>();
        for(IEnemy enemy : enemies){
            tempList.add((IWorldObject) enemy);
        }
        worldObjects.addAll(tempList);
        
        tempList.clear();
        
        friendlyNPCs = area.getFriendlyNPCs();
        for(IFriendlyNPC friendlyNPC : friendlyNPCs){
            tempList.add((IWorldObject) friendlyNPC);
        }
        worldObjects.addAll(tempList);

        stationaryObjects = area.getStationaryObjects();
        worldObjects.addAll(stationaryObjects);

        walls = area.getWalls();
        worldObjects.addAll(walls);

        obstacles = area.getObstacles();
        worldObjects.addAll(obstacles);

        chests = area.getChests();
        worldObjects.addAll(chests);
        
        attacks = new ArrayList<>();
    }

    public IPlayer getPlayer() {
        return player;
    }

    public List<IEnemy> getEnemies() { return enemies; }

    public List<IFriendlyNPC> getFriendlyNPCs() {
        return friendlyNPCs;
    }

    public List<IWorldObject> getStationaryObjects() {
        return stationaryObjects;
    }

    public List<IWorldObject> getWorldObjects() {
        return worldObjects;
    }

    public List<IWall> getWalls() {
        return walls;
    }

    public List<IObstacle> getObstacles() {
        return obstacles;
    }

    public List<IChest> getChests() {
        return chests;
    }
    
    public void removeEnemyFromWorld(INPC enemy){
        enemies.remove(enemy);
        worldObjects.remove(enemy);
    }

    public void removeChestFromWorld(IChest chest) {
        chests.remove(chest);
        worldObjects.remove(chest);
    }
    
    public CollisionHandler2 getCollisionHandler(){
        return collisionHandler;
    }
    
    public List<IAttack> getAttacks(){
        return attacks;
    }
    
    public void removeAttackFromWorld(IAttack attack){
        attacks.remove(attack);
        worldObjects.remove(attack);
    }
}
