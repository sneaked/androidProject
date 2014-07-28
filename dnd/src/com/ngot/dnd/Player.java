package com.ngot.dnd;

import android.graphics.Bitmap;


public class Player extends Sprite {

	int speed = 5;
	boolean sneak = false;
	
	public Player(Bitmap img) {
		super(img);
	}
	
	public void Update(boolean left,boolean right,boolean up,boolean down) {
		if(left){
			imgX-=speed;
			sneak = true;
		}else{
			sneak = false;
		}
		if(right){
			imgX+=speed;
		}
		if(up){
			imgY-=speed;
		}
		if(down){
			imgY+=speed;
		}
		if(imgX<imgWidth){
			imgX = imgWidth;
		}
		if(imgX>GThread.sWidth){
			imgX = GThread.sWidth;
		}
	}
	
}
