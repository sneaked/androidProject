package com.ngot.dnd;
import android.graphics.Bitmap;



public class GameMap extends Sprite {

	public GameMap(Bitmap img) {
		super(img);
	}

	
	public void Update(int px) {
		if(px>GThread.sWidth*0.8){
			imgX-=5;
		}
		
		if(px<GThread.sWidth*0.2){
			imgX+=5;
		}
	}
	
}
