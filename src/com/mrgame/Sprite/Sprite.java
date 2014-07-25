package com.mrgame.Sprite;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Sprite {
	
	Canvas sc;
	int imgX,imgY;
	int width,height;
	public Sprite(int width,int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setCanvas(Canvas canvas){
		sc = canvas;
	}
	
	public void addBg(Bitmap img){
		img = Bitmap.createScaledBitmap(img, width, height, true);
		sc.drawBitmap(img,0,0,null);
	}
	
	public void addBg(Bitmap img,Paint p){
		img = Bitmap.createScaledBitmap(img, width, height, true);
		sc.drawBitmap(img,0,0,p);
	}
	
	public void addSprite(Bitmap img,int x,int y){
		sc.drawBitmap(img, x-img.getWidth()/2, y-img.getHeight()/2, null);
	}
	
	public void addSprite(Bitmap img,int x,int y,Paint p){
		sc.drawBitmap(img, x-img.getWidth()/2, y-img.getHeight()/2, p);
	}
	
	public void addArraySprite(ArrayList<GameObject> array){
		for(GameObject t:array){
			sc.drawBitmap(t.img, t.x-t.img.getWidth()/2, t.y-t.img.getHeight()/2,null);
		}
	}
	
	
}
