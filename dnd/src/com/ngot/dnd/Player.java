package com.ngot.dnd;

import java.util.Random;

import android.graphics.Bitmap;

public class Player extends Sprite {

	
	boolean onTouch = false;
	int atk = 149,saveAtk;
	float hp = 3000f;
	float maxHp = hp;
	float exp = 0;
	float maxExp = 3000;
	float mag[] = {0.8f,0.9f,1f,1.1f,1.2f,1.3f};
	int level = 1,maxLevel = 61;
	int savey;
	Random rnd = new Random();
	public Player(int index, Bitmap[] imgs, float x, float y) {
		super(index, imgs, x, y);
		savey = imgY;
	}
	
	
	public int Update(int direction) {
		mainImg = direction;
		switch(direction){
		case 0:
			if(onTouch){
				onTouch = false;
				direction = 1;
				aCurrentFrame[0] = 0;
				imgY+=aimgHeight[1]*0.09f;
			}
			break;
		case 1:
			if(getlastFrame()){
				direction = 0;
				aCurrentFrame[1] = 0;
				imgY = savey;
			}
			break;
		}
		return direction;
	}
	
	public boolean levelup(){
		
		if(exp>=maxExp&&level<maxLevel){
			float tmp = exp-maxExp;
			level++;
			exp = 0;
			maxExp+=900;
			exp+=tmp;
			atk+=(int)(atk*0.03f);
			saveAtk+=(int)(atk*0.03f);
			return true;
		}
		return false;
	}
	public int getLevel() {
		return level;
	}
	void setTouchTime(boolean touchChk){
		onTouch = touchChk;
		
	}

	public void setAtk(int atk) {
		saveAtk = this.atk;
		this.atk = atk;
	}
	
	public int getSaveAtk() {
		return saveAtk;
	}
	public int getAtk() {
		int n = rnd.nextInt(6);
		return (int)(atk*mag[n]);
	}
	
	void decreaseHp(int eatk){
		hp-=eatk;
	}
}
