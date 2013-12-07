package com.hard.targets.delhi.post.office.finder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		Intent x = new Intent(this, TutorialActivity.class);
		String query = "SELECT * FROM " + SplashActivity.table;
		Cursor c = SplashActivity.check.rawQuery(query, null);
		if(c.getCount() == 0) {
			c.close();
			ContentValues values = new ContentValues();
			values.put(SplashActivity.Column_val, "1");
			SplashActivity.check.insert(SplashActivity.table, null, values);
			x.putExtra("val", 1);
			startActivity(x);
		} else {
			c.moveToFirst();
			int val = Integer.parseInt("" + c.getString(0));
			if (val <= 2) {
				x.putExtra("val", val);
				startActivity(x);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.rate:
			
			return true;
		case R.id.share:
			
			return true;
		case R.id.about:
			
			return true;
		case R.id.disclaimer:
			
			return true;
		case R.id.exit:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
			.setIcon(R.drawable.ic_action_alert)
			.setTitle(R.string.dialog_title)
			.setMessage(R.string.dialog_message)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					MainActivity.this.finish();
				}
			})
			.setNegativeButton(R.string.no, null)
			.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public static class DummySectionFragment extends Fragment {
		
		View rootView = null;
		int pos;
		EditText et1, et2, et3;
		ListView lv1, lv2, lv3;
		LinkedList<String> ll2, ll3;
		CustomListAdapter adapter;
		ArrayAdapter<String> aa2, aa3;
		ArrayList<String> office = new ArrayList<String>();
		ArrayList<String> district = new ArrayList<String>();
		ArrayList<String> pin = new ArrayList<String>();
		AdView av1, av2, av3;

		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			pos = getArguments().getInt(ARG_SECTION_NUMBER);
			
			final DatabaseHelper myDbHelper = new DatabaseHelper(getActivity());
			
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
			
			switch (pos) {
			case 0:
				rootView = inflater.inflate(R.layout.fragment_main_dummy_1, container, false);
				et1 = (EditText) rootView.findViewById(R.id.etSearch1);
				lv1 = (ListView) rootView.findViewById(R.id.listView1);
				Cursor c = myDbHelper.getAllOffice();
				c.moveToFirst();
				for(int i = 0; i <= c.getCount() - 1; i++) {
					office.add(c.getString(1));
					district.add(c.getString(3));
					pin.add(c.getString(2));
					c.moveToNext();
				}
				c.close();
				adapter = new CustomListAdapter(getActivity(), office, district, pin);
				lv1.setAdapter(adapter);
				lv1.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent view1 = new Intent("com.hard.targets.delhi.post.office.finder.VIEWACTIVITY");
						view1.putExtra("office", office.get(position).toString());
						view1.putExtra("district", district.get(position).toString());
						view1.putExtra("pin", pin.get(position).toString());
						startActivity(view1);
					}
				});
				et1.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						try {
							office.clear();
							district.clear();
							pin.clear();
							adapter.clear();
							Cursor c;
							lv1.setAdapter(null);
							if (et1.getText().toString().contentEquals("")) {
								c = myDbHelper.getAllOffice();
								c.moveToFirst();
								for(int i = 0; i <= c.getCount() - 1; i++) {
									office.add(c.getString(1));
									district.add(c.getString(3));
									pin.add(c.getString(2));
									c.moveToNext();
								}
								c.close();
							} else {
								String search1 = et1.getText().toString();
								c = myDbHelper.searchOffice(search1);
								c.moveToFirst();
								for(int i = 0; i <= c.getCount() - 1; i++) {
									office.add(c.getString(1));
									district.add(c.getString(3));
									pin.add(c.getString(2));
									c.moveToNext();
								}
								c.close();
							}
							adapter = new CustomListAdapter(getActivity(), office, district, pin);
							lv1.setAdapter(adapter);
						} catch (Exception e) {
							Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}
					
					@Override
					public void afterTextChanged(Editable s) {
					}
				});
				av1 = (AdView) rootView.findViewById(R.id.adViewFragment1);
				av1.loadAd(new AdRequest());
				break;
			case 1:
				//String[] pinCodes = { "110001", "110002", "110003", "110004", "110005", "110006", "110007", "110008", "110009", "110010", "110011", "110012", "110013", "110014", "110015", "110016", "110017", "110018", "110019", "110020", "110021", "110022", "110023", "110024", "110025", "110026", "110027", "110028", "110029", "110030", "110031", "110032", "110033", "110034", "110035", "110036", "110037", "110038", "110039", "110040", "110041", "110042", "110043", "110044", "110045", "110046", "110047", "110048", "110049", "110051", "110052", "110053", "110054", "110055", "110056", "110057", "110058", "110059", "110060", "110061", "110062", "110063", "110064", "110065", "110066", "110067", "110068", "110069", "110070", "110071", "110072", "110073", "110074", "110075", "110076", "110077", "110078", "110080", "110081", "110082", "110083", "110084", "110085", "110086", "110087", "110088", "110089", "110091", "110092", "110093", "110094", "110095", "110096" };
				rootView = inflater.inflate(R.layout.fragment_main_dummy_2, container, false);
				et2 = (EditText) rootView.findViewById(R.id.etSearch2);
				lv2 = (ListView) rootView.findViewById(R.id.listView2);
				ll2 = myDbHelper.getAllPinCode();
				aa2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ll2);
				lv2.setAdapter(aa2);
				lv2.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						Intent view2 = new Intent("com.hard.targets.delhi.post.office.finder.VIEWPINACTIVITY");
						view2.putExtra("pin", "" + lv2.getItemAtPosition(arg2));
						startActivity(view2);
					}
				});
				et2.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						try {
							ll2.clear();
							aa2.clear();
							lv2.setAdapter(null);
							if (et2.getText().toString().contentEquals("")) {
								ll2 = myDbHelper.getAllPinCode();
							} else {
								String search2 = et2.getText().toString();
								ll2 = myDbHelper.searchPinCode(search2);
							}
							aa2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ll2);
							lv2.setAdapter(aa2);
						} catch (Exception e) {
							Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}
					
					@Override
					public void afterTextChanged(Editable s) {
					}
				});
				av2 = (AdView) rootView.findViewById(R.id.adViewFragment2);
				av2.loadAd(new AdRequest());
				break;
			case 2:
				rootView = inflater.inflate(R.layout.fragment_main_dummy_3, container, false);
				et3 = (EditText) rootView.findViewById(R.id.etSearch3);
				lv3 = (ListView) rootView.findViewById(R.id.listView3);
				ll3 = myDbHelper.getAllDistricts();
				aa3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ll3);
				lv3.setAdapter(aa3);
				lv3.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						Toast.makeText(getActivity(), "You Selected: " + lv3.getItemAtPosition(arg2), Toast.LENGTH_LONG).show();
						Intent view3 = new Intent("com.hard.targets.delhi.post.office.finder.VIEWDISTRICTACTIVITY");
						view3.putExtra("district", "" + lv3.getItemAtPosition(arg2));
						startActivity(view3);
					}
				});
				et3.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						try {
							ll3.clear();
							aa3.clear();
							lv3.setAdapter(null);
							if (et3.getText().toString().contentEquals("")) {
								ll3 = myDbHelper.getAllDistricts();
							} else {
								String search2 = et3.getText().toString();
								ll3 = myDbHelper.searchDistricts(search2);
							}
							aa3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ll3);
							lv3.setAdapter(aa3);
						} catch (Exception e) {
							Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}
					
					@Override
					public void afterTextChanged(Editable s) {
					}
				});
				av3 = (AdView) rootView.findViewById(R.id.adViewFragment3);
				av3.loadAd(new AdRequest());
				break;
			default:
				break;
			}
			
			return rootView;
		}
	}

}
