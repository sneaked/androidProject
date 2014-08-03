package com.ngot.dnd;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	int imgX,imgY;
	int imgWidth,imgHeight;
	Bitmap mImg;
	Bitmap aImgs[];
	
	//ani
	private Rect mRect;//1프레임 영역
	private int mFps;//초당 프레임
	protected int miFrames;//프레임 개수
	
	protected int mCurrentFrame;//최근 프레임
	protected int mSpriteWidth;//프레임 크기
	protected int mSpriteHeight;
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
	boolean lastFrame = false;
	

	
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
		aimgHeight = new int[index];
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
	public Sprite(int index,Bitmap[] imgs,float x,float y,int kind){
		
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
		mainImg = kind;
	}
	public Sprite(int index,Bitmap[][] imgs,float x,float y,int kind){
		
		imgX = (int)x;
		imgY = (int)y;
		aImgs = imgs[kind];
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
	
	public void aniUpdate(long thisTime){
		if(ani==false){
			mUpdateTime = thisTime;
			if(thisTime>mFrameTimer+mFps){
				mFrameTimer = thisTime;
				mCurrentFrame += 1;
				if(mCurrentFrame>=miFrames){
					mCurrentFrame = 0;
				}
			}
			mRect.left = mCurrentFrame*mSpriteWidth;
			mRect.right = mRect.left+mSpriteWidth;
		}else{
			aUpdateTime[mainImg] = thisTime;
			if(thisTime>aFrameTimer[mainImg]+aFps[mainImg]){
				aFrameTimer[mainImg] = thisTime;
				aCurrentFrame[mainImg] += 1;
				if(aCurrentFrame[mainImg]>=aiFrames[mainImg]){
					aCurrentFrame[mainImg] = 0;
				}
				if(aCurrentFrame[mainImg]>=aiFrames[mainImg]-1){
					lastFrame = true;
				}else{
					lastFrame = false;
				}
			}
			aRect[mainImg].left = aCurrentFrame[mainImg]*aSpriteWidth[mainImg];
			aRect[mainImg].right = aRect[mainImg].left+aSpriteWidth[mainImg];
		}
		
	}
	Rect dest;
	public void drawSprite(Canvas canvas,boolean bg){
		if(ani==false){
			if(bg){
				canvas.drawBitmap(mImg, imgX, imgY, null);
			}else{
				canvas.drawBitmap(mImg, imgX-imgWidth, imgY-imgHeight, null);
			}
		}else{
			if(bg){
				mImg = aImgs[mainImg];
				imgWidth = aimgWidth[mainImg];
				imgHeight = aimgHeight[mainImg];
				mSpriteWidth = aSpriteWidth[mainImg];
				mSpriteHeight = aSpriteHeight[mainImg];
				mRect = aRect[mainImg];
				dest = null;
				dest = new Rect(imgX, imgY,(imgX+aSpriteWidth[mainImg]),(imgY+aSpriteHeight[mainImg]));
				canvas.drawBitmap(mImg, mRect, dest, null);
			}else{
				mImg = aImgs[mainImg];
				imgWidth = aimgWidth[mainImg];
				imgHeight = aimgHeight[mainImg];
				mSpriteWidth = aSpriteWidth[mainImg];
				mSpriteHeight = aSpriteHeight[mainImg];
				mRect = aRect[mainImg];
				dest = null;
				dest = new Rect(imgX-imgWidth, imgY-imgHeight,(imgX+mSpriteWidth)-imgWidth,(imgY+mSpriteHeight)-imgHeight);
				canvas.drawBitmap(mImg, mRect, dest, null);
			}
		}
	}

	public boolean getlastFrame(){
		return lastFrame;
	}
	
	
	
}















