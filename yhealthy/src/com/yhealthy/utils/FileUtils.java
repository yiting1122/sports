package com.yhealthy.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public abstract class FileUtils {
	private static final String TAG = "uitls.FileUtils";

	public static boolean copyFile(InputStream in, OutputStream out)
			throws IOException {
		boolean flag = false;
		if (in != null && out != null) {
			byte[] buf = new byte[1024];
			try {
				int count = in.read(buf);
				while (count > 0) {
					out.write(buf, 0, count);
					count = in.read(buf);
				}
				out.flush();
				out.close();
				in.close();
				in = null;
				out = null;
				flag = true;
				Log.i(TAG, " copy file successfully");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.i(TAG, "fail to copy file");
			} finally {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			}
		}
		return flag;
	}

	public static byte[] readInputStrem(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		try {
			Log.i(TAG, "start to read inputstrem");
			int len = in.read(buffer);
			while (len > 0) {
				out.write(buffer, 0, len);
				len = in.read(buffer);
			}
			Log.i(TAG, "success to read inputstrem");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i(TAG, "fail to read inputstrem");
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		}
		return out.toByteArray();
	}
}
