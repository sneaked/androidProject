package com.ngot.dnd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Weapon extends Sprite {
	double radian;
	float speed;
	float dx;
	
	int maxRange;
	int px;
	
	boolean isHit = false,isDead = false;
	long currentTime,lastTime;
	int direction = 0;
	int atkSpeed;
	public Weapon(int index, Bitmap[] imgs, float x, float y,double radian,int px,int kind,float despeed) {
		super(index, imgs, px, y, kind);
		switch(kind){
		case 0:
			initAnimation(0, 20, 5);
			break;
		case 1:
			initAnimation(1, 24, 3);
			break;
		}
		this.radian = radian;
		this.px = px;
		
		switch(kind){
		case 0:
			atkSpeed = 300;
			speed = aimgWidth[0]/3;
			dx = speed/despeed;//max:45&55
			break;
		case 1:
			atkSpeed = 100;
			speed = aimgWidth[1]/3;
			dx = speed/40;
			break;
		}
		
	}
	
	
	@Override
	public void Update() {
		isHit = false;
		switch(direction){
		case 0:
			
			imgX = (int)(imgX+Math.cos(radian)*speed);
			imgY = (int)(imgY-Math.sin(radian)*speed);
			speed-=dx;
			
			if(imgX<px){
				direction = 1;
			}
			
			break;
		case 1:
			if(aCurrentFrame[mainImg]>=aiFrames[mainImg]-1){
				isDead = true;
			}
			break;
		}
		if(imgX>GView.sWidth+imgWidth){
			isDead = true;
		}
	}
	

	
	
	public void setHit() {
		
		currentTime = System.currentTimeMillis();
		if(currentTime-lastTime>atkSpeed){
			isHit = true;
			lastTime = currentTime;
		}
	}
	
	
	public boolean getHit() {
		return isHit;
	}

	
}
