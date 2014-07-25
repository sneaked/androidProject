package com.mrgame.crush;


import android.content.Context;
import android.os.Handler;
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
	Handler handler;
	public GameView(Context context, AttributeSet attrs) {
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
		mWidth = getWidth();
		mHeight = getHeight();
		if(mThread == null){
			mThread = new GameThread(mContext, mHolder, mWidth, mHeight,handler);
			mThread.start();
		}else{
			mThread.pauseThread(false);//true->∏ÿ√„
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
		mThread = new GameThread(mContext, mHolder, mWidth, mHeight,handler);
		mThread.start();
	}
	
	void setHandler(Handler handler){
		this.handler = handler;
	}
	
}
