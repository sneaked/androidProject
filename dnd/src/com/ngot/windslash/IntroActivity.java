package com.ngot.windslash;

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
	Timer timer = new Timer(); // �� �ȿ� true���� �ָ� ��Ƽ��Ƽ����� �������

	// �����͸� ������ �ʴ´�

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		logo = (ImageView) findViewById(R.id.img_logo);
		// �ִϸ��̼���ƿ�� ��Ʈ�����丮�� ����� ���
		Animation ani = AnimationUtils.loadAnimation(this, R.anim.appear_logo);
		logo.startAnimation(ani);
		// �������� ������ ��� ����
		timer.schedule(task, 2000);
	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			Intent intent = new Intent(IntroActivity.this, StartActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	public void onBackPressed() {
		timer.cancel();
		finish();
	};
}
