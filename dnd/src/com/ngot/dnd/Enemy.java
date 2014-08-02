package com.ngot.dnd;


import java.util.Random;

import android.graphics.Bitmap;

public class Enemy extends Sprite {

	
	int kind;
	int dx,dy;
	int life;
	int atkdelay=3000;
	long lastTime,thisTime,hitTime;
	boolean isHit = false;
	boolean isDead = false;
	int direction = 0;
	int cnt,dmg,atk,exp;
	boolean attack = false;
	public Enemy(int index, Bitmap[][] imgs, float x, float y, int kind) {
		super(index, imgs, x, y, kind);
		this.kind = kind;
		init();
		lastTime = System.currentTimeMillis();
		dx = aimgWidth[0]/25;
		dy = aimgWidth[0]/15;
		atk = (int)(life*0.2f);
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
				if(getlastFrame()){
					direction = 0;
					mainImg = 0;
					aCurrentFrame[1] = 0;
					attack = false;
				}
				break;
			case 2:
				attack = false;
				if(getlastFrame()){
					cnt++;
					if(cnt>12){
						cnt = 0;
						direction = 0;
						mainImg = 0;
						aCurrentFrame[2] = 0;
					}
				}
				break;
			}
		if(isHit&&direction!=2){
			isHit = false;
			if(thisTime-hitTime>80){
				hitTime = thisTime;
				direction = 2;
				mainImg = 2;
				life-=dmg;
			}
		}else{
			isHit = false;
		}
		if(life<=0){
			isDead = true;
		}
	}//end update
	
	void init(){
		switch(kind){
		case 0:
			initAnimation(0, 4, 4);
			initAnimation(1, 8, 8);
			initAnimation(2, 12, 4);
			life = 600;
			exp = 300;
			break;
		case 1:
			initAnimation(0, 4, 4);
			initAnimation(1, 9, 9);
			initAnimation(2, 12, 4);
			life = 1200;
			exp = 700;
			break;
		case 2:
			initAnimation(0, 6, 6);
			initAnimation(1, 17, 15);
			initAnimation(2, 12, 6);
			life = 4000;
			exp = 3000;
			break;
			
		}
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
}
