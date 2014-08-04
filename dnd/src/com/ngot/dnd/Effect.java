package com.ngot.dnd;

import android.graphics.Bitmap;

public class Effect extends Sprite{

	
	public Effect(int index, Bitmap[] imgs, float x, float y,int kind) {
		super(index, imgs, x, y);
		mainImg = kind;
		initAnimation(0, 21, 7);
		initAnimation(1, 7, 7);
		initAnimation(2, 12, 9);
		initAnimation(3, 16, 4);
	}

	public boolean moveReturn(){
		if(getlastFrame()){
			return true;
		}
		return false;
	}
}
