package com.ngot.dnd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	GView gView;
	static final int GAME_EXIT = 1;
	static final int GAME_PAUSE = 2;
	static final int GAME_RESUME = 3;
	
	BackPressCloseHandler backButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gView = (GView)findViewById(R.id.GView);
	
		backButton = new BackPressCloseHandler(this,gView);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, GAME_EXIT, 0, "EXIT");
		menu.add(0, GAME_PAUSE, 0, "PAUSE");
		menu.add(0, GAME_RESUME, 0, "RESUME");
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case GAME_EXIT:		
			Toast.makeText(this, "exit", Toast.LENGTH_SHORT).show();
			gView.gamePause();
			showDialog(GAME_EXIT);
			break;
		case GAME_PAUSE:
			Toast.makeText(this, "pause", Toast.LENGTH_SHORT).show();
			gView.gamePause();
			break;
		case GAME_RESUME:
			Toast.makeText(this, "resume", Toast.LENGTH_SHORT).show();
			gView.gameResume();
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case GAME_EXIT:
			dialog = new AlertDialog.Builder(this)
			.setTitle("GAME EXIT")
			.setMessage("EXIT?")
			.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					gView.gameExit();
					finish();
				}
			})
			.setNegativeButton("CANCEL", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					gView.gameResume();
				}
			}).create();
			break;
		}
		
		return dialog;
	}
	
	@Override
	public void onBackPressed() {
		backButton.onBackPressed();
	}
	
	@Override
	protected void onPause() {
		gView.gamePause();
		super.onPause();
	}
}












