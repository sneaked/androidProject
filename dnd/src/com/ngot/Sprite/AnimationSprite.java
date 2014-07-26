package com.ngot.Sprite;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class AnimationSprite {

	private Rect mRect;//1프레임 영역
	private int mFps;//초당 프레임
	private int miFrames;//프레임 개수
	
	Bitmap img;
	
	private int mCurrentFrame;//최근 프레임
	private int mSpriteWidht;//프레임 크기
	private int mSpriteHeight;
	private int mFrameTimer;
	
	public AnimationSprite(Bitmap bitmap) {
		img = bitmap;
		mRect = new Rect(0, 0, 0, 0);
		mFrameTimer = 0;
		mCurrentFrame = 0;
	}
	
	public void initSpriteData(int width,int height,int fps,int iFrame){
		mSpriteWidht = width;
		mSpriteHeight = height;
		mFps = fps;
		miFrames = iFrame;
		
		mFps = 1000/fps;
		miFrames = iFrame;
	}
}













































