package com.hard.targets.delhi.post.office.finder;

import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class ViewActivity extends Activity {

	TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10;
	ImageButton b1;
	AdView av;
	DatabaseHelper myDbHelper;
	String off, dis, pin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		setupActionBar();
		Intent t = getIntent();
		off = t.getStringExtra("office");
		dis = t.getStringExtra("district");
		pin = t.getStringExtra("pin");
		initialize();
		myDbHelper = new DatabaseHelper(this);
		try {
			myDbHelper.createDatabase();
		} catch (IOException e) {
			throw new Error("Unable to create database.");
		}
		try {
			myDbHelper.openDatabase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		Cursor c = myDbHelper.getValues(off, dis, pin);
		c.moveToFirst();
		tv1.setText(c.getString(1));
		tv2.setText(c.getString(2));
		tv3.setText(c.getString(3));
		tv4.setText(c.getString(5));
		tv5.setText(c.getString(6));
		tv6.setText(c.getString(7));
		tv7.setText(c.getString(8));
		tv8.setText(c.getString(9));
		tv9.setText(c.getString(10));
		tv10.setText(c.getString(11));
		if (tv8.getText().toString().contains("Not Available"))
			b1.setVisibility(View.INVISIBLE);
		else
			b1.setVisibility(View.VISIBLE);
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String num = tv8.getText().toString();
				Intent x = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
				startActivity(x);
			}
		});
		av.loadAd(new AdRequest());
		c.close();
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	private void initialize() {
		tv1 = (TextView) findViewById(R.id.tvOfficeName);
		tv2 = (TextView) findViewById(R.id.tvPinCode);
		tv3 = (TextView) findViewById(R.id.tvDistrict);
		tv4 = (TextView) findViewById(R.id.tvStatus);
		tv5 = (TextView) findViewById(R.id.tvSubOffice);
		tv6 = (TextView) findViewById(R.id.tvHeadOffice);
		tv7 = (TextView) findViewById(R.id.tvLocation);
		tv8 = (TextView) findViewById(R.id.tvTelephone);
		tv9 = (TextView) findViewById(R.id.tvSPCC);
		tv10 = (TextView) findViewById(R.id.tvPDI);
		b1 = (ImageButton) findViewById(R.id.bCall);
		av = (AdView) findViewById(R.id.adView0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
