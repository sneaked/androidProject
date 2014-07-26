package com.ngot.blockgame;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.ngot.Sprite.Sprite;

public class GameThread extends Thread {
	Context mContext;
	SurfaceHolder mHolder;
	boolean isRun = true,isWait = false;
	int width,height;
	Random rnd = new Random();
	Sprite sp;
	boolean gameEnd = false;
	
	final int STAGE_COUNTER = 2;
	int ballCnt =4;
	int stgNum = 0;
	int tot = 0;
	int score = 0;
	int sx[] = {-3,-2,2,3};
	Paint p = new Paint();
	Bitmap imgSball;
	
	ArrayList<Block> mBlocks = new ArrayList<Block>();
	Ball mBall;
	Paddle mPaddle;
	Bitmap bg;
	
	float Stage[][][]={
			{
				{0,0,0},{1,0,0},{2,0,0},{3,0,0},{4,0,0},
				{0,1,0},{1,1,1},{2,1,1},{3,1,1},{4,1,0},
				{0,2,0},{1,2,1},{2,2,2},{3,2,1},{4,2,0},
				{0,3,0},{1,3,1},{2,3,2},{3,3,1},{4,3,0},
				{0,4,0},{1,4,1},{2,4,1},{3,4,1},{4,4,0},
				{0,5,0},{1,5,0},{2,5,0},{3,5,0},{4,5,0}
				
			},
			{
				{1.5f,0,0},{2.5f,0,0},
				{1,1,0},{2,1,1},{3,1,0},
				{0.5f,2,0},{1.5f,2,1},{2.5f,2,1},{3.5f,2,0},
				{0,3,0},{1,3,1},{2,3,2},{3,3,1},{4,3,0},
				{0.5f,4,0},{1.5f,4,1},{2.5f,4,1},{3.5f,4,0},
				{1,5,0},{2,5,1},{3,5,1},
				{1.5f,6,0},{2.5f,6,0}
			}
			
	};
	
	public GameThread(Context c,SurfaceHolder holder,int width,int height) {
		mContext = c;
		mHolder = holder;
		this.width = width;
		this.height = height;
		
		initGame();
		makeStage();

	}//constructer

	public void initGame(){
		GameView.B_width = width/8;
		GameView.B_height = GameView.B_width/2;
		GameView.M_left = (width-GameView.B_width*5)/2;
		GameView.M_top = GameView.B_width * 4/5;
		
		mPaddle = new Paddle(mContext, width/2, height-GameView.B_height, width);
		mBall = new Ball(mContext, width/2, mPaddle.y-17, width, height);
		
		p.setAntiAlias(true);
		p.setColor(Color.WHITE);
		p.setTextSize(15);
		imgSball = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ball);
		imgSball = Bitmap.createScaledBitmap(imgSball, 10, 14, false);
		bg = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.bg); 
		
	}
	
	public void  makeStage(){
		for(int i = 0;i<Stage[stgNum].length;i++){
			mBlocks.add(new Block(mContext, Stage[stgNum][i][0], Stage[stgNum][i][1], Stage[stgNum][i][2]));
		}
		resetPosition();
		mBall.sy = -(4+stgNum);
		
	}
	
	public void resetPosition(){
		mPaddle.x = width/2;
		mPaddle.y = height - GameView.B_height;
		mPaddle.sx = 0;
		
		mBall.x = mPaddle.x;
		mBall.y = mPaddle.y -17;
		mBall.sy = -Math.abs(mBall.sy);
		mBall.isMove = false;
	}
	
	public void movePaddle(int direction){
		switch(direction){
		case 1:
			mPaddle.sx = -4;
			break;
		case 2:
			mPaddle.sx = 4;
			break;
		case 3:
			mPaddle.sx = 0;
			break;
		}
	}
	
	public void shootBall(){
		mBall.isMove = true;
	}
	
	public void moveBall(){
		mPaddle.move();
		if(mBall.isMove == false){
			mBall.x = mPaddle.x;
		}
		if(mBall.Move()==false){
			ballCnt--;
			if(ballCnt<0){
				gameOver();
				return;
			}
			resetPosition();
		}
	}
	
	public void onCollision(){
		if(mBlocks.size()==0){
			stgNum++;
			if(stgNum>=STAGE_COUNTER){
				stgNum = 0;
			}
			makeStage();
			return;
		}
		if(Math.abs(mBall.x-mPaddle.x)<=mPaddle.pw&&mBall.y>=(mPaddle.y-17)&&mBall.y<mPaddle.y){
			mBall.sx = sx[rnd.nextInt(4)];
			mBall.sy = -Math.abs(mBall.sy);
		}
		
		for(Block t:mBlocks){
			if(mBall.x+mBall.bw<t.x1||mBall.x+mBall.bw<t.x2
					||mBall.y+mBall.bw<t.y1||mBall.y+mBall.bw<t.y2){
				continue;
			}
			if(t.x1-mBall.x>=mBall.bw||mBall.x-t.x2>=mBall.bw){
				mBall.sx = -mBall.sx;
			}else{
				mBall.sy = -mBall.sy;
			}
			tot += t.score;
			score++;
			mBlocks.remove(t);
			break;
		}
	}
	
	void drawSprite(){
		
	}
	void drawCharacters(Canvas canvas){
		sp.addBg(bg);
		for(int i =0;i<=ballCnt;i++){
			canvas.drawBitmap(imgSball, i*12+5,height-20,null);
		}
		for(Block t:mBlocks){
			canvas.drawBitmap(t.imgBlock, t.x1, t.y1, null);
		}
			
		canvas.drawBitmap(mBall.imgBall, mBall.x-mBall.bw, mBall.y-mBall.bh,null);
	
		canvas.drawBitmap(mPaddle.imgPdl, mPaddle.x-mPaddle.pw,	mPaddle.y-mPaddle.ph,null);
		canvas.drawText("STAGE"+stgNum, 5, 18, p);
		canvas.drawText("BLOCK"+score, width/2-40, 18, p);
		canvas.drawText("Score"+tot, width-80, 18, p);
	
	}
	
	public void gameOver(){
		ballCnt = 4;
	}
	@Override
	public void run() {
		Canvas canvas = null;
		sp = new Sprite(width, height);

		while(isRun){
			canvas = mHolder.lockCanvas();
			sp.setCanvas(canvas);
			try{
				synchronized (mHolder) {
					moveBall();
					onCollision();
					drawCharacters(canvas);
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
			((MainActivity)mContext);
		}
	}*/
}
