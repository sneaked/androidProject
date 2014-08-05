package com.ngot.dnd;

import android.graphics.Bitmap;

public class Effect extends Sprite{

	int kind;
	
	public Effect(int index, Bitmap[] imgs, float x, float y,int kind) {
		super(index, imgs, x, y);
		mainImg = kind;
		this.kind = kind;
		switch(kind){
		case 0:
			initAnimation(0, 21, 7);
			break;
		case 1:
			initAnimation(1, 7, 7);
			break;
		case 2:
			initAnimation(2, 12, 9);
			break;
		case 3:
			initAnimation(3, 16, 4);
			break;
		case 4:
			initAnimation(4, 12, 6);
			break;
		case 5:
			initAnimation(5, 12, 6);
			break;
		case 6:
			initAnimation(6, 8, 8);
			break;
		}
		
		
	}

	
	public boolean moveReturn(){
		if(getlastFrame()){
			return true;
		}
		return false;
	}
	
	public int getKind() {
		return kind;
	}
}
