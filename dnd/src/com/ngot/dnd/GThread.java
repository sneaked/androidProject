package com.ngot.dnd;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.ngot.Sprite.Sprite;

public class GThread extends Thread {
	static int sWidth,sHeight;
	
	Context mContext;
	SurfaceHolder mHolder;
	boolean isRun = true,isWait = false;

	private int ground;
	Sprite imgBack;
	Sprite imgPlayer,imgZombie;
	Player player;
	
	public GThread(Context c,SurfaceHolder holder,int width,int heght) {
		mContext = c;	
		mHolder = holder;	
		sWidth = width;	
		sHeight = heght;
		ground = (int)(heght*0.7f);
		imgBack = new Sprite(decode(c.getResources(), R.drawable.bg));
		player = new Player(decode(c.getResources(), R.drawable.player_ani));
		imgZombie = new Sprite(decode(c.getResources(), R.drawable.zombie));
		Start();
	}
	
	void Start(){
		
		imgBack.initSprite(0, 0, sWidth, sHeight);
		player.initSprite(sWidth/2, ground , sWidth/2, sWidth/3);
		player.setAnimation(player.mImg.getWidth()/5, player.mImg.getHeight(), 6, 5);
		imgZombie.initSprite(sWidth/6*5, ground, sWidth/4, sWidth/3);
		
	}
	void Update(){
		long GameTime = System.currentTimeMillis();
		player.Update();
		player.aniUpdate(GameTime);
	}
	void drawSprite(Canvas canvas){
		imgBack.drawSprite(canvas, true);
		imgZombie.drawSprite(canvas, false);
		player.drawSprite(canvas, false);
	}
	@Override
	public void run() {
		Canvas canvas = null;
		while(isRun){
			canvas = mHolder.lockCanvas();
			try{
				synchronized (mHolder) {
					Update();
					drawSprite(canvas);
				}
			}finally{
				if(canvas!=null){
					mHolder.unlockCanvasAndPost(canvas);
				}
			}
			synchronized (this) {
				if(isWait){
					try{
						wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		}//end while
	}//end run
	
	
		
	void stopThread(){
		isRun = false;
		synchronized (this) {
			this.notify();
		}
	}
	
	void pauseThread(boolean wait){
		isWait = wait;
		synchronized (this) {
			this.notify();
		}
	}
	
	Bitmap decode(Resources resource,int id){
		Bitmap tmp;
		tmp = BitmapFactory.decodeResource(resource, id);
		return tmp;
	}
	
}
