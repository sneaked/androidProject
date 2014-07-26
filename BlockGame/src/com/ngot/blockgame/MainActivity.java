package com.ngot.blockgame;

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

	public static final int GAME_STOP = 1;
	public static final int GAME_PAUSE = 2;
	public static final int GAME_RESUME = 3;
	public static final int GAME_RESTART = 4;


	GameView gameView;
	BackPressCloseHandler back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gameView = (GameView)findViewById(R.id.GameView);
		back = new BackPressCloseHandler(this, gameView);

	}

	@Override
	protected void onPause() {
		gameView.pauseGame();
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		back.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, GAME_STOP, 0, "GAME STOP");
		menu.add(0, GAME_PAUSE, 0, "GAME PAUSE");
		menu.add(0, GAME_RESUME, 0, "GAME RESUME");
		menu.add(0, GAME_RESTART, 0, "GAME RESTART");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case GAME_STOP:
			gameView.pauseGame();
			showDialog(GAME_STOP);
			break;
		case GAME_PAUSE:
			gameView.pauseGame();
			Toast.makeText(this, "GAME PAUSE", Toast.LENGTH_SHORT).show();
			break;
		case GAME_RESUME:
			gameView.resumeGame();
			Toast.makeText(this, "GAME RESUME", Toast.LENGTH_SHORT).show();
			break;
		case GAME_RESTART:
			gameView.pauseGame();
			showDialog(GAME_RESTART);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case GAME_STOP:
			dialog = new AlertDialog.Builder(this)
			.setTitle("GAME STOP")
			.setMessage("Do you want STOP?")
			.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					gameView.stopGame();
					finish();
				}
			})
			.setNegativeButton("CANCEL", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					gameView.resumeGame();
				}
			}).create();
			break;
		case GAME_RESTART:
			dialog = new AlertDialog.Builder(this)
			.setTitle("GAME RESTART")
			.setMessage("Do you want RESTART?")
			.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					gameView.restartGame();
				}
			})
			.setNegativeButton("CANCEL", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					gameView.resumeGame();
				}
			}).create();
			break;

		}
		return dialog;
	}

	


	/*void endDialog(int score){
		new AlertDialog.Builder(this)
		.setTitle("GAME END")
		.setMessage("YOUR SCOER "+score)
		.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				gameView.restartGame();
			}
		})
		.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				gameView.stopGame();
				finish();

			}
		})
		.create().show();
	};*/
}
