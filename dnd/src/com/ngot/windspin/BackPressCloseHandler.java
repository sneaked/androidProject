package com.ngot.windspin;

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {
	private long backKeyPressedTime = 0;
    private Toast toast;
 
    private Activity activity;
    private GView gView;
    
    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }
    
    public BackPressCloseHandler(Activity context,GView gameView) {
        this.activity = context;
        this.gView = gameView;
    }
 
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
        	gView.gameExit();
            activity.finish();
            toast.cancel();
        }
    }
 
    public void showGuide() {
        toast = Toast.makeText(activity,
                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
