package com.birin.imageloader.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

/**
 * General utilities.
 */
public class Utils {

	public static final boolean DEBUG = true;

	public static void log(String tag, String log) {
		if (DEBUG) {
			Log.i(tag, log);
		}
	}

	public static void warn(String tag, String log) {
		if (DEBUG) {
			Log.w(tag, log);
		}
	}

	/**
	 * Copies data from IS to OS in chunks of 1024 bytes.
	 * 
	 * 
	 * @param inputStream
	 *            where data needs to be read from
	 * @param outputStream
	 *            where data needs to be written
	 * @throws IOException
	 *             in case of error.
	 */
	public static void copyStream(InputStream inputStream,
			OutputStream outputStream) throws IOException {
		final int BUFFER_SIZE = 1024;
		byte[] bytes = new byte[BUFFER_SIZE];
		while (true) {
			int count = inputStream.read(bytes, 0, BUFFER_SIZE);
			if (count == -1)
				break;
			outputStream.write(bytes, 0, count);
		}
	}
}