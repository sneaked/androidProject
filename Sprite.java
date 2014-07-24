package com.mrgame.spider;

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
	
	void setCanvas(Canvas canvas){
		sc = canvas;
	}
	
	void addBg(Bitmap img){
		img = Bitmap.createScaledBitmap(img, width, height, true);
		sc.drawBitmap(img,0,0,null);
	}
	
	void addBg(Bitmap img,Paint p){
		img = Bitmap.createScaledBitmap(img, width, height, true);
		sc.drawBitmap(img,0,0,p);
	}
	
	void addSprite(Bitmap img,int x,int y){
		sc.drawBitmap(img, x-img.getWidth()/2, y-img.getHeight()/2, null);
	}
	
	void addSprite(Bitmap img,int x,int y,Paint p){
		sc.drawBitmap(img, x-img.getWidth()/2, y-img.getHeight()/2, p);
	}
	
	void addArraySprite(ArrayList<GameObject> array){
		for(GameObject t:array){
			sc.drawBitmap(t.img, t.x-t.img.getWidth()/2, t.y-t.img.getHeight()/2,null);
		}
	}
	
	
}
