package com.ngot.dnd;

import android.graphics.Bitmap;

public class Player extends Sprite {

	
	boolean onTouch = false;
	
	public Player(int index, Bitmap[] imgs, float x, float y) {
		super(index, imgs, x, y);
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
	
	void setTouchTime(boolean touchChk){
		onTouch = touchChk;
		
	}

}
