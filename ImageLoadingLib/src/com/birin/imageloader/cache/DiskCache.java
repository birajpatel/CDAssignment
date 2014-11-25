package com.birin.imageloader.cache;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * Class that is responsible for pointing to the image cache which will be
 * stored on disk.
 */
// http://stackoverflow.com/questions/22421686/android-programming-write-file-to-internal-storage
public class DiskCache {

	private File imageCacheFolder;

	public DiskCache(final Context context, final String folderName) {
		final String SDCARD_ROOT = getSDCardPath(context);
		final String IMAGE_CACHE_PATH = SDCARD_ROOT + File.separator
				+ folderName;
		imageCacheFolder = new File(IMAGE_CACHE_PATH);
		imageCacheFolder.mkdirs();
	}

	/**
	 * Determines which Root directory path needs to be used.
	 */
	private String getSDCardPath(Context context) {
		String path;
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_REMOVED)) {
			path = context.getFilesDir().toString();
		} else {
			path = Environment.getExternalStorageDirectory().toString();
		}
		return path;
	}

	/**
	 * Generates unique file name for given URL, hash-code for practical
	 * purposes hash-codes are unique.
	 */
	public File getFileForUrl(String url) {
		String filename = String.valueOf(url.hashCode());
		File file = new File(imageCacheFolder, filename);
		return file;
	}

	/**
	 * Clears the image cache directory & its contents from disk.
	 */
	public void clear() {
		if (null != imageCacheFolder) {
			File[] files = imageCacheFolder.listFiles();
			if (null != files) {
				for (File f : files) {
					f.delete();
				}
			}
			imageCacheFolder.delete();
		}
	}

}