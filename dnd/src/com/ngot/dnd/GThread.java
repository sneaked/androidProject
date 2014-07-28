package com.ngot.dnd;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.view.SurfaceHolder;


public class GThread extends Thread {
	static int sWidth,sHeight;
	
	Context mContext;
	SurfaceHolder mHolder;
	boolean isRun = true,isWait = false;

	private int ground;
	Sprite imgBack;
	Zombie zombie;
	Player player;
	GameMap map;
	boolean left=false, right=false, up = false, down = false;
	
	Paint paint = new Paint();
	public GThread(Context c,SurfaceHolder holder,int width,int heght) {
		mContext = c;	
		mHolder = holder;	
		sWidth = width;	
		sHeight = heght;
		ground = (int)(heght*0.75f);
		
		//객체생성
		imgBack = new Sprite(decode(c.getResources(), R.drawable.bg));
		player = new Player(decode(c.getResources(), R.drawable.player_ani));
		zombie = new Zombie(decode(c.getResources(), R.drawable.zombie));
		map = new GameMap(decode(c.getResources(), R.drawable.map));
		paint.setColor(Color.RED);
		paint.setTextSize(30);
		Start();
	}
	
	void Start(){
		//이미지 초기화
		imgBack.initSprite(0, 0, sWidth, sHeight);
		player.initSprite(sWidth/2, ground , sWidth/2, sWidth/3);
		player.setAnimation(player.mImg.getWidth()/5, player.mImg.getHeight(), 10, 5);
		zombie.initSprite(sWidth/6*5, sHeight/2, sWidth/2, sWidth/2);
		map.initSprite(0, sHeight-map.mImg.getHeight());
	}
	void Update(){
		
		//업데이트
		long GameTime = System.currentTimeMillis();
		player.Update(left, right, up, down);
		player.aniUpdate(GameTime);
		map.Update(player.imgX);
	}
	void drawSprite(Canvas canvas){
		//그리기
		imgBack.drawSprite(canvas, true);
		map.drawSprite(canvas, true);
		zombie.drawSprite(canvas, false);
		player.drawSprite(canvas, false);
		canvas.drawText(player.imgX+"/"+player.imgY, sWidth/2,sHeight/2, paint);
		canvas.drawText("down"+downx+"/"+downy, sWidth/2,sHeight/3, paint);
		canvas.drawText("move"+movex+"/"+movey, sWidth/2,sHeight/4, paint);
	}
	
	//test
	int downx,downy;
	int movex,movey;
	void setdown(int x,int y){
		downx = x;
		downy = y;
	}
	void setmove(int x,int y){
		movex = x;
		movey = y;
		if(downx-100>movex){
			left = true;
			right = false;
		}else if(downx+100<movex){
			right = true;
			left = false;
		}
		/*else if(downy-100>movey){
			up = true;
			down = false;
		}else if(downy+100<movey){
			down = true;
			up = false;
		}*/
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
	
	Bitmap decode(Resources resource,int id){
		Bitmap tmp;
		tmp = BitmapFactory.decodeResource(resource, id);
		return tmp;
	}
	
}
