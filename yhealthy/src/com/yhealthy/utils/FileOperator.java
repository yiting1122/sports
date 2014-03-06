package com.yhealthy.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import android.os.Environment;

public class FileOperator {
	public static File[] getFileList(String dir, String suffix) {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		String filePath = path + dir;
		File file = new File(filePath);
		File[] fileList = null;
		if (!file.exists()) {
			return null;
		}
		if (file.isDirectory()) {
			FilenameFilter filter = null;
			if (".mp4".equals(suffix)) {
				filter = new FilenameFilter() {

					@Override
					public boolean accept(File dir, String filename) {
						// TODO Auto-generated method stub
						return filename.endsWith(".mp4");
					}
				};
			}
			if (".jpg".equals(suffix)) {
				filter = new FilenameFilter() {

					@Override
					public boolean accept(File dir, String filename) {
						// TODO Auto-generated method stub
						return filename.endsWith(".jpg");
					}
				};
			}
			fileList = file.listFiles(filter);

		}
		return fileList;
	}

}
