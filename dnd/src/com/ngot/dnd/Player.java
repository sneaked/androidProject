package com.ngot.dnd;

import android.graphics.Bitmap;

public class Player extends Sprite {

	private int dashSpeed = 20;
	private int defaultPositionx;
	private long dashTime,touchTime;
	boolean onTouch = false;
	int ex;
	public Player(Bitmap img) {
		super(img);
		
	}
	@Override
	public void initSprite(float x, float y, float width, float height) {
		super.initSprite(x, y, width, height);
		defaultPositionx = imgX;
	}
	
	public int Update(int direction) {
		switch(direction){
		case 0://idle
			break;
		case 1://dash
			if(onTouch)
				imgX+=dashSpeed;
			
			if(ex-imgWidth<imgX){
				direction = 2;
				onTouch = false;
				dashSpeed = -dashSpeed;
				dashTime = System.currentTimeMillis();
			}
			
			if(imgX<defaultPositionx){
				imgX = defaultPositionx;
				dashSpeed = -dashSpeed;
				direction = 0;
				onTouch = false;
			}
			break;
		case 2://test
			if(mUpdateTime-dashTime<1000){
				if(onTouch){
					direction = 3;
					onTouch = false;
				}
			}else{
				direction = 1;
				onTouch = true;
			}
			break;
		case 3://test
			
			imgY+=s;
			if(imgY<300){
				s = -s;
			}
			if(imgY>GThread.ground){
				direction = 1;
				onTouch = true;
				s = -s;
				imgY = GThread.ground;
			}
			break;
		}
		return direction;
	}
	int s = -10;
	void setTouchTime(boolean touchChk){
		touchTime = System.currentTimeMillis();
		onTouch = touchChk;
		
	}
	void setEnemyx(int x){
		ex = x;
	}

}
