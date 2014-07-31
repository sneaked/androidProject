package com.ngot.dnd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Missile extends Sprite {

	Rect hitbox = new Rect();
	
	public Missile(int index, Bitmap[] imgs, float x, float y) {
		super(index, imgs, x, y);
		initAnimation(0, 14, 7);
	}
	
	int dx = 5;
	boolean isHit = false,isDead = false;
	long currentTime;
	int direction = 0;
	@Override
	public void Update() {
		
		switch(direction){
		case 0:
			imgX+=dx;
			if(isHit){
				direction = 1;
				currentTime = System.currentTimeMillis();
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
	
	@Override
	public void drawSprite(Canvas canvas, boolean bg) {
		super.drawSprite(canvas, bg);
		makeHitbox();
	}
	
	
	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}

	void makeHitbox(){
		hitbox.left = imgX-imgWidth;
		hitbox.top = imgY;
		hitbox.right = hitbox.left+(imgWidth*2);
		hitbox.bottom = hitbox.top-imgHeight;
	}

}
