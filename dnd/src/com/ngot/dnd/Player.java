package com.ngot.dnd;

import android.graphics.Bitmap;

public class Player extends Sprite {

	
	
	
	
	private int dashSpeed = 20;
	private int defaultPositionx;
	private long dashTime,touchTime;
	boolean onTouch = false;
	int ex;
	
	public Player(int index, float x, float y) {
		super(index, x, y);
		defaultPositionx = imgX;
	}
	
	public int Update(int direction) {
		mainImg = direction;
		switch(direction){
		case 0://idle
			break;
		case 1://dash
			if(onTouch){
				direction = 1;
				onTouch = false;
			}
			if(System.currentTimeMillis()-touchTime>1000){
				direction = 2;
			}
			
			break;
		case 2://test
			if(mUpdateTime-dashTime<1000){
				if(onTouch){
					direction = 0;
					onTouch = false;
				}
			}else{
				direction = 0;
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
