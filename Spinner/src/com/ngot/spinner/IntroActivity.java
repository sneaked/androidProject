package com.ngot.spinner;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroActivity extends Activity {

	ImageView logo;
	Timer timer = new Timer(); // 이 안에 true값을 주면 액티비티종료와 상관없이

	// 데이터를 지우지 않는다

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		logo = (ImageView) findViewById(R.id.img_logo);
		// 애니메이션유틸이 비트맵팩토리와 비슷한 기능
		Animation ani = AnimationUtils.loadAnimation(this, R.anim.appear_logo);
		logo.startAnimation(ani);
		// 스케쥴은 여러개 사용 가능
		timer.schedule(task, 8000);
	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent intent = new Intent(IntroActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			
		}
	};
}
