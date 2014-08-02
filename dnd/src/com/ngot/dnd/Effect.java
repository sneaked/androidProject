package com.ngot.dnd;

import android.graphics.Bitmap;

public class Effect extends Sprite{

	
	public Effect(int index, Bitmap[] imgs, float x, float y) {
		super(index, imgs, x, y);
		initAnimation(0, 21, 7);
	}

	public boolean moveReturn(){
		if(getlastFrame()){
			return true;
		}
		return false;
	}
}
