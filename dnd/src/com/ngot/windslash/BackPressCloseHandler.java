package com.ngot.windslash;

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
                "\'�ڷ�\'��ư�� �ѹ� �� �����ø� ����˴ϴ�.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
