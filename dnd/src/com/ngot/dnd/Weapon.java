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
	
	public Weapon(int index, Bitmap[] imgs, float x, float y,double radian,int maxRange,int px) {
		super(index, imgs, x, y);
		initAnimation(0, 20, 5);
		this.radian = radian;
		this.maxRange = maxRange;
		this.px = px;
		speed = aimgWidth[0]/3;
		dx = speed/45;
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
		if(currentTime-lastTime>300){
			isHit = true;
			lastTime = currentTime;
		}
	}
	
	
	public boolean getHit() {
		return isHit;
	}

	

}
