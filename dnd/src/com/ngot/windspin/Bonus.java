package com.ngot.windspin;

import android.graphics.Bitmap;

public class Bonus extends Sprite {

	

	int kind;
	int dx,dy;
	int life;
	int atkdelay=3000;
	long lastTime,thisTime,hitTime;
	boolean isHit = false;
	boolean isDead = false,isOut = false;
	int direction = 0;
	int cnt,dmg,atk;
	boolean attack = false;
	int exp;
	public Bonus(int index, Bitmap[] imgs, float x, float y) {
		super(index, imgs, x, y);
		initAnimation(0, 4, 4);
		initAnimation(1, 4, 4);
		lastTime = System.currentTimeMillis();
		dx = aimgWidth[0]/70;
		exp = 30000;
		life = 10;
	}
	
	public void Update(long thisTime) {
		this.thisTime = thisTime;
			switch(direction){
			case 0:
				imgX-=dx;
				if(attack){
					direction = 1;
					mainImg = 1;
					aCurrentFrame[0] = 0;
					lastTime = thisTime;
				}
				break;
			case 1:
				if(thisTime-hitTime>2000){
					direction = 0;
					mainImg = 0;
					aCurrentFrame[1] = 0;
				}				
				break;
			}
		if(isHit&&direction!=1){
			isHit = false;
			if(thisTime-hitTime>2000){
				hitTime = thisTime;
				direction = 1;
				mainImg = 1;
				life--;
			}
		}else{
			isHit = false;
		}
		if(life<=0){
			isDead = true;
		}
		if(imgX<-imgWidth){
			isOut = true;
		}
	}//end update
	
	public int getExp() {
		return exp;
	}
	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}

	public boolean getHit() {
		return isHit;
	}

}
