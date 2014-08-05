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
import android.graphics.BitmapFactory.Options;
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
		
		//������ ����
		final int FPS = 25;
		final int MAX_SKIP_FRAME = 5;
		final int FRAME_PERIOD = 1000/FPS;//�����ð����� (���ؽð�)
		long lastTime;
		long currentTime;//���� �ð�
		long timeGap;//����ð�
		long sleepTime;//��� ��� �ð�(fast..)
		int skipcnt;//��ŵȽ�� 
		boolean isRun = true,isWait = false;
		
		Bitmap tmp;
		Resources res = mContext.getResources();
		Paint paint = new Paint();
		Random rnd = new Random();
		long updateTime;
		SoundPool effectSound;
		int enemyLevel = 1,weaponLevel = 0;
		int gameLevel = 1;
		long startTime;
		int timeLimit = 180;//20��  200==25��
		float timeCnt = timeLimit;
		boolean startwave = true;
		int regenTime = 3000;//3000  test
		boolean onFire = false;
		int enemyHpLevel = 1;
		//----------���
		BackGround back;
		Bitmap imgBg[] = new Bitmap[1];
		//----------�������̽�
		Bitmap inter = scale(R.drawable.inter, sWidth, sHeight, 1);
		Bitmap imgGauge,imgExpGauge,imgHpGauge;
		Bitmap imgPanel0;
		Sprite panel0;
		Rect recPanel0;
		Bitmap imgButtons[] = new Bitmap[5];
		ArrayList<ImageButton> mImageButtons = new ArrayList<ImageButton>();
		Bitmap imgTimeGauge[] = new Bitmap[2];
		//----------ĳ����
		Player player;
		Bitmap imgPlayers[] = new Bitmap[2];
		int playerDirection = 0;
		float fireSpeed = 1600f,saveFireSpeed = fireSpeed;//bust = 300
		long lastfire,currentfire;
		double radian;
		int soundLevelup;
		int statPoint = 10;
		int bustMax = 50,bustCnt = 0,bustTime = 0,bustTimeMax = 8;
		boolean isBust = false;
		//----------����
		Bitmap imgWeapon[] = new Bitmap[2];
		ArrayList<Weapon> mWeapons = new ArrayList<Weapon>();
		int soundAtk;
		float despeed = 25f;
		//----------����Ʈ
		Bitmap imgEffect[] = new Bitmap[7];
		ArrayList<Effect> mEffects = new ArrayList<Effect>();
		//----------���ط�
		Bitmap imgFont[] = new Bitmap[10];
		ImageScore dmg;
		ArrayList<ImageScore> mDmgs = new ArrayList<ImageScore>();
		//----------����
		Bitmap imgEnemys[][] = new Bitmap[4][3];
		ArrayList<Enemy> mEnemies = new ArrayList<Enemy>();
		long emakeTime;
		//----------���ʽ�
		Bitmap imgBonus[] = new Bitmap[2];
		Bonus bonus;
		ArrayList<Bonus> mBonus = new ArrayList<Bonus>();
		long bmakeTime;
		
		public GameThread() {
			ground = (int)(sHeight*0.9f);
			makeImages();
			
			back = new BackGround(imgBg.length, imgBg, 0, 0);
			player = new Player(imgPlayers.length, imgPlayers, sWidth*0.2f,ground);
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
			soundAtk = effectSound.load(mContext, R.raw.weaponsound_0, 1);
			soundLevelup = effectSound.load(mContext, R.raw.levelup, 1);
			G.level = 0;
			G.wave = 0;
			startTime = System.currentTimeMillis();
			
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
				
			//����
				imgEnemys[3][0] = scale(R.drawable.boss0_0, sWidth/2, sWidth/2, 12);
				imgEnemys[3][1] = scale(R.drawable.boss0_0, sWidth/2, sWidth/2, 12);
				imgEnemys[3][2] = scale(R.drawable.boss0_1, sWidth/2, sWidth/2, 6);
				
				imgWeapon[0] = scale(R.drawable.weapon0, sWidth*0.17f, sWidth*0.17f, 5);
				imgWeapon[1] = scale(R.drawable.bust0, sWidth*0.25f, sWidth*0.25f, 3);
				
				for(int i =0;i<10;i++){
					imgFont[i] = BitmapFactory.decodeResource(res, R.drawable.number_0+i);
					imgFont[i] = Bitmap.createScaledBitmap(imgFont[i], (int)(sWidth*0.05f), (int)(sWidth*0.07f), true);
				}
				
				imgGauge = scale(R.drawable.expgauge_0, sWidth, sHeight/16, 1);
				imgExpGauge = scale(R.drawable.expgauge_1, sWidth, sHeight/16, 1);
				imgHpGauge = scale(R.drawable.hpgauge_1, sWidth, sHeight/16, 1);
				
				imgEffect[0] = scale(R.drawable.effect_0, sWidth*0.19f, sWidth*0.14f, 7);//�⺻���� ����Ʈ
				imgEffect[1] = scale(R.drawable.levelup0, sWidth/3, sWidth/3, 7);//������ ����
				imgEffect[2] = scale(R.drawable.levelup1, sWidth/3, sWidth/3, 9);//������ ����
				imgEffect[3] = scale(R.drawable.busteffect0, sWidth*0.19f,  sWidth*0.14f, 4);//b����Ʈ
				imgEffect[4] = scale(R.drawable.lightning0, sWidth*0.4f,  sWidth*0.5f, 6);//���� ����
				imgEffect[5] = scale(R.drawable.aura0, sWidth*0.6f,  sWidth*0.6f, 6);//aura
				imgEffect[6] = scale(R.drawable.next0, sWidth*0.7f,  sWidth*0.6f, 8);//next
				
				for(int i = 0;i<imgBonus.length;i++){
					imgBonus[i] = scale(R.drawable.bonus0_0+i, sWidth*0.15f, sWidth*0.15f, 4);
				}
				
				imgPanel0 = scale(R.drawable.panel0, sWidth*0.69f, sWidth*0.495f, 1);
				for(int i = 0;i<3;i++){
					imgButtons[i] = scale(R.drawable.point0+i, sWidth*0.15f, sHeight*0.29f, 2);
				}
				imgButtons[3] = scale(R.drawable.point3, (int)(imgPanel0.getWidth()*0.27f), imgButtons[2].getHeight()*0.95f, 2);
				imgButtons[4] = scale(R.drawable.point4, (int)(imgPanel0.getWidth()*0.227f), imgButtons[2].getHeight()*0.95f, 2);
				
				imgTimeGauge[0] = scale(R.drawable.timegauge_0, sWidth*0.6f, sHeight*0.1f, 1);
				imgTimeGauge[1] = scale(R.drawable.timegauge_1, sWidth*0.58f, sHeight*0.1f, 1);
			
		}
		
		void recycle(){
			tmp.recycle();
			tmp = null;
		//���
			for(int i=0;i<imgBg.length;i++){
				imgBg[i].recycle();
				imgBg[i] = null;
			}
		//�÷��̾�
			for(int i = 0;i<imgPlayers.length;i++){
				imgPlayers[i].recycle();
				imgPlayers[i] = null;
			}
		//����
			for(int i =0;i<imgWeapon.length;i++){
				imgWeapon[i].recycle();
				imgWeapon[i] = null;
			}
		//����
			for(int i = 0;i<imgEnemys.length;i++){
				for(int j = 0;j<imgEnemys[i].length;j++){
					imgEnemys[i][j].recycle();
					imgEnemys[i][j] = null;
				}
			}
		//��Ʈ
			for(int i =0;i<imgFont.length;i++){
				imgFont[i].recycle();
				imgFont[i] = null;
			}
		//����Ʈ
			for(int i =0;i<imgEffect.length;i++){
				imgEffect[i].recycle();
				imgEffect[i] = null;
			}
		//���ʽ�
			for(int i=0;i<imgBonus.length;i++){
				imgBonus[i].recycle();
				imgBonus[i] = null;
			}
		//�̹��� ��ư
			for(int i = 0;i<imgButtons.length;i++){
				imgButtons[i].recycle();
				imgButtons[i] = null;
			}
			for(int i=0;i<imgTimeGauge.length;i++){
				imgTimeGauge[i].recycle();
				imgTimeGauge[i] = null;
			}
		}
		
		void Update(){
			updateTime = System.currentTimeMillis();
			gameMgr();
			makeAll();
			moveAll();
			aniAll();
			onCollision();
		}
		
		void gameMgr(){
			if(mEnemies.size()==0){
				startwave = true;
			}
			if(startwave){
				if(updateTime-startTime>250){
					timeCnt-=0.25f;
					if(timeCnt<=0f){
						startTime = updateTime;
						timeCnt = timeLimit;
						startwave = false;
						gameLevel++;
						regenTime-=400;
						mEffects.add(new Effect(imgEffect.length, imgEffect, sWidth/2, sHeight*0.9f, 6));
						G.wave++;
						post(postLevel);
					}
				}
			}
			player.hp-=player.maxHp*0.0002f;
			if(gameLevel%5==0){
				regenTime = 3000;
				enemyLevel++;
				gameLevel++;
				if(enemyLevel>=4){
					enemyLevel = 3;
				}
			}
			
			if(gameLevel%8==0){
				enemyHpLevel++;
				mEnemies.add(new Enemy(imgEnemys.length, imgEnemys, sWidth+(sWidth*0.2f), sHeight/2, 3,enemyHpLevel));
				gameLevel++;
			}
			
			if(statPoint<=0){
				for(int i=0;i<3;i++){
					mImageButtons.get(i).setOn(false);
				}
			}else{
				for(int i=0;i<3;i++){
					mImageButtons.get(i).setOn(true);
				}
			}
			if(bustCnt==bustMax){
				mImageButtons.get(4).setOn(true);
			}
			if(isBust){//����Ʈ on
				bustTime++;
				if(bustTime>FPS*bustTimeMax){//10��������
					isBust = false;//����
					bustTime = 0;//����Ʈ�ð� �ʱ�ȭ
					weaponLevel = 0;//���ⷹ�� �ʱ�ȭ
					fireSpeed = saveFireSpeed;//����ӵ� �ʱ�ȭ
					player.setAtk(player.saveAtk);//���ݷ� �ʱ�ȭ
					for(Effect t:mEffects){
						if(t.getKind()==5){
							mEffects.remove(t);
							break;
						}
					}
				}
			}
		}
		
		void makeAll(){
	
			if(onFire){
				currentfire = System.currentTimeMillis();
				if(currentfire-lastfire>fireSpeed){
					player.setTouchTime(true);
					mWeapons.add(new Weapon(imgWeapon.length, imgWeapon, player.imgX, player.imgY,radian,player.imgX,weaponLevel,despeed));
					lastfire = currentfire;
					effectSound.play(soundAtk, 5, 5, 0, 0, 1);
				}
			}
			
			if(startwave){
				if(updateTime-emakeTime>regenTime){
					mEnemies.add(new Enemy(imgEnemys.length, imgEnemys, sWidth+(sWidth*0.2f), ground, rnd.nextInt(enemyLevel),enemyHpLevel));
					emakeTime = updateTime;
					
				}
				if(updateTime-bmakeTime>20000&&mBonus.size()==0){
					mBonus.add(new Bonus(imgBonus.length, imgBonus, sWidth+imgBonus[0].getWidth(), sHeight/2));
					bmakeTime = updateTime;
				}
			}
		}
		
		Runnable postLevel = new Runnable() {
			@Override
			public void run() {
				((MainActivity)mContext).text_level.setText(G.level+"");
				((MainActivity)mContext).text_wave.setText(G.wave+"");
			}
		};
		void statsUp(){
			statPoint+=3;
			if(isBust){
				saveFireSpeed-=13f;
			}else{
				fireSpeed-=13f;
			}
			
		}
		void moveAll(){
			playerDirection = player.Update(playerDirection);
			if(player.levelup()){
				mEffects.add(new Effect(imgEffect.length, imgEffect, player.imgX-player.imgWidth, player.imgY+(player.imgWidth/9), 1));
				mEffects.add(new Effect(imgEffect.length, imgEffect, player.imgX, player.imgY-(imgEffect[2].getHeight()*0.5f), 2));
				effectSound.play(soundLevelup, 1, 1, 0, 0, 1);
				G.level++;
				post(postLevel);
				statsUp();
			}
			for(Weapon t:mWeapons){
				t.Update();
			}
		//�̻���
			for(int i = mWeapons.size()-1;i>=0;i--){
				if(mWeapons.get(i).isDead){
					mWeapons.remove(i);
				}
			}
		//����,����
			for(int i = mEnemies.size()-1;i>=0;i--){
				mEnemies.get(i).Update(updateTime);
				if(mEnemies.get(i).isDead){
					if(player.level<player.maxLevel){
						player.exp+=mEnemies.get(i).getExp();
						//player.exp+=30000;//test
						player.hp+=player.maxHp*0.05f;
					}
					mEnemies.remove(i);
				}else if(mEnemies.get(i).isOut){
					mEnemies.remove(i);
				}
			}
		//������
			for(int i=mDmgs.size()-1;i>=0;i--){
				if(mDmgs.get(i).moveReturn()){
					mDmgs.remove(i);
				}
			}
		//����Ʈ
			for(int i=mEffects.size()-1;i>=0;i--){
				if(mEffects.get(i).moveReturn()&&mEffects.get(i).getKind()!=5){
					mEffects.remove(i);				}
			}
		//���ʽ�
			for(int i=mBonus.size()-1;i>=0;i--){
				mBonus.get(i).Update(updateTime);
				if(mBonus.get(i).isDead){
					player.exp+=mBonus.get(i).getExp();
					mBonus.remove(i);
				}else if(mBonus.get(i).isOut){
					mBonus.remove(i);
				}
			}
		//����
			for(Enemy t:mEnemies){
				if(t.getKind()==3&&t.getBossAttack()){
					t.setBossAttack(false);
					mEffects.add(new Effect(imgEffect.length, imgEffect, player.imgX, player.imgY, 4));
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
		//�׸���
			
			back.drawSprite(canvas, true);
			for(Enemy t:mEnemies){
				t.drawSprite(canvas, false);
			}
			for(Effect t:mEffects){
				if(t.getKind()==5){
					t.drawSprite(canvas, false);
					break;
				}
			}
			for(Bonus t:mBonus){
				t.drawSprite(canvas, false);
			}
			for(Weapon t:mWeapons){
				t.drawSprite(canvas, false);
			}
			player.drawSprite(canvas, false);
			for(Effect t:mEffects){
				if(t.getKind()!=5){
					t.drawSprite(canvas, false);
				}
			}
			for(ImageScore t:mDmgs){
				canvas.drawBitmap(t.img, t.x, t.y, null);
			}
			
			canvas.drawBitmap(inter, 0, sHeight, null);
		//������
			canvas.drawBitmap(imgGauge, 0, sHeight, null);
			canvas.save();
			canvas.scale(player.hp/player.maxHp, 1);
			canvas.drawBitmap(imgHpGauge, 0, sHeight, null);
			canvas.restore();
			canvas.save();
			canvas.drawBitmap(imgGauge, 0, sHeight+imgExpGauge.getHeight(), null);
			canvas.scale(player.exp/player.maxExp, 1);
			canvas.drawBitmap(imgExpGauge, 0, sHeight+imgExpGauge.getHeight(), null);
			canvas.restore();
		//�ð�
			canvas.drawBitmap(imgTimeGauge[0], 0, sHeight*0.12f, null);
			canvas.save();
			canvas.scale(timeCnt/timeLimit, 1);
			canvas.drawBitmap(imgTimeGauge[1], 0, sHeight*0.12f, null);
			canvas.restore();
		//panel0
			panel0.drawSprite(canvas, true);
			for(ImageButton t:mImageButtons){
				t.drawSprite(canvas);
			}
			canvas.drawText("BUST"+bustCnt+"/"+bustMax, sWidth/2, sHeight/2, paint);
			canvas.drawText("stat"+statPoint, sWidth/2, sHeight/3, paint);
			
		}
		
		void bustUp(){
			if(bustCnt<bustMax&&isBust==false){
				bustCnt++;
			}else if(bustCnt>=bustMax){
				bustCnt = bustMax;
			}
		}
		
		void onCollision(){
			for(Weapon t:mWeapons){
				for(Enemy t1:mEnemies){
					if(t.imgX>t1.imgX-t1.imgWidth&&t.imgX<t1.imgX+t1.imgWidth&&t.imgY+(t.imgHeight/2)>t1.imgY){
						if(t1.getKind()!=3){//�Ϲ�enemy
							t.setHit();
							if(t.getHit()){
								t1.setHit(true,player.getAtk());
								mDmgs.add(new ImageScore(imgFont, t1.imgX, t1.imgY-t1.imgHeight, player.getAtk()));
								mEffects.add(new Effect(imgEffect.length, imgEffect, t.imgX, t.imgY,(weaponLevel==0)?0:3));
								bustUp();
								break;
							}
						}else if(t1.getKind()==3&&t1.getDirection()==1){//������ �������϶�
							t1.setHit(true,player.getAtk());
							mDmgs.add(new ImageScore(imgFont, t1.imgX, t1.imgY-t1.imgHeight, player.getAtk()));
							mEffects.add(new Effect(imgEffect.length, imgEffect, t.imgX, t.imgY,(weaponLevel==0)?0:3));
							bustUp();
							break;
						}
						
					}
				}
				for(Bonus bt:mBonus){
					if(t.imgX>bt.imgX-bt.imgWidth&&t.imgX<bt.imgX+bt.imgWidth&&t.imgY<bt.imgY+(bt.imgHeight/2)){
						t.setHit();
						if(t.getHit()){
							bt.setHit(true);
							mEffects.add(new Effect(imgEffect.length, imgEffect, t.imgX, t.imgY,(weaponLevel==0)?0:3));
							break;
						}
						
					}

				}
			}
			for(Enemy t:mEnemies){
				if(t.imgX-t.aimgWidth[0]<player.imgX+player.aimgWidth[0]){
					if(t.setAttack(true)){
						player.decreaseHp(t.getAtk());
						break;
					}
				}
			}
			for(Effect t:mEffects){
				if(t.getKind()==4&&t.getlastFrame()){
					mDmgs.add(new ImageScore(imgFont, player.imgX, player.imgY, (int)(player.maxHp*0.1f)));
					player.decreaseHp((int)(player.maxHp*0.1f));
					break;
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
					case 0://����-3
						statPoint--;
						if(isBust){
							saveFireSpeed-=8;
						}else{
							fireSpeed -=8;
						}
						break;
					case 1:
						statPoint--;
						despeed+=0.5f;
						break;
					case 2:
						statPoint-=6;
						bustTimeMax++;
						break;
					case 3:
						
						break;
					case 4:
						weaponLevel = 1;
						saveFireSpeed = fireSpeed;
						fireSpeed = 200;
						player.setAtk((int)(player.getAtk()*1.5f));
						isBust = true;
						bustCnt = 0;
						t.setOn(false);
						mEffects.add(new Effect(imgEffect.length, imgEffect, player.imgX, player.imgY, 5));
						break;
					}
				}
			}
		}
		void setdown(int x,int y){
			downx = x;
			downy = y;
			calCharRadian(x,y);
			if(recPanel0.contains(downx, downy))
				onFire = true;
			
			btnCheck(x,y);
		}
		
		void setmove(int x,int y){
			movex = x;
			movey = y;
			calCharRadian(x,y);
		}
		
		void setup(int x,int y){
			upx = x;
			upy = y;
			onFire = false;
			for(ImageButton t:mImageButtons){
				t.btnUp();
			}
		}
		
		void adjustFPS(){
			currentTime = System.currentTimeMillis();
			timeGap = currentTime-lastTime;
			lastTime = currentTime;
			sleepTime = FRAME_PERIOD - timeGap;
			if(sleepTime>0){//fast...
				try {
					GameThread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			skipcnt=0;
			while(sleepTime<0&&skipcnt<MAX_SKIP_FRAME){
				Update();
				sleepTime+=FRAME_PERIOD;
				skipcnt++;
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
						adjustFPS();
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
			tmp = BitmapFactory.decodeResource(res, id);
			return tmp;
		}
		
		Bitmap scale(int id,float width,float height,int iframes){
			tmp = BitmapFactory.decodeResource(res, id);
			tmp = Bitmap.createScaledBitmap(tmp, (int)(width*iframes), (int)height, true);
			return tmp;
		}
		
		
		
	}

}









