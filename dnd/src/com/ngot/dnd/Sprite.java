package com.ngot.dnd;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	int imgX,imgY;
	int imgWidth,imgHeight;
	Bitmap mImg;
	

	private Rect mRect;//1프레임 영역
	private int mFps;//초당 프레임
	private int miFrames;//프레임 개수
	
	private int mCurrentFrame;//최근 프레임
	private int mSpriteWidth;//프레임 크기
	private int mSpriteHeight;
	private long mFrameTimer;
	long mUpdateTime;
	
	boolean ani = false;
	
	public Sprite(Bitmap img) {
		mImg = img;
		
	}

	public void initSprite(float x,float y,float width,float height){
		mImg = Bitmap.createScaledBitmap(mImg, (int)width, (int)height, true);
		imgX = (int)x;
		imgY = (int)y;
		imgWidth = mImg.getWidth()/2;
		imgHeight = mImg.getHeight()/2;
		
		
	}
	public void initSprite(float x,float y){
		imgX = (int)x;
		imgY = (int)y;
		imgWidth = mImg.getWidth()/2;
		imgHeight = mImg.getHeight()/2;
	}
	
	public void setAnimation(int width,int height,int fps,int iFrame){
		mRect = new Rect(0, 0, 0, 0);
		mFrameTimer = 0;
		mCurrentFrame = 0;
		mSpriteWidth = width;
		mSpriteHeight = height;
		mFps = fps;
		miFrames = iFrame;
		mRect.bottom = mSpriteHeight;
		mRect.right = mSpriteWidth;
		mFps = 1000/fps;
		miFrames = iFrame;
		ani = true;
		imgWidth = mImg.getWidth()/iFrame;
	}


	public void drawSprite(Canvas canvas,boolean bg){
		if(ani==false){
			if(bg){
				canvas.drawBitmap(mImg, imgX, imgY, null);
			}else{
				canvas.drawBitmap(mImg, imgX-imgWidth, imgY-imgHeight, null);
			}
		}else{
			Rect dest = new Rect(imgX-imgWidth, imgY-imgHeight,(imgX+mSpriteWidth)-imgWidth,(imgY+mSpriteHeight)-imgHeight);
			canvas.drawBitmap(mImg, mRect, dest, null);
		}
	}

	
	public void Update(){
		
		
	}
	
	public void aniUpdate(long GameTime){
		mUpdateTime = GameTime;
		if(GameTime>mFrameTimer+mFps){
			mFrameTimer = GameTime;
			mCurrentFrame += 1;
			if(mCurrentFrame>=miFrames){
				mCurrentFrame = 0;
			}
		}
		mRect.left = mCurrentFrame*mSpriteWidth;
		mRect.right = mRect.left+mSpriteWidth;
		
		
	}
	
}















