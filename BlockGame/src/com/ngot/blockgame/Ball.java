package com.ngot.blockgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Ball {
	public int x,y,bw,bh;
	public int sx,sy;
	public boolean isMove = false;
	public Bitmap imgBall;
	
	private int width,height;
	
	public Ball(Context c,int _x,int _y,int _width,int _height) {
		x = _x; y = _y; width = _width; height = _height;
		sx=3;
		sy=-4;
		imgBall = BitmapFactory.decodeResource(c.getResources(), R.drawable.ball);
		bw = imgBall.getWidth()/2;
		bh = bw;
	}
	
	public boolean Move(){
		if(!isMove){
			return true;
		}
		x+=sx;
		if(x<bw||x>width-bw){
			sx = -sx;
			x +=sx;
		}
		y+= sy;
		if(y>=height+bh){
			return false;
			
		}
		if(y<bh){
			sy = -sy;
			y+=sy;
		}
		return true;
		
	}
}




























