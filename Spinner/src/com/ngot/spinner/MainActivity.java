package com.ngot.spinner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void mOnclick(View v){
		switch(v.getId()){
		case R.id.btn_start:
			Intent intent = new Intent(this, GameActivity.class);
			startActivity(intent);
			break;
		}
	}
}
