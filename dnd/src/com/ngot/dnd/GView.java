package com.ngot.dnd;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.drm.DrmStore.RightsStatus;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GView extends SurfaceView implements Callback {

	Context mContext;
	SurfaceHolder mHolder;
	GameThread mThread;
	static int sWidth,sHeight,ground;
	
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
		if(mThread==null){
			sWidth = width;
			sHeight = height;
			mThread = new GameThread();
			mThread.start();
		}else{
			gamePause(false);
		}
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		gameExit();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int action = event.getAction();
		int x = (int)event.getX();
		int y = (int)event.getY(); 
		switch(action){
		case MotionEvent.ACTION_DOWN:
			mThread.setdown(x,y);
			break;
		case MotionEvent.ACTION_MOVE:
			mThread.setmove(x,y);
			break;
		case MotionEvent.ACTION_UP:
			mThread.setup(x, y);
			break;
		}
		
		return true;
	}
	
	void gameExit(){
		mThread.stopThread();
	}
	
	void gamePause(boolean wait){
		mThread.pauseThread(wait);
	}
	
	
	
	class GameThread extends Thread{
		
		boolean isRun = true,isWait = false;
		//private int ground;
		Sprite imgBack;
		Player player;
		int playerDirection = 0;
		Enemy enemy,enemy2;
		ArrayList<Sprite> mEnemys = new ArrayList<Sprite>();
		boolean left=false, right=false, up = false, down = false;
		
		Paint paint = new Paint();
		
		//test
		Resources res = mContext.getResources();
		Bitmap imgPlayers[] = new Bitmap[3];
		
		
		public GameThread() {
	
			//test
			imgPlayers[0] = scale(R.drawable.player_0, sWidth*0.25f, sWidth*0.25f, 14);
			imgPlayers[1] = scale(R.drawable.player_1, sWidth*0.25f, sWidth*0.25f, 9);
			imgPlayers[2] = scale(R.drawable.player_2, sWidth*0.25f, sWidth*0.25f, 20);
			
			
			ground = (int)(sHeight*0.9f);
			//객체생성
			imgBack = new Sprite(decode(R.drawable.stage_1));
			player = new Player(3,imgPlayers,sWidth*0.2f, ground);
			enemy = new Enemy(decode(R.drawable.zombie));
			enemy2 = new Enemy(decode(R.drawable.zombie));
			paint.setColor(Color.RED);
			paint.setTextSize(30);
			Start();
			
		}
		
		void Start(){
			//이미지 초기화
			player.initAnimation(0, 30, 14);
			player.initAnimation(1, 20, 9);
			player.initAnimation(2, 20, 20);
			imgBack.initSprite(0, 0, sWidth, sHeight);
			enemy.initSprite(sWidth*0.65f, ground, sWidth/4, sWidth/3);
			enemy2.initSprite(sWidth*0.6f, ground, sWidth/4, sWidth/3);
			mEnemys.add(enemy);
			mEnemys.add(enemy2);
			
		}
		void Update(){
			//업데이트
			long GameTime = System.currentTimeMillis();
			playerDirection = player.Update(playerDirection);
			player.aniUpdate(GameTime);
			enemy.Update();
			enemy2.Update();
		}
		void drawSprite(Canvas canvas){
			//그리기
			imgBack.drawSprite(canvas, true);
			enemy.drawSprite(canvas, false);
			enemy2.drawSprite(canvas, false);
			player.drawSprite(canvas, false);
		}
		
		//test
		int downx,downy;
		int movex,movey;
		int upx,upy;
		void setdown(int x,int y){
			downx = x;
			downy = y;
			
		}
		void setmove(int x,int y){
			movex = x;
			movey = y;
		}
		void setup(int x,int y){
			upx = x;
			upy = y;
			if(playerDirection==0){
				playerDirection = 1;
				int tmp = mEnemys.get(0).imgX;
				for(Sprite t: mEnemys){
					if(tmp>t.imgX){
						tmp = t.imgX;
					}
				}
				player.setEnemyx(tmp);
			}
			
			player.setTouchTime(true);
		}
		
		void resetTouch(){
				left = false;
				right = false;
				up = false;
				down = false;
		}
		//end test
		
		@Override
		public void run() {
			Canvas canvas = null;
			while(isRun){
				canvas = mHolder.lockCanvas();
				try{
					synchronized (mHolder) {
						Update();
						drawSprite(canvas);
					}
				}finally{
					if(canvas!=null){
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
			}//end while
		}//end run
		
			
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
		
		Bitmap decode(int id){
			Bitmap tmp;
			tmp = BitmapFactory.decodeResource(res, id);
			return tmp;
		}
		
		Bitmap scale(int id,float width,float height,int iframes){
			Bitmap tmp = BitmapFactory.decodeResource(res, id);
			tmp = Bitmap.createScaledBitmap(tmp, (int)(width*iframes), (int)height, true);
			return tmp;
		}
		
		
	}

}









