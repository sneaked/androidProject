package com.ngot.dnd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
	}
	
	public void mOnclick(View v){
		switch(v.getId()){
		case R.id.btn_start:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;
		}
	}
}
