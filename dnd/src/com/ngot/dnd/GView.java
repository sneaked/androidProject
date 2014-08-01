package com.ngot.dnd;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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
		Random rnd = new Random();
		long updateTime,makeTime;
		MediaPlayer bgm;
		SoundPool effectSound;
		int effectid;
		//----------���
		BackGround back;
		Bitmap imgBg[] = new Bitmap[1];
		//----------�������̽�
		Bitmap inter = scale(R.drawable.inter, sWidth, sHeight, 1);
		Bitmap imgGauge,imgExpGauge,imgHpGauge;
		//----------ĳ����
		Player player;
		Bitmap imgPlayers[] = new Bitmap[2];
		int playerDirection = 0;
		boolean fire = false;
		int fireSpeed = 180;
		long lastfire,currentfire;
		double radian;
		//----------����
		Bitmap imgWeapon[] = new Bitmap[1];
		ArrayList<Weapon> mWeapons = new ArrayList<Weapon>();
		//----------���ط�
		Bitmap imgFont[] = new Bitmap[10];
		ImageScore dmg;
		ArrayList<ImageScore> mDmgs = new ArrayList<ImageScore>();
		//----------����
		Bitmap imgEnemys[][] = new Bitmap[2][3];
		ArrayList<Enemy> mEnemies = new ArrayList<Enemy>();
		
		public GameThread() {
			ground = (int)(sHeight*0.9f);
			makeImages();
			
			back = new BackGround(1, imgBg, 0, 0);
			back.initAnimation(0, 8, 4);

			player = new Player(2, imgPlayers, sWidth/7,ground);
			player.initAnimation(0, 13, 8);
			player.initAnimation(1, 16, 8);

			paint.setColor(Color.RED);
			paint.setTextSize(30);
			
			effectSound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
			effectid = effectSound.load(mContext, R.raw.weaponsound_0, 1);
			
		}
		
		void makeImages(){
			imgBg[0] = scale(R.drawable.stage_3, sWidth, sHeight*1.1f, 4);
			
			imgPlayers[0] = scale(R.drawable.samurai_0, sWidth/5, sWidth/5, 8);
			imgPlayers[1] = scale(R.drawable.samurai_1, sWidth/4.5f, sWidth/5, 8);
			
			int n[] = {4,8,4};
			for(int i=0;i<3;i++){
				imgEnemys[0][i] = scale(R.drawable.enemy0_0+i, sWidth/8, sWidth/8, n[i]);
			}
			n[1] = 9;
			for(int i=0;i<3;i++){
				imgEnemys[1][i] = scale(R.drawable.enemy1_0+i, sWidth/5, sWidth/5, n[i]);
			}
			
			imgWeapon[0] = scale(R.drawable.weapon0, sWidth*0.17f, sWidth*0.17f, 5);
			for(int i =0;i<10;i++){
				imgFont[i] = BitmapFactory.decodeResource(res, R.drawable.number_0+i);
				imgFont[i] = Bitmap.createScaledBitmap(imgFont[i], (int)(sWidth*0.05f), (int)(sWidth*0.07f), true);
			}
			
			imgGauge = scale(R.drawable.expgauge_0, sWidth, sHeight/16, 1);
			imgExpGauge = scale(R.drawable.expgauge_1, sWidth, sHeight/16, 1);
			imgHpGauge = scale(R.drawable.hpgauge_1, sWidth, sHeight/16, 1);
		}
		
		void recycle(){
			for(int i = 0;i<2;i++){
				imgPlayers[i].recycle();
			}
			for(int i =0;i<1;i++){
				imgWeapon[i].recycle();
			}
			for(int i = 0;i<2;i++){
				for(int j = 0;j<3;j++){
					imgEnemys[i][j].recycle();
				}
			}
			for(int i =0;i<10;i++){
				imgFont[i].recycle();
				imgFont[i] = null;
			}
		}
		
		void Update(){
			updateTime = System.currentTimeMillis();
			makeAll();
			moveAll();
			aniAll();
			onCollision();
		}
		
		
		void makeAll(){
	
			if(playerDirection==0&&fire){
				fire = false;
				mWeapons.add(new Weapon(1, imgWeapon, player.imgX, player.imgY,radian,player.maxRange,player.imgX));
			}
			
			if(updateTime-makeTime>3000){
				mEnemies.add(new Enemy(3, imgEnemys, sWidth, ground, rnd.nextInt(2)));
				makeTime = updateTime;
				player.hp-=20;
			}
			
		}
		
		void moveAll(){
			playerDirection = player.Update(playerDirection);
			for(Weapon t:mWeapons){
				t.Update();
			}
		//�̻���
			for(int i = mWeapons.size()-1;i>=0;i--){
				if(mWeapons.get(i).isDead){
					mWeapons.remove(i);
				}
			}
			
			for(int i = mEnemies.size()-1;i>=0;i--){
				mEnemies.get(i).Update(updateTime);
				if(mEnemies.get(i).isDead){
					mEnemies.remove(i);
					player.exp+=300;
				}
			}
			for(int i=mDmgs.size()-1;i>=0;i--){
				if(mDmgs.get(i).moveReturn()){
					mDmgs.remove(i);
				}
			}
			
			
		}
		
		void aniAll(){
			for(Weapon t:mWeapons){
				t.aniUpdate(updateTime);
			}
			player.aniUpdate(updateTime);
			for(Enemy t:mEnemies){
				t.aniUpdate(updateTime);
			}
			back.aniUpdate(updateTime);
		}
		
		
		
		void drawSprite(Canvas canvas){
			//�׸���
			
			back.drawSprite(canvas, true);
			for(Enemy t:mEnemies){
				t.drawSprite(canvas, false);
			}
			for(Weapon t:mWeapons){
				t.drawSprite(canvas, false);
			}
			player.drawSprite(canvas, false);
			
			for(ImageScore t:mDmgs){
				canvas.drawBitmap(t.img, t.x, t.y, null);
			}
			
			for(Enemy t:mEnemies){
				canvas.drawText(t.isHit+"", sWidth/2, sHeight/2, paint);
			}
			canvas.drawBitmap(inter, 0, sHeight, null);
		//������
			canvas.drawBitmap(imgGauge, 0, sHeight, null);
			canvas.save();
			canvas.scale(player.hp/player.maxHp, 1);
			canvas.drawBitmap(imgHpGauge, 0, sHeight, null);
			canvas.restore();
			canvas.drawBitmap(imgGauge, 0, sHeight+imgExpGauge.getHeight(), null);
			canvas.scale(player.exp/player.maxExp, 1);
			canvas.drawBitmap(imgExpGauge, 0, sHeight+imgExpGauge.getHeight(), null);
			canvas.restore();
			
			canvas.drawText(downx+"/"+downy, 0, (int)(sHeight*0.45)+sHeight, paint);
			
		}
		void onCollision(){
			for(Weapon t:mWeapons){
				for(Enemy t1:mEnemies){
					if(t.imgX>t1.imgX-t1.imgWidth&&t.imgX<t1.imgX+t1.imgWidth){
						t.setHit();
						if(t.getHit()){
							t1.setHit(true,player.getAtk());
							mDmgs.add(new ImageScore(imgFont, t1.imgX, t1.imgY-t1.imgHeight, player.getAtk()));
						}
						break;
					}
				}
			}
		}
		
		
		
		//test
		int downx,downy;
		int movex,movey;
		int upx,upy;
		void calCharRadian(int x,int y){
			radian = (Math.atan2((int)(sHeight*0.45)+sHeight-y, x-0))*0.5f;
		}
		void setdown(int x,int y){
			downx = x;
			downy = y;
			calCharRadian(x,y);
			if(playerDirection==0){
				currentfire = System.currentTimeMillis();
				if(currentfire-lastfire>fireSpeed){
					player.setTouchTime(true);
					fire = true;
					lastfire = currentfire;
					effectSound.play(effectid, 5, 5, 0, 0, 1);
				}
			}
		}
		void setmove(int x,int y){
			movex = x;
			movey = y;
			
		}
		void setup(int x,int y){
			upx = x;
			upy = y;
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









