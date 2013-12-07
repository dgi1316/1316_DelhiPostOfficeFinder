package com.hard.targets.delhi.post.office.finder;

import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ViewDistrictActivity extends Activity {

	String d;
	DatabaseHelper myDbHelper;
	TextView tv;
	EditText et;
	ListView lv;
	CustomListAdapter adapter;
	ArrayList<String> office = new ArrayList<String>();
	ArrayList<String> district = new ArrayList<String>();
	ArrayList<String> pin = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_district);
		setupActionBar();
		tv = (TextView) findViewById(R.id.tvHeadDistrict);
		et = (EditText) findViewById(R.id.etDistrict);
		lv = (ListView) findViewById(R.id.lvDistrict);
		Intent t = getIntent();
		d = t.getStringExtra("district");
		tv.setText(getResources().getString(R.string.headDistrict) + ": " + d);
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
		Cursor c = myDbHelper.viewDistrict(d);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			office.add(c.getString(1));
			district.add(c.getString(3));
			pin.add(c.getString(2));
			c.moveToNext();
		}
		c.close();
		adapter = new CustomListAdapter(ViewDistrictActivity.this, office, district, pin);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent view1 = new Intent("com.hard.targets.delhi.post.office.finder.VIEWACTIVITY");
				view1.putExtra("office", office.get(arg2).toString());
				view1.putExtra("district", district.get(arg2).toString());
				view1.putExtra("pin", pin.get(arg2).toString());
				startActivity(view1);
			}
		});
		et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				office.clear();
				district.clear();
				pin.clear();
				adapter.clear();
				Cursor c;
				lv.setAdapter(null);
				if(et.getText().toString().contentEquals("")) {
					c = myDbHelper.viewDistrict(d);
					c.moveToFirst();
					for(int i = 0; i <= c.getCount() - 1; i++) {
						office.add(c.getString(1));
						district.add(c.getString(3));
						pin.add(c.getString(2));
						c.moveToNext();
					}
					c.close();
				} else {
					String search = et.getText().toString();
					c = myDbHelper.viewDistrictSearch(d, search);
					c.moveToFirst();
					for(int i = 0; i <= c.getCount() - 1; i++) {
						office.add(c.getString(1));
						district.add(c.getString(3));
						pin.add(c.getString(2));
						c.moveToNext();
					}
					c.close();
				}
				adapter = new CustomListAdapter(ViewDistrictActivity.this, office, district, pin);
				lv.setAdapter(adapter);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_district, menu);
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
