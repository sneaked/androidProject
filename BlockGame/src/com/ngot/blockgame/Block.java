package com.ngot.blockgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Block {
	
	public int x1,y1,x2,y2,score;
	public Bitmap imgBlock;
	
	public Block(Context c,float x,float y,float num) {
	
		x1 = (int)(GameView.M_left+x*GameView.B_width);
		y1 = (int)(GameView.M_top+y*GameView.B_height);
		
		x2 = x1+GameView.B_width;
		y2 = y1+GameView.B_height;
		score = (int)num+1*50;
		imgBlock = BitmapFactory.decodeResource(c.getResources(), R.drawable.k0+(int)num);
		imgBlock = Bitmap.createScaledBitmap(imgBlock, GameView.B_width, GameView.B_height,true	);
	
	}
}
