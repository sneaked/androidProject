package com.ngot.dnd;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	int imgX,imgY;
	int imgWidth,imgHeight;
	Bitmap mImg;
	Bitmap aImgs[];
	
	//1ani
	private Rect mRect;//1프레임 영역
	private int mFps;//초당 프레임
	protected int miFrames;//프레임 개수
	
	protected int mCurrentFrame;//최근 프레임
	private int mSpriteWidth;//프레임 크기
	private int mSpriteHeight;
	private long mFrameTimer;
	long mUpdateTime;
	
	//arrayani
	int[] aimgWidth,aimgHeight;
	private Rect[] aRect;//1프레임 영역
	private int[] aFps;//초당 프레임
	protected int[] aiFrames;//프레임 개수
	
	protected int[] aCurrentFrame;//최근 프레임
	private int[] aSpriteWidth;//프레임 크기
	protected int[] aSpriteHeight;
	private long[] aFrameTimer;
	long[] aUpdateTime;
	
	boolean ani = false;
	int mainImg = 0;
	
	
	public Sprite(Bitmap img) {
		mImg = img;
	}
	
	public Sprite(Bitmap img,float x, float y) {
		mImg = img;
		imgX = (int)x;
		imgY = (int)y;
	}
	
	public Sprite(int index,float x,float y){
		
		imgX = (int)x;
		imgY = (int)y;
		aImgs = new Bitmap[index];
		aimgWidth = new int[index];
		aimgHeight = new int[index];;
		aRect = new Rect[index];//1프레임 영역
		aFps = new int[index];//초당 프레임
		aiFrames = new int[index];//프레임 개수
		
		aCurrentFrame = new int[index];//최근 프레임
		aSpriteWidth = new int[index];//프레임 크기
		aSpriteHeight = new int[index];
		aFrameTimer = new long[index];
		aUpdateTime = new long[index];
	}
	public Sprite(int index,Bitmap[] imgs,float x,float y){
		
		imgX = (int)x;
		imgY = (int)y;
		aImgs = imgs;
		aimgWidth = new int[index];
		aimgHeight = new int[index];;
		aRect = new Rect[index];//1프레임 영역
		aFps = new int[index];//초당 프레임
		aiFrames = new int[index];//프레임 개수
		
		aCurrentFrame = new int[index];//최근 프레임
		aSpriteWidth = new int[index];//프레임 크기
		aSpriteHeight = new int[index];
		aFrameTimer = new long[index];
		aUpdateTime = new long[index];
	}

	public void initSprite(float x,float y,float width,float height){
		mImg = Bitmap.createScaledBitmap(mImg, (int)width, (int)height, true);
		imgX = (int)x;
		imgY = (int)y;
		imgWidth = mImg.getWidth()/2;
		imgHeight = mImg.getHeight();
		
	}
	public void initSprite(float x,float y){
		imgX = (int)x;
		imgY = (int)y;
		imgWidth = mImg.getWidth()/2;
		imgHeight = mImg.getHeight();
	}
	
	
	/*public void initAnimation(float x,float y,float width,float height,int fps,int iFrame){
		mImg = Bitmap.createScaledBitmap(mImg, (int)(width*iFrame), (int)height, true);
		mRect = new Rect(0, 0, 0, 0);
		mFrameTimer = 0;
		mCurrentFrame = 0;
		mSpriteWidth = (int)width;
		mSpriteHeight = (int)height;
		mFps = fps;
		miFrames = iFrame;
		mRect.bottom = mSpriteHeight;
		mRect.right = mSpriteWidth;
		mFps = 1000/fps;
		miFrames = iFrame;
		ani = true;
		imgWidth = mSpriteWidth/2;
		imgHeight = mSpriteHeight/2;
		imgX = (int)x;
		imgY = (int)y;
	}*/
	public void initAnimation(int index,Bitmap img,float width,float height,int fps,int iFrame){
		ani = true;
		aImgs[index] = img;
		aImgs[index] = Bitmap.createScaledBitmap(aImgs[index], (int)(width*iFrame), (int)height, true);
		aRect[index] = new Rect(0, 0, 0, 0);
		aFrameTimer[index] = 0;
		aCurrentFrame[index] = 0;
		aSpriteWidth[index] = (int)width;
		aSpriteHeight[index] = (int)height;
		//aFps[index] = fps;
		aiFrames[index] = iFrame;
		aRect[index].bottom = aSpriteWidth[index];
		aRect[index].right = aSpriteHeight[index];
		aFps[index] = 1000/fps;
		aimgWidth[index] = aSpriteWidth[index]/2;
		aimgHeight[index] = aSpriteHeight[index];
		
	}

	public void initAnimation(int index,int fps,int iFrame){
		ani = true;
		aRect[index] = new Rect(0, 0, 0, 0);
		aFrameTimer[index] = 0;
		aCurrentFrame[index] = 0;
		aSpriteWidth[index] = aImgs[index].getWidth()/iFrame;
		aSpriteHeight[index] =  aImgs[index].getHeight();
		aiFrames[index] = iFrame;
		aRect[index].bottom = aSpriteWidth[index];
		aRect[index].right = aSpriteHeight[index];
		aFps[index] = 1000/fps;
		aimgWidth[index] = aSpriteWidth[index]/2;
		aimgHeight[index] = aSpriteHeight[index];
		
	}
	
	public void Update(){
		
		
	}
	
	public void aniUpdate(long GameTime){
		if(ani==false){
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
		}else{
			aUpdateTime[mainImg] = GameTime;
			if(GameTime>aFrameTimer[mainImg]+aFps[mainImg]){
				aFrameTimer[mainImg] = GameTime;
				aCurrentFrame[mainImg] += 1;
				if(aCurrentFrame[mainImg]>=aiFrames[mainImg]){
					aCurrentFrame[mainImg] = 0;
				}
			}
			aRect[mainImg].left = aCurrentFrame[mainImg]*aSpriteWidth[mainImg];
			aRect[mainImg].right = aRect[mainImg].left+aSpriteWidth[mainImg];
		}
		
	}

	public void drawSprite(Canvas canvas,boolean bg){
		if(ani==false){
			if(bg){
				canvas.drawBitmap(mImg, imgX, imgY, null);
			}else{
				canvas.drawBitmap(mImg, imgX-imgWidth, imgY-imgHeight, null);
			}
		}else{
			mImg = aImgs[mainImg];
			imgWidth = aimgWidth[mainImg];
			imgHeight = aimgHeight[mainImg];
			mSpriteWidth = aSpriteWidth[mainImg];
			mSpriteHeight = aSpriteHeight[mainImg];
			mRect = aRect[mainImg];
			Rect dest = new Rect(imgX-imgWidth, imgY-imgHeight,(imgX+mSpriteWidth)-imgWidth,(imgY+mSpriteHeight)-imgHeight);
			canvas.drawBitmap(mImg, mRect, dest, null);
		}
	}

	
	
	
	
}















