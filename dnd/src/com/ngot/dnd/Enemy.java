package com.ngot.dnd;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Enemy extends Sprite {

	Random rnd = new Random();
	private int	lnr,speed;
	Rect hitbox = new Rect();
	
	public Enemy(Bitmap img) {
		super(img);
		speed = rnd.nextInt(5)+5;
		
	}

	public void Update() {
		
		imgX+=speed;
		hitbox.left = imgX-imgWidth;
		if(GView.sWidth/2>imgX||GView.sWidth<imgX){
			speed = -speed;
		}
	}
	
	@Override
	public void drawSprite(Canvas canvas, boolean bg) {
		// TODO Auto-generated method stub
		super.drawSprite(canvas, bg);
		makeHitbox();
	}
	void makeHitbox(){
		hitbox.left = imgX-imgWidth;
		hitbox.top = imgY;
		hitbox.right = hitbox.left+(imgWidth*2);
		hitbox.bottom = hitbox.top-imgHeight;
	}
}
