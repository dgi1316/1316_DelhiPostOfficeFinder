package com.hard.targets.delhi.post.office.finder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressLint("SdCardPath")
public class DatabaseHelper extends SQLiteOpenHelper {

	private SQLiteDatabase myDatabase;
	private final Context myContext;
	private static final String DATABASE_NAME = "post.mp3";
	public final static String DATABASE_PATH = "/data/data/com.hard.targets.delhi.post.office.finder/databases/";
	public static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}
	
	public void createDatabase() throws IOException {
		boolean dbExist = checkDatabase();
		
		if(dbExist) {
			Log.v("DB Exists", "db exists");
		}
		
		boolean dbExist1 = checkDatabase();
		
		if(!dbExist1) {
			this.getReadableDatabase();
			try {
				this.close();
				copyDatabase();
			} catch (IOException e) {
				throw new Error("Error copying Database");
			}
		}
	}
	
	private boolean checkDatabase() {
		boolean checkDB = false;
		
		try {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			File dbFile = new File(myPath);
			checkDB = dbFile.exists();
		} catch (SQLiteException e) {
		}
		
		return checkDB;
	}
	
	private void copyDatabase() throws IOException {
		String outFileName = DATABASE_PATH + DATABASE_NAME;
		
		OutputStream myOutput = new FileOutputStream(outFileName);
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
		
		byte[] buffer = new byte[1024];
		int length;
		
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		
		myInput.close();
		myOutput.flush();
		myOutput.close();
	}
	
	public void db_delete() {
		File file = new File(DATABASE_PATH + DATABASE_NAME);
		if (file.exists()) {
			file.delete();
			System.out.println("Delete Database File");
		}
	}
	
	public void openDatabase() throws SQLException {
		String myPath = DATABASE_PATH + DATABASE_NAME;
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	public synchronized void closeDatabase() throws SQLException {
		if (myDatabase != null)
			myDatabase.close();
		super.close();
	}
	
	public Cursor getAllOffice() {
		String query = "SELECT * FROM office ORDER BY pinCode";
		Cursor c = myDatabase.rawQuery(query, null);
		return c;
	}

	public Cursor searchOffice(String s) {
		String query = "select * from office where officeName like '%" + s + "%' order by pinCode";
		Cursor c = myDatabase.rawQuery(query, null);
		return c;
	}
	
	public LinkedList<String> getAllPinCode() {
		String query = "SELECT * FROM pin";
		LinkedList<String> pinCode = new LinkedList<String>();
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			pinCode.add(c.getString(1));
			c.moveToNext();
		}
		return pinCode;
	}

	public LinkedList<String> searchPinCode(String s) {
		String query = "select * from pin where pinCode like '%" + s + "%' ";
		LinkedList<String> pinCode = new LinkedList<String>();
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			pinCode.add(c.getString(1));
			c.moveToNext();
		}
		return pinCode;
	}
	
	public LinkedList<String> getAllDistricts() {
		String query = "SELECT * FROM district";
		LinkedList<String> district = new LinkedList<String>();
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			district.add(c.getString(1));
			c.moveToNext();
		}
		return district;
	}

	public LinkedList<String> searchDistricts(String s) {
		String query = "select * from district where districtName like '%" + s + "%' ";
		LinkedList<String> district = new LinkedList<String>();
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			district.add(c.getString(1));
			c.moveToNext();
		}
		return district;
	}
	
	public Cursor viewPin(String p) {
		String query = "SELECT * FROM office WHERE pinCode = ?";
		Cursor c = myDatabase.rawQuery(query, new String[] {p});
		return c;
	}
	
	public Cursor viewPinSearch(String p, String s) {
		String query = "SELECT * FROM office WHERE pinCode = ? AND officeName like '%" + s + "%' ";
		Cursor c = myDatabase.rawQuery(query, new String[] {p});
		return c;
	}
	
	public Cursor viewDistrict(String d) {
		String query = "SELECT * FROM office WHERE district = ?";
		Cursor c = myDatabase.rawQuery(query, new String[] {d});
		return c;
	}
	
	public Cursor viewDistrictSearch(String d, String s) {
		String query = "SELECT * FROM office WHERE district = ? and officeName like '%" + s + "%' ";
		Cursor c = myDatabase.rawQuery(query, new String[] {d});
		return c;
	}

	public Cursor getValues(String off, String dis, String pin) {
		String query = "SELECT * FROM office WHERE officeName = ? AND district = ? AND pinCode = ?";
		Cursor c = myDatabase.rawQuery(query, new String[] {off, dis, pin});
		return c;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			Log.v("Database Uppgrade", "Database version higher than old.");
			db_delete();
		}
	}

}