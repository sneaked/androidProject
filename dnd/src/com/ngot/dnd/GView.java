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
import android.graphics.Rect;
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
		long updateTime;
		MediaPlayer bgm;
		SoundPool effectSound;
		int effectid;
		int stageLevel = 1,weaponLevel = 0;
		//----------배경
		BackGround back;
		Bitmap imgBg[] = new Bitmap[1];
		//----------인터페이스
		Bitmap inter = scale(R.drawable.inter, sWidth, sHeight, 1);
		Bitmap imgGauge,imgExpGauge,imgHpGauge;
		Bitmap imgPanel0;
		Sprite panel0;
		Rect recPanel0;
		Bitmap imgButtons[] = new Bitmap[5];
		ArrayList<ImageButton> mImageButtons = new ArrayList<ImageButton>();
		//----------캐릭터
		Player player;
		Bitmap imgPlayers[] = new Bitmap[2];
		int playerDirection = 0;
		boolean fire = false;
		int fireSpeed = 800,saveFireSpeed = fireSpeed;
		long lastfire,currentfire;
		double radian;
		//----------무기
		Bitmap imgWeapon[] = new Bitmap[2];
		ArrayList<Weapon> mWeapons = new ArrayList<Weapon>();
		
		//----------이펙트
		Bitmap imgEffect[] = new Bitmap[2];
		ArrayList<Effect> mEffects = new ArrayList<Effect>();
		//----------피해량
		Bitmap imgFont[] = new Bitmap[10];
		ImageScore dmg;
		ArrayList<ImageScore> mDmgs = new ArrayList<ImageScore>();
		//----------적군
		Bitmap imgEnemys[][] = new Bitmap[3][3];
		ArrayList<Enemy> mEnemies = new ArrayList<Enemy>();
		long emakeTime;
		//----------보너스
		Bitmap imgBonus[] = new Bitmap[2];
		Bonus bonus;
		ArrayList<Bonus> mBonus = new ArrayList<Bonus>();
		long bmakeTime;
		
		public GameThread() {
			ground = (int)(sHeight*0.9f);
			makeImages();
			
			back = new BackGround(1, imgBg, 0, 0);
			back.initAnimation(0, 8, 4);

			player = new Player(2, imgPlayers, sWidth*0.2f,ground);
			player.initAnimation(0, 13, 8);
			player.initAnimation(1, 16, 8);

			panel0 = new Sprite(imgPanel0, 0, sHeight+(imgExpGauge.getHeight()*2)+imgExpGauge.getHeight()*0.23f);
			recPanel0 = new Rect(panel0.imgX, panel0.imgY, panel0.imgX+panel0.mImg.getWidth(), panel0.imgY+panel0.mImg.getHeight());
			
			mImageButtons.add(new ImageButton(imgButtons, sWidth*0.7f, sHeight+(sHeight*0.13f), 0));
			mImageButtons.add(new ImageButton(imgButtons, sWidth*0.7f, sHeight+(sHeight*0.425f), 1));
			mImageButtons.add(new ImageButton(imgButtons, sWidth*0.7f, sHeight+(sHeight*0.71f), 2));
			mImageButtons.add(new ImageButton(imgButtons, 0, sHeight+(sHeight*0.735f), 3));
			mImageButtons.add(new ImageButton(imgButtons, sWidth*0.38f, sHeight+(sHeight*0.735f), 4));
			
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
			
			imgEnemys[2][0] = scale(R.drawable.enemy2_0, sWidth*0.3f, sWidth*0.3f, 6);
			imgEnemys[2][1] = scale(R.drawable.enemy2_1, sWidth*0.3f, sWidth*0.3f, 15);
			imgEnemys[2][2] = scale(R.drawable.enemy2_2, sWidth*0.3f, sWidth*0.3f, 6);
			
			imgWeapon[0] = scale(R.drawable.weapon0, sWidth*0.17f, sWidth*0.17f, 5);
			imgWeapon[1] = scale(R.drawable.bust0, sWidth*0.25f, sWidth*0.25f, 3);
			
			for(int i =0;i<10;i++){
				imgFont[i] = BitmapFactory.decodeResource(res, R.drawable.number_0+i);
				imgFont[i] = Bitmap.createScaledBitmap(imgFont[i], (int)(sWidth*0.05f), (int)(sWidth*0.07f), true);
			}
			
			imgGauge = scale(R.drawable.expgauge_0, sWidth, sHeight/16, 1);
			imgExpGauge = scale(R.drawable.expgauge_1, sWidth, sHeight/16, 1);
			imgHpGauge = scale(R.drawable.hpgauge_1, sWidth, sHeight/16, 1);
			
			
			imgEffect[0] = scale(R.drawable.effect_0, sWidth*0.19f, sWidth*0.14f, 7);
			imgEffect[1] = scale(R.drawable.levelup, sWidth/3, sWidth/3, 7);
			
			for(int i = 0;i<imgBonus.length;i++){
				imgBonus[i] = scale(R.drawable.bonus0_0+i, sWidth/3, sWidth/3, 4);
			}
			
			imgPanel0 = scale(R.drawable.panel0, sWidth*0.69f, sWidth*0.495f, 1);
			for(int i = 0;i<3;i++){
				imgButtons[i] = scale(R.drawable.point0+i, sWidth*0.15f, sHeight*0.29f, 2);
			}
			imgButtons[3] = scale(R.drawable.point3, (int)(imgPanel0.getWidth()*0.27f), imgButtons[2].getHeight()*0.95f, 2);
			imgButtons[4] = scale(R.drawable.point4, (int)(imgPanel0.getWidth()*0.227f), imgButtons[2].getHeight()*0.95f, 2);
		}
		
		void recycle(){
		//플레이어
			for(int i = 0;i<imgPlayers.length;i++){
				imgPlayers[i].recycle();
				imgPlayers[i] = null;
			}
		//무기
			for(int i =0;i<imgWeapon.length;i++){
				imgWeapon[i].recycle();
				imgWeapon[i] = null;
			}
		//적군
			for(int i = 0;i<imgEnemys.length;i++){
				for(int j = 0;j<imgEnemys[i].length;j++){
					imgEnemys[i][j].recycle();
					imgEnemys[i][j] = null;
				}
			}
		//폰트
			for(int i =0;i<imgFont.length;i++){
				imgFont[i].recycle();
				imgFont[i] = null;
			}
		//이펙트
			for(int i =0;i<imgEffect.length;i++){
				imgEffect[i].recycle();
				imgEffect[i] = null;
			}
		//보너스
			for(int i=0;i<imgBonus.length;i++){
				imgBonus[i].recycle();
				imgBonus[i] = null;
			}
		//이미지 버튼
			for(int i = 0;i<imgButtons.length;i++){
				imgButtons[i].recycle();
				imgButtons[i] = null;
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
				mWeapons.add(new Weapon(imgWeapon.length, imgWeapon, player.imgX, player.imgY,radian,player.imgX,weaponLevel));
			}
			
			if(updateTime-emakeTime>3000){
				mEnemies.add(new Enemy(3, imgEnemys, sWidth, ground, rnd.nextInt(stageLevel)));
				emakeTime = updateTime;
				player.hp-=player.maxHp*0.01f;
			}
			if(updateTime-bmakeTime>10000){
				mBonus.add(new Bonus(imgBonus.length, imgBonus, sWidth+imgBonus[0].getWidth(), sHeight/2));
				bmakeTime = updateTime;
			}
			
		}
		
		void moveAll(){
			playerDirection = player.Update(playerDirection);
			if(player.levelup()){
				mEffects.add(new Effect(imgEffect.length, imgEffect, player.imgX-player.imgWidth, player.imgY+(player.imgWidth/9), 1));
			}
			for(Weapon t:mWeapons){
				t.Update();
			}
		//미사일
			for(int i = mWeapons.size()-1;i>=0;i--){
				if(mWeapons.get(i).isDead){
					mWeapons.remove(i);
				}
			}
		//적군
			for(int i = mEnemies.size()-1;i>=0;i--){
				mEnemies.get(i).Update(updateTime);
				if(mEnemies.get(i).isDead){
					//player.exp+=mEnemies.get(i).getExp();
					player.exp+=30000;
					mEnemies.remove(i);
				}
			}
		//데미지
			for(int i=mDmgs.size()-1;i>=0;i--){
				if(mDmgs.get(i).moveReturn()){
					mDmgs.remove(i);
				}
			}
		//이펙트
			for(int i=mEffects.size()-1;i>=0;i--){
				if(mEffects.get(i).moveReturn()){
					mEffects.remove(i);				}
			}
		//보너스
			for(int i=mBonus.size()-1;i>=0;i--){
				mBonus.get(i).Update(updateTime);
				if(mBonus.get(i).isDead){
					player.exp+=mBonus.get(i).getExp();
					mBonus.remove(i);
				}else if(mBonus.get(i).isOut){
					mBonus.remove(i);
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
			for(Effect t:mEffects){
				t.aniUpdate(updateTime);
			}
			for(Bonus t:mBonus){
				t.aniUpdate(updateTime);
			}
		}
		
		
		
		void drawSprite(Canvas canvas){
		//그리기
			
			back.drawSprite(canvas, true);
			for(Enemy t:mEnemies){
				t.drawSprite(canvas, false);
			}
			for(Bonus t:mBonus){
				t.drawSprite(canvas, false);
			}
			for(Weapon t:mWeapons){
				t.drawSprite(canvas, false);
			}
			player.drawSprite(canvas, false);
			for(Effect t:mEffects){
				t.drawSprite(canvas, false);
			}
			for(ImageScore t:mDmgs){
				canvas.drawBitmap(t.img, t.x, t.y, null);
			}
			
			for(Enemy t:mEnemies){
				canvas.drawText(t.isHit+"", sWidth/2, sHeight/2, paint);
			}
			canvas.drawBitmap(inter, 0, sHeight, null);
		//게이지
			canvas.drawBitmap(imgGauge, 0, sHeight, null);
			canvas.save();
			canvas.scale(player.hp/player.maxHp, 1);
			canvas.drawBitmap(imgHpGauge, 0, sHeight, null);
			canvas.restore();
			canvas.drawBitmap(imgGauge, 0, sHeight+imgExpGauge.getHeight(), null);
			canvas.scale(player.exp/player.maxExp, 1);
			canvas.drawBitmap(imgExpGauge, 0, sHeight+imgExpGauge.getHeight(), null);
			canvas.restore();
		//panel0
			panel0.drawSprite(canvas, true);
			for(ImageButton t:mImageButtons){
				t.drawSprite(canvas);
			}
			canvas.drawText(mBonus.size()+"", 0, (int)(sHeight*0.5)+sHeight, paint);
			
		}
		void onCollision(){
			for(Weapon t:mWeapons){
				for(Enemy t1:mEnemies){
					if(t.imgX>t1.imgX-t1.imgWidth&&t.imgX<t1.imgX+t1.imgWidth&&t.imgY+(t.imgHeight/2)>t1.imgY){
						t.setHit();
						if(t.getHit()){
							t1.setHit(true,player.getAtk());
							mDmgs.add(new ImageScore(imgFont, t1.imgX, t1.imgY-t1.imgHeight, player.getAtk()));
							mEffects.add(new Effect(imgEffect.length, imgEffect, t.imgX, t.imgY,0));
						}
						
					}
				}
				for(Bonus bt:mBonus){
					if(t.imgX>bt.imgX-bt.imgWidth&&t.imgX<bt.imgX+bt.imgWidth&&t.imgY<bt.imgY+(bt.imgHeight/2)){
						t.setHit();
						if(t.getHit()){
							bt.setHit(true);
							mEffects.add(new Effect(imgEffect.length, imgEffect, t.imgX, t.imgY,0));
						}
						
					}

				}
			}
			for(Enemy t:mEnemies){
				if(t.imgX-t.aimgWidth[0]<player.imgX+player.aimgWidth[0]){
					if(t.setAttack(true))
						player.decreaseHp(t.getAtk());
				}
			}
		}
		
		
		
		//test
		int downx,downy;
		int movex,movey;
		int upx,upy;
		void calCharRadian(int x,int y){
			radian = (Math.atan2((int)(sHeight*0.5)+sHeight-y, x-0));
		}
		void btnCheck(int x,int y){
			for(ImageButton t:mImageButtons){
				if(t.btnCilck(x, y)){
					switch(t.getKind()){
					case 0:
						break;
					case 1:
						break;
					case 2:
						break;
					case 3:
						weaponLevel = 0;
						fireSpeed = saveFireSpeed;
						player.setAtk(player.saveAtk);
						break;
					case 4:
						weaponLevel = 1;
						fireSpeed = 300;
						player.setAtk((int)(player.getAtk()*1.5f));
						break;
					}
				}
			}
		}
		void setdown(int x,int y){
			downx = x;
			downy = y;
			calCharRadian(x,y);
			if(playerDirection==0&&recPanel0.contains(x, y)){
				currentfire = System.currentTimeMillis();
				if(currentfire-lastfire>fireSpeed){
					player.setTouchTime(true);
					fire = true;
					lastfire = currentfire;
					effectSound.play(effectid, 5, 5, 0, 0, 1);
				}
			}
			btnCheck(x,y);
		}
		
		void setmove(int x,int y){
			movex = x;
			movey = y;
			
		}
		void setup(int x,int y){
			upx = x;
			upy = y;
			for(ImageButton t:mImageButtons){
				t.btnUp();
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









