package com.ngot.dnd;


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
	
	public Enemy(int index, Bitmap[][] imgs, float x, float y, int kind) {
		super(index, imgs, x, y, kind);
		this.kind = kind;
		if(kind==0){
			initAnimation(0, 4, 4);
			initAnimation(1, 8, 8);
			initAnimation(2, 12, 4);
		}else{
			initAnimation(0, 4, 4);
			initAnimation(1, 9, 9);
			initAnimation(2, 12, 4);
		}
		lastTime = System.currentTimeMillis();
		life = (kind==0)?600:1200;
		
		dx = aimgWidth[0]/25;
		dy = aimgWidth[0]/15;
	}
	int cnt;
	public void Update(long thisTime) {
		if(kind==0){
			switch(direction){
			case 0:
				imgX-=dx;
				if(thisTime-lastTime>atkdelay){
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
				}
				break;
			case 2:
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
			
		}else if(kind==1){
			switch(direction){
			case 0:
				imgX-=dx;
				if(thisTime-lastTime>atkdelay){
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
				}
				break;
			case 2:
				if(getlastFrame()){
					cnt++;
					if(cnt>4){
						cnt = 0;
						direction = 0;
						mainImg = 0;
						aCurrentFrame[2] = 0;
					}
				}
				break;
			}
			
		}
		if(isHit&&direction!=2){
			isHit = false;
			if(thisTime-hitTime>600){
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
	int dmg;
	public void setHit(boolean isHit,int dmg) {
		this.isHit = isHit;
		this.dmg = dmg;
	}

	public boolean getHit() {
		return isHit;
	}
	
	
	
	
}
