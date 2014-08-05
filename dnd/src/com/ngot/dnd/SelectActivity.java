package com.ngot.dnd;

import java.util.Timer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SelectActivity extends Activity {

	Timer timer = new Timer(); // 이 안에 true값을 주면 액티비티종료와 상관없이
	// 데이터를 지우지 않는다

	ImageView img_character1, img_character2, img_character3;
	Bitmap zangief2;
	boolean touch = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charactor_select);

		img_character1 = (ImageView) findViewById(R.id.mihune);
		Animation ani1 = AnimationUtils.loadAnimation(SelectActivity.this,
				R.anim.appear_character);
		ani1.setInterpolator(this,
				android.R.anim.accelerate_decelerate_interpolator);
		img_character1.startAnimation(ani1);

		img_character2 = (ImageView) findViewById(R.id.zangief);
		Animation ani2 = AnimationUtils.loadAnimation(SelectActivity.this,
				R.anim.appear_character2);
		ani2.setInterpolator(this,
				android.R.anim.accelerate_decelerate_interpolator);
		img_character2.startAnimation(ani2);

		img_character3 = (ImageView) findViewById(R.id.pocketball);
		img_character3.startAnimation(ani1);

		zangief2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.zangief2);
	}

	public void monclick(View v) {
		switch (v.getId()) {
		case R.id.mihune:
			Intent intent = new Intent(SelectActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.zangief:
			touch = !touch;
			img_character2.setImageResource(R.drawable.zangief
					+ ((touch) ? 1 : 0));
			break;
		}
	}
}
