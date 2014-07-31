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
	static int inHeight;
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
			sHeight = height/2;
			inHeight = height;
			mThread = new GameThread();
			mThread.start();
		}else{
			gamePause(false);
		}
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//gamePause(true);
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
		
		Resources res = mContext.getResources();
		boolean isRun = true,isWait = false;
		Paint paint = new Paint();
		
		//----------배경
		Sprite imgBack;
		BackGround back;
		Bitmap imgBg[] = new Bitmap[1];
		//----------인터페이스
		Bitmap inter = scale(R.drawable.inter, sWidth, sHeight, 1);
		
		//----------캐릭터
		Player player;
		Bitmap imgPlayers[] = new Bitmap[2];
		int playerDirection = 0;
		boolean fire = false;
		long lastfire,currentfire;
		//----------미사일
		Bitmap imgMissile[] = new Bitmap[1];
		
		ArrayList<Missile> mMissiles = new ArrayList<Missile>();
		//----------적군
		Enemy e1,e2;
		Bitmap imgEnemys[][] = new Bitmap[2][3];
		ArrayList<Enemy> mEnemies = new ArrayList<Enemy>();
		
		public GameThread() {
			ground = (int)(sHeight*0.9f);
			makeImages();
			
			back = new BackGround(1, imgBg, 0, 0);
			back.initAnimation(0, 8, 4);
			
			imgBack = new Sprite(decode(R.drawable.stage_2));
			imgBack.initSprite(0, 0, sWidth, sHeight);

			player = new Player(2, imgPlayers, sWidth/7,ground);
			player.initAnimation(0, 13, 8);
			player.initAnimation(1, 16, 8);
			
			e1 = new Enemy(3, imgEnemys, sWidth, ground, 0);
			e2 = new Enemy(3, imgEnemys, sWidth, ground, 1);
			
			mEnemies.add(e1);
			mEnemies.add(e2);
			
			paint.setColor(Color.RED);
			paint.setTextSize(30);
			
		}
		
		void makeImages(){
			imgBg[0] = scale(R.drawable.stage_3, sWidth, sHeight*1.1f, 4);
			
			imgPlayers[0] = scale(R.drawable.samurai_0, sWidth/5, sWidth/5, 8);
			imgPlayers[1] = scale(R.drawable.samurai_1, sWidth/5, sWidth/5, 8);
			
			int n[] = {4,8,4};
			for(int i=0;i<3;i++){
				imgEnemys[0][i] = scale(R.drawable.enemy0_0+i, sWidth/8, sWidth/8, n[i]);
			}
			n[1] = 9;
			for(int i=0;i<3;i++){
				imgEnemys[1][i] = scale(R.drawable.enemy1_0+i, sWidth/5, sWidth/5, n[i]);
			}
		
			
			imgMissile[0] = scale(R.drawable.missile0_0, sWidth*0.1f, sWidth*0.1f, 7);
		}
		
		void recycle(){
			for(int i = 0;i<2;i++){
				imgPlayers[i].recycle();
			}
			for(int i =0;i<1;i++){
				imgMissile[i].recycle();
			}
			for(int i = 0;i<2;i++){
				for(int j = 0;j<3;j++){
					imgEnemys[i][j].recycle();
				}
			}
		}
		
		void Update(){
			makeAll();
			moveAll();
			aniAll();
			onCollision();
		}
		
		
		void makeAll(){
		
			if(playerDirection==0&&fire){
				fire = false;
				mMissiles.add(new Missile(1, imgMissile, player.imgX, player.imgY));
			}
			
		}
		
		void moveAll(){
			long thisTime = System.currentTimeMillis();
			playerDirection = player.Update(playerDirection);
			for(Missile t:mMissiles){
				t.Update();
			}
			for(int i = mMissiles.size()-1;i>=0;i--){
				if(mMissiles.get(i).isDead){
					mMissiles.remove(i);
				}
			}
			
			for(int i = mEnemies.size()-1;i>=0;i--){
				mEnemies.get(i).Update(thisTime);
				if(mEnemies.get(i).isDead){
					mEnemies.remove(i);
				}
			}
			
		}
		
		void aniAll(){
			long thisTime = System.currentTimeMillis();
			for(Missile t:mMissiles){
				t.aniUpdate(thisTime);
			}
			player.aniUpdate(thisTime);
			for(Enemy t:mEnemies){
				t.aniUpdate(thisTime);
			}
			back.aniUpdate(thisTime);
		}
		
		
		
		void drawSprite(Canvas canvas){
			//그리기
			
			back.drawSprite(canvas, true);
			//imgBack.drawSprite(canvas, true);
			player.drawSprite(canvas, false);
			canvas.drawText(mMissiles.size()+"", sWidth/2,sHeight/3, paint);
			for(Enemy t:mEnemies){
				t.drawSprite(canvas, false);
			}
			for(Missile t:mMissiles){
				t.drawSprite(canvas, false);
			}
			for(Enemy t:mEnemies){
				canvas.drawText(t.isHit+"", sWidth/2, sHeight/2, paint);
			}
			canvas.drawBitmap(inter, 0, sHeight, null);

		}
		void onCollision(){
			for(Missile t:mMissiles){
				for(Enemy t1:mEnemies){
					if(t.imgX>t1.imgX&&t.imgX<t1.imgX+t1.imgWidth){
						t.setHit(true);
						t1.setHit(true);
						break;
					}
				}
			}
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
				currentfire = System.currentTimeMillis();
				if(currentfire-lastfire>300){
					player.setTouchTime(true);
					fire = true;
					lastfire = currentfire;
				}
			}
		}
		
		
		@Override
		public void run() {
			Canvas canvas = null;
			while(isRun){
				canvas = mHolder.lockCanvas();
				try{
					synchronized (mHolder) {
						Update();
						//adjustFPS();
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
			recycle();
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









