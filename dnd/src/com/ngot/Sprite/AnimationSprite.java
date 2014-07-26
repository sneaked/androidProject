package com.ngot.Sprite;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class AnimationSprite {

	private Rect mRect;//1������ ����
	private int mFps;//�ʴ� ������
	private int miFrames;//������ ����
	
	Bitmap img;
	
	private int mCurrentFrame;//�ֱ� ������
	private int mSpriteWidht;//������ ũ��
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













































