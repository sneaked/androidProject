package com.ngot.dnd;

import android.graphics.Bitmap;

public class Player extends Sprite {

	
	boolean onTouch = false;
	int maxRange;
	int atk = 149;
	
	public Player(int index, Bitmap[] imgs, float x, float y) {
		super(index, imgs, x, y);
		maxRange =	(int)(GView.sWidth*0.65f);
	}
	
	
	public int Update(int direction) {
		mainImg = direction;
		switch(direction){
		case 0:
			if(onTouch){
				onTouch = false;
				direction = 1;
				aCurrentFrame[0] = 0;
			}
			break;
		case 1:
			if(getlastFrame()){
				direction = 0;
				aCurrentFrame[1] = 0;
			}
			break;
		}
		return direction;
	}
	
	public int getMaxRange() {
		return maxRange;
	}
	
	void setTouchTime(boolean touchChk){
		onTouch = touchChk;
		
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}
	public int getAtk() {
		return atk;
	}

}
