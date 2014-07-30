package com.ngot.dnd;

import android.graphics.Bitmap;

public class Player extends Sprite {

	
	private int defaultPositionx;
	private long dashTime,touchTime;
	boolean onTouch = false;
	int ex;
	
	public Player(int index, Bitmap[] imgs, float x, float y) {
		super(index, imgs, x, y);
		defaultPositionx = imgX;
	}
	
	
	
	public int Update(int direction) {
		mainImg = direction;
		switch(direction){
		case 0://idle
			aCurrentFrame[1] = 0;
			break;
		case 1://ready
			
			if(aCurrentFrame[mainImg]>=aiFrames[mainImg]-1){
				aCurrentFrame[1] = 8;
				direction = 2;
			}
			break;
		case 2://readyidle
			/*if(aCurrentFrame[mainImg]>=aiFrames[mainImg]-1){
				aCurrentFrame[2] = 0;
				direction = 3;
			}*/
			if(onTouch){
				onTouch = false;
				aCurrentFrame[2] = 0;
				direction = 3;
			}
			break;
		case 3://atk
			if(aCurrentFrame[mainImg]>=aiFrames[mainImg]-1){
				aCurrentFrame[3] = 0;
				direction = 2;
			}
			break;
		case 4://down
			if(aCurrentFrame[mainImg]>=aiFrames[mainImg]-1){
				aCurrentFrame[4] = 0;
				aCurrentFrame[0] = 0;
				onTouch = false;
				direction = 2;
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
