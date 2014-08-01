package com.ngot.dnd;

import android.graphics.*;
import android.content.*;
public class ImageScore{

	int w,h;
	int life = 20;
	int x,y;
	Bitmap fonts[];
	Bitmap img;
	public ImageScore(Bitmap[] font,int x,int y,int score) {
		this.x = x;
		this.y = y;
		fonts = font;
		makeImage(score);
	}
	
	void makeImage(int score){
		int fw = fonts[0].getWidth();
		int fh = fonts[0].getHeight();
		String s = score+"";
		int len = s.length();
		
		img = Bitmap.createBitmap(fw*len, fh, fonts[0].getConfig());
		Canvas canvas = new Canvas(img);
		for(int i=0;i<len;i++){
			int n = s.charAt(i)-48;//('0' = 48) - 48 = 0
			canvas.drawBitmap(fonts[n],fw*i,0,null);
		}
		w = img.getWidth()/2;
		h = img.getHeight()/2;
	}
	
	boolean moveReturn(){
		y-=(h/6);
		life--;
		if(life<=0){
			img.recycle();
			return true;
		}
		return false;
	}
	
}
