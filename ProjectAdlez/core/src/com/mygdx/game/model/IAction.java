package com.mygdx.game.model;

/**
 * Created by Michel on 8.5.2016.
 */
public interface IAction extends IWorldObject{
	HitBox getHitBox();
	boolean isFinished();
	void setFinished();
	void setSound(GameSound attackSound);
	void playSound(float volume);
	GameSound getSound();
	void setInitLocation(ICharacter character);
	ICharacter getCharacter();
}