package com.ngot.dnd;

import android.graphics.Bitmap;

public class Zombie extends Sprite {

	public Zombie(Bitmap img) {
		super(img);
		
	}

	public void Update(int px,boolean pState) {
		if(!pState){
			if(px<imgX){
				imgX-=2;
			}else{
				imgX+=2;
			}
		}
	}
}
