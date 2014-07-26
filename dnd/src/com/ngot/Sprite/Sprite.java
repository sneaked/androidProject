package com.ngot.Sprite;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Sprite {
	int imgX,imgY;
	int imgWidth,imgHeight;
	Bitmap mImg;
	public Sprite(Bitmap img) {
		mImg = img;
	}

	public void initSprite(int x,int y,int width,int height){
		mImg = Bitmap.createScaledBitmap(mImg, width, height, true);
		imgX = x;
		imgY = y;
		imgWidth = mImg.getWidth()/2;
		imgHeight = mImg.getHeight()/2;
		
	}
	
	
	public void drawSprite(Canvas canvas,boolean bg){
		if(bg){
			canvas.drawBitmap(mImg, imgX, imgY, null);
		}else{
			canvas.drawBitmap(mImg, imgX-imgWidth, imgY-imgHeight, null);
		}
	}

	
	public void Update(){
		
	}
	
	
}
