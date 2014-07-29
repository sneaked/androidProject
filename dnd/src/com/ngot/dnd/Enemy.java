package com.ngot.dnd;

import java.util.Random;

import android.graphics.Bitmap;

public class Enemy extends Sprite {

	Random rnd = new Random();
	private int	lnr,speed;
	
	public Enemy(Bitmap img) {
		super(img);
		speed = rnd.nextInt(5)+5;
	}

	public void Update() {
		
		imgX+=speed;
		if(GThread.sWidth/2>imgX||GThread.sWidth<imgX){
			speed = -speed;
		}
		
		
	}
}
