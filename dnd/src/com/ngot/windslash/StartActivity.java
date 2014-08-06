package com.ngot.windslash;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends Activity {
	
	MediaPlayer mp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		mp = MediaPlayer.create(this, R.raw.soundback);
		mp.setVolume(0.5f, 0.5f);
		mp.setLooping(true);
		loadData();
	}
	
	public void mOnClick(View v) {
		switch (v.getId()) {
		case R.id.start_screen:
			Intent i = new Intent(this, SelectActivity.class);
			startActivity(i);
			break;
		} 
	}
	
	@Override
	protected void onResume() {
		if(mp!=null&&!mp.isPlaying()){
			mp.start();
		}
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		if(mp!=null){
			mp.stop();
			mp.release();
			mp = null;
		}
		saveData();
		super.onDestroy();
	}
	
	void loadData(){
		SharedPreferences pref = getSharedPreferences("save", MODE_PRIVATE);
		G.bestwave = pref.getInt("bestwave", 0);
	}
	
	void saveData(){
		SharedPreferences pref = getSharedPreferences("save", MODE_PRIVATE);
		SharedPreferences.Editor editer = pref.edit();
		editer.putInt("bestwave", G.bestwave);
		editer.commit();
	}
}








