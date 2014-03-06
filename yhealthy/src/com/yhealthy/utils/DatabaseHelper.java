package com.yhealthy.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	public final static String DATABASENAME = "yhealthy.db";
	public static final int VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASENAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHelper(Context context, Integer version) {
		super(context, DATABASENAME, null, version);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHelper(Context context, String name) {
		super(context, name, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHelper(Context context, String name, Integer version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE province(id varchar(10) primary key not null,name varchar(20) not null);");
		db.execSQL("CREATE TABLE city(id varchar(20) primary key not null,name varchar(20) not null,isHot integer ,provinceId varchar(10) not null);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
