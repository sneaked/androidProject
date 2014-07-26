package com.ngot.blockgame;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Callback {

	SurfaceHolder mHolder;
	Context mContext;
	GameThread mThread;
	int mWidth,mHeight;
	static int B_width;
	static int B_height;
	static int M_left;
	static int M_top;
	
	final int LEFT = 1;
	final int RIGHT = 2;
	final int STOP = 3;
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mContext = context;
		//setFocusable(true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mWidth = getWidth();
		mHeight = getHeight();
		if(mThread == null){
			mThread = new GameThread(mContext, mHolder, mWidth, mHeight);
			mThread.start();
		}else{
			mThread.pauseThread(false);//true->¸ØÃã
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = (int)event.getAction();
		int x = (int)event.getX();
		int y = (int)event.getY();

		switch(action){
		case MotionEvent.ACTION_DOWN:
			mThread.shootBall();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return true;
	}

	void stopGame(){
		mThread.stopThread();
	}
	void pauseGame(){
		mThread.pauseThread(true);
	}
	void resumeGame(){
		mThread.pauseThread(false);
	}
	void restartGame(){
		mThread.stopThread();
		mThread = null;
		mThread = new GameThread(mContext, mHolder, mWidth, mHeight);
		mThread.start();
	}


}
