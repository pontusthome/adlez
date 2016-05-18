package com.mygdx.game.model;

import com.mygdx.game.model.exceptions.InventoryFullException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinso on 20/04/16.
 */
public class Chest extends WorldObject implements IChest, Serializable {

    private int chestSize;
    private int chestSizeCount = 0;
    private List<IItem> slots = new ArrayList<IItem>(chestSize);
    
    private int health;

    public Chest(float posX, float posY, int width, int height, int chestSize, int health) {
        super(posX, posY, width, height);
        this.chestSize = chestSize;
        setHealth(health);
    }
    
    @Override
    public int getHealth() {
        return health;
    }
    
    @Override
    public void setHealth(int health) {
        this.health = health;
    }
    
    @Override
    public void addItem(IItem type) throws InventoryFullException {
        if (chestSizeCount < chestSize) {
            slots.add(type);
            chestSizeCount++;
        } else {
            throw new InventoryFullException("Chest is full");
        }
    }

    @Override
    public int getSize() {
        return chestSize;

    }
    
    @Override
    public List<IItem> getItems(IChest chest) {
        return slots;
    }

    public boolean isFull() {
        return chestSizeCount == chestSize;
    }

    public void removeItem(IItem item) {
        slots.remove(item);
        chestSizeCount--;
    }
    
    @Override
    public void onCollide(Collidable other){
        if(other instanceof IInteraction){
            IInteraction interaction = (IInteraction) other;
            if(interaction.getCharacter() instanceof IPlayer){
                IPlayer player = (IPlayer) interaction.getCharacter();
                for(IItem item : slots){
                    try{
                        player.lootItem(item);
                    } catch(InventoryFullException e){
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public boolean isDestroyed(){
        return getHealth() <= 0;
    }
    
    @Override
    public boolean isEmpty(){
        return chestSizeCount <= 0;
    }
}
