package com.ngot.windslash;


import java.util.Random;

import android.graphics.Bitmap;

public class Enemy extends Sprite {

	
	int kind;
	int dx,dy;
	int life,maxLife;
	int atkdelay=3000;
	long lastTime,thisTime,hitTime;
	boolean isHit = false;
	boolean isDead = false,isOut = false;
	int direction = 0;
	int dmg,atk,exp;
	boolean attack = false;
	public Enemy(int index, Bitmap[][] imgs, float x, float y, int kind,int hplevel) {
		super(index, imgs, x, y, kind);
		this.kind = kind;
		init();
		life*=hplevel;
		maxLife = life;
		lastTime = System.currentTimeMillis();
		dx = (int)(aimgWidth[0]*0.05f);
		atk = (int)(life*0.2f);
	}
	void init(){
		switch(kind){
		case 0:
			initAnimation(0, 4, 4);
			initAnimation(1, 8, 8);
			initAnimation(2, 12, 4);
			life = 700;
			exp = (int)(life*0.8f);
			break;
		case 1:
			initAnimation(0, 4, 4);
			initAnimation(1, 9, 9);
			initAnimation(2, 12, 4);
			life = 1200;
			exp = (int)(life*0.7f);
			break;
		case 2:
			initAnimation(0, 6, 6);
			initAnimation(1, 17, 15);
			initAnimation(2, 12, 6);
			life = 4000;
			exp = (int)(life*0.9f);
			break;
		}
		
	}

	public void Update(long thisTime) {
		this.thisTime = thisTime;
		switch (direction) {
		case 0:
			imgX -= dx;
			if (attack) {
				direction = 1;
				mainImg = 1;
				aCurrentFrame[0] = 0;
			}
			break;
		case 1:
			if (getlastFrame()) {
				direction = 0;
				mainImg = 0;
				aCurrentFrame[1] = 0;
				attack = false;
			}
			break;
		case 2:
			attack = false;
			if (getlastFrame()) {

				direction = 0;
				mainImg = 0;
				aCurrentFrame[2] = 0;

			}
			break;
		}

		if (isHit && direction != 2) {
			isHit = false;
			if (thisTime - hitTime > 50) {
				hitTime = thisTime;
				direction = 2;
				mainImg = 2;
				life -= dmg;
			}
		} else {
			isHit = false;
		}
		if (imgX < -imgWidth) {
			isOut = true;
		}
		if (life <= 0) {
			isDead = true;
		}

	}// end update

	public int getKind() {
		return kind;
	}
	public int getDirection() {
		return direction;
	}
	public void setHit(boolean isHit,int dmg) {
		this.isHit = isHit;
		this.dmg = dmg;
	}

	public boolean getHit() {
		return isHit;
	}
	
	public boolean setAttack(boolean attack) {
		this.attack = attack;
		if(direction==1&&getlastFrame())
			return true;
		
		return false;
	}
	public int getAtk() {
		return atk;
	}
	public int getExp() {
		return exp;
	}
	public int getMaxLife() {
		return maxLife;
	}
}











