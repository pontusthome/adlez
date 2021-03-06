package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.model.characters.IFriendlyNPC;

import java.util.List;

/**
 * Created by martinso on 09/05/16.
 */
public class FriendlyNPCView {

    private Texture npcTexture;

    public FriendlyNPCView(String npcImg) {
        npcTexture = new Texture((Gdx.files.internal((npcImg))));
    }

    public void generateFriendlyNPC(List<IFriendlyNPC> npcs, SpriteBatch batch) {
        for(IFriendlyNPC npc : npcs) {
            batch.draw(npcTexture, npc.getPosX(), npc.getPosY());
        }
    }
}
