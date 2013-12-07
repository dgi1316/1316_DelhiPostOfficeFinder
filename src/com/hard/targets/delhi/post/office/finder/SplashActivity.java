package com.hard.targets.delhi.post.office.finder;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;

public class SplashActivity extends Activity implements Runnable {

	public final String database = "run.db";
	public final static String table = "run";
	public final static String Column_val = "val";
	
	static SQLiteDatabase check;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		getActionBar().hide();
		Thread timer = new Thread(this);
		timer.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			Intent openStartingPoint = new Intent("com.hard.targets.delhi.post.office.finder.MAINACTIVITY");
			startActivity(openStartingPoint);
		}
	}

	@Override
	protected void onResume() {
		check = openOrCreateDatabase(database, SQLiteDatabase.CREATE_IF_NECESSARY|SQLiteDatabase.OPEN_READWRITE, null);
		check.execSQL("CREATE TABLE IF NOT EXISTS " + table + " (" +
				Column_val + " TEXT NOT NULL);");

		super.onResume();
	}

}
