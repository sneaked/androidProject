package com.ngot.blockgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Paddle {
	
	public int x,y,pw,ph;
	public Bitmap imgPdl;
	public int width;
	public int sx;
	public Paddle(Context c,int _x,int _y,int _width) {
		x = _x; y = _y; width = _width;
		pw = GameView.B_width*4/6;
		ph = GameView.B_width/6;
		
		imgPdl =BitmapFactory.decodeResource(c.getResources(), R.drawable.bar);
		imgPdl = Bitmap.createScaledBitmap(imgPdl, pw*2, ph*2, true);
	}
	
	public void move(){
		x += sx;
		if(x<pw||x>width-pw){
			sx = 0;
		}
	}
}
