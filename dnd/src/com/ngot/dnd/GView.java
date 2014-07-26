package com.ngot.dnd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GView extends SurfaceView implements Callback {

	Context mContext;
	SurfaceHolder mHolder;
	GThread mThread;
	int sWidth,sHeight;
	
	public GView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mContext = context;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		sWidth = getWidth(); sHeight = getHeight();
		if(mThread==null){
			mThread = new GThread(mContext, mHolder, sWidth, sHeight);
			mThread.start();
		}else{
			mThread.pauseThread(false);
		}
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		
		return true;
	}
	
	void gameExit(){
		mThread.stopThread();
	}
	
	void gamePause(){
		mThread.pauseThread(true);
	}
	
	void gameResume(){
		mThread.pauseThread(false);
	}
	

}









