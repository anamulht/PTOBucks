package com.ptobucks.utils;

import java.io.InputStream;
import java.io.OutputStream;

import android.text.TextUtils;

public class Utils {
	
	private static boolean isTokenExpired;

	public static boolean isTokenExpired() {
		return isTokenExpired;
	}

	public static void setTokenExpired(boolean isTokenExpired) {
		Utils.isTokenExpired = isTokenExpired;
	}
	
	private static long lastTime;

	public static long getLastTime() {
		return lastTime;
	}

	public static void setLastTime(long lastTime) {
		Utils.lastTime = lastTime;
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public final static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}
}