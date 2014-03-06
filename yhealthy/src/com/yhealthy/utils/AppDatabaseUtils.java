package com.yhealthy.utils;

import java.io.File;



import android.content.Context;

import android.util.Log;

public abstract class AppDatabaseUtils {
	private final static String TAG = "uitls.DatabaseUtils";

	public static boolean isExistDatabase(Context context) {
		File file = context.getFilesDir();
		if (file != null && file.exists()) {
			File parentFile = file.getParentFile();
			if (parentFile != null && parentFile.exists()) {
				String databasePath = parentFile.getPath() + "/databases/"
						+ DatabaseHelper.DATABASENAME;
				File databaseFile = new File(databasePath);
				if (databaseFile != null && databaseFile.exists()) {
					Log.i(TAG, "a database file exist");
					return true;
				}
			}
		}
		Log.i(TAG, "a database file no exist");
		return false;
	}

}
