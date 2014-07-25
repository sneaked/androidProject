package com.mrgame.crush;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;
import com.mrgame.Sprite.*;
public class GameThread extends Thread {

	
	Context mContext;
	SurfaceHolder mHolder;
	boolean isRun = true,isWait = false;
	int width,height;
	Random rnd = new Random();
	Sprite sp;
	boolean gameEnd = false;
	Handler handler;
	Bitmap imgBg;
	public GameThread(Context c,SurfaceHolder holder,int width,int height,Handler handler) {
		mContext = c;
		mHolder = holder;
		this.width = width;
		this.height = height;
		this.handler = handler;
		imgBg = BitmapFactory.decodeResource(c.getResources(), R.drawable.ic_launcher);
		
	}//constructer
	
	@Override
	public void run() {
		Canvas canvas = null;
		sp = new Sprite(width, height);
		
		while(isRun){
			canvas = mHolder.lockCanvas();
			sp.setCanvas(canvas);
			try{
				synchronized (mHolder) {
					drawSprite();
				}
			}finally{
				if(canvas != null){
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
		}//end While
	}//end Run
	
	void drawSprite(){
		sp.addBg(imgBg);
	}
	
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
	
	/*void chkEnd(){
		if(gameEnd){
			pauseThread(true);
			handler.sendEmptyMessage(score);
		}
	}*/
}















