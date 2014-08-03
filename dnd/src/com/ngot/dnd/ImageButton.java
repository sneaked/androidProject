package com.ngot.dnd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class ImageButton{

	Rect dest,mRect;
	int imgX,imgY,imgWidth,imgHeight;
	Bitmap mImg;
	int kind;
	public ImageButton(Bitmap[] imgs, float x, float y, int kind) {
		this.kind = kind;
		mImg = imgs[kind];
		imgX = (int)x;
		imgY = (int)y;
		imgWidth = mImg.getWidth();
		imgHeight = mImg.getHeight();
		dest = new Rect(imgX, imgY,imgX+imgWidth ,imgY+imgHeight);
		mRect = new Rect(0, 0,imgWidth/2 ,imgHeight);
	}

	public void drawSprite(Canvas canvas){
		canvas.drawBitmap(mImg, mRect, dest, null);
	}
	
	public boolean btnCilck(int x,int y){
		if(dest.contains(x, y)){
			mRect.left = imgWidth/2;
			mRect.right = imgWidth;
			return true;
		}
		return false;
	}
	
	public void btnUp(){
		mRect.left = 0;
		mRect.right = imgWidth/2;
	}
	
	public int getKind() {
		return kind;
	}
}
