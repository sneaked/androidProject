package com.ngot.dnd;

import android.graphics.Bitmap;


public class Player extends Sprite {

	int dx = 5;
	
	public Player(Bitmap img) {
		super(img);
	}
	
	
	@Override
	public void Update() {
		imgX+=dx;
		if(imgX<imgWidth||imgX>GThread.sWidth){
			dx = -dx;
			imgX+=dx;
		}
	}
}
