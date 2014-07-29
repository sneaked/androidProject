package com.ngot.dnd;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.view.SurfaceHolder;


public class GThread extends Thread {
	static int sWidth,sHeight;
	
	Context mContext;
	SurfaceHolder mHolder;
	boolean isRun = true,isWait = false;

	//private int ground;
	static int ground;
	Sprite imgBack;
	Player player;
	int playerDirection = 0;
	Enemy enemy,enemy2;
	ArrayList<Sprite> mEnemys = new ArrayList<Sprite>();
	boolean left=false, right=false, up = false, down = false;
	
	Paint paint = new Paint();
	public GThread(Context c,SurfaceHolder holder,int width,int heght) {
		mContext = c;	
		mHolder = holder;	
		sWidth = width;	
		sHeight = heght;
		ground = (int)(heght*0.9f);
		//객체생성
		imgBack = new Sprite(decode(R.drawable.stage_1));
		player = new Player(3,sWidth*0.2f, ground);
		enemy = new Enemy(decode(R.drawable.zombie));
		enemy2 = new Enemy(decode(R.drawable.zombie));
		paint.setColor(Color.RED);
		paint.setTextSize(30);
		Start();
	}
	
	void Start(){
		//이미지 초기화
		imgBack.initSprite(0, 0, sWidth, sHeight);
		player.initAnimation(0,decode(R.drawable.player),sWidth*0.1f,sWidth*0.1f, 16, 8);
		player.initAnimation(1, decode(R.drawable.playeridle), sWidth*0.12f, sWidth*0.12f, 30, 14);
		player.initAnimation(2, decode(R.drawable.playeratk), sWidth*0.15f, sWidth*0.15f, 20, 10);
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
		tmp = BitmapFactory.decodeResource(mContext.getResources(), id);
		return tmp;
	}
	
}
