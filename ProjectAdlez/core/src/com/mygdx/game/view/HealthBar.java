package com.mygdx.game.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.model.characters.ICharacter;

/**
 * Created by Viktor on 2016-05-03.
 */
public class HealthBar implements IStatusBar {

    private ICharacter character;
    private Pixmap background;
    private Pixmap bar;

    private int barWidth;
    private int barHeight;

    private int currentValue;
    private int maxValue;
    private Image barImage;

    public HealthBar(ICharacter character){
        this.character = character;
        updateCurrentValues();
        this.bar = new Pixmap(currentValue, 10, Pixmap.Format.RGBA8888);
        bar.setColor(Color.RED);
        bar.fill();

        this.background = new Pixmap(100, 10, Pixmap.Format.RGBA8888);
        background.setColor(Color.WHITE);
        background.fill();
        updateBar();
    }

    public void updateBar(){
        updateCurrentValues();
        background.drawPixmap(bar,0,0,0,0,currentValue,9);
        barImage = new Image(new Texture(background));
    }

    private void updateCurrentValues(){
        maxValue = 100;
        currentValue = 100;
        /*
        this.maxValue = character.getMaxHealth();
        int tmp;
        tmp = character.getHealth()*100/maxValue;
        if(tmp < 1){
            this.currentValue = 0;
        }else if(tmp>100){
            this.currentValue = 100;
        }else {
            this.currentValue = tmp;
        }*/
    }

    public Image getBarImage(){
        return barImage;
    }
}