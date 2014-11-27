package com.birin.imageloader.workers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.birin.imageloader.cache.DiskCache;
import com.birin.imageloader.cache.HeapMemoryCache;
import com.birin.imageloader.utils.ImageData;
import com.birin.imageloader.utils.RequestStore;
import com.birin.imageloader.utils.Utils;

/**
 * Gets bitmap from SD card if available else loads from server.
 * 
 */
public class BitmapGetter implements Runnable {

	private ImageData imageData;
	private BitmapFileDecoder decoder;
	private DiskCache diskCache;
	private Handler handler;

	public BitmapGetter(Context context, ImageData imageData,
			String cacheDirectoryName) {
		this.imageData = imageData;
		this.decoder = new BitmapFileDecoder();
		this.diskCache = new DiskCache(context, cacheDirectoryName);
		this.handler = new Handler(Looper.getMainLooper());
	}

	@Override
	public void run() {
		if (RequestStore.getInstance().shouldProcessImageData(imageData) == false) {
			// Image is no longer in use for this URL data, simply return;
			return;
		}
		Bitmap bitmap = null;
		try {
			bitmap = getBitmap();
		} catch (Throwable error) {
			error.printStackTrace();
			if (error instanceof OutOfMemoryError) {
				HeapMemoryCache.getHeapMemoryCache().clear();
			}
		}
		processBitmap(bitmap);
	}

	/**
	 * Put the loaded bitmap in to heap-cache & also post a runnable to update
	 * the ImageView.
	 */
	private void processBitmap(Bitmap bitmap) {
		if (null != bitmap) {
			HeapMemoryCache.getHeapMemoryCache().put(imageData.url, bitmap);
			if (RequestStore.getInstance().shouldProcessImageData(imageData) == true) {
				handler.post(new BitmapSetter(bitmap, imageData));
			}
		}
	}

	/**
	 * Try to get the bitmap from SD, if not found load from server.
	 */
	private Bitmap getBitmap() throws Throwable {
		final String url = imageData.url;
		final int requiredWidth = imageData.requiredWidth;
		final int requiredHeight = imageData.requiredHeight;
		File bitmapFilePointer = diskCache.getFileForUrl(url);
		Bitmap bitmap = decodeBitmapFromSDCardFile(bitmapFilePointer,
				requiredWidth, requiredHeight);
		if (bitmap == null) {
			HttpURLConnection connection = openUrlConnectionToServer(url);
			InputStream is = connection.getInputStream();
			saveInputBitmapStreamToSDCard(bitmapFilePointer, is);
			connection.disconnect();
			bitmap = decodeBitmapFromSDCardFile(bitmapFilePointer,
					requiredWidth, requiredHeight);
		} else {
			// Already from from SD card, no server call.
		}
		return bitmap;

	}

	/**
	 * Create connection to server using Url Connection class.
	 */
	private HttpURLConnection openUrlConnectionToServer(String url)
			throws IOException {
		URL imageUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) imageUrl
				.openConnection();
		connection.setConnectTimeout(30000);
		connection.setReadTimeout(30000);
		connection.setInstanceFollowRedirects(true);
		return connection;
	}

	/**
	 * Save server bitmap stream to SD card.
	 */
	private void saveInputBitmapStreamToSDCard(File bitmapFilePointer,
			InputStream is) throws IOException {
		OutputStream os = new FileOutputStream(bitmapFilePointer);
		Utils.copyStream(is, os);
		os.close();
	}

	/**
	 * Decode bitmap from SD card using {@link BitmapFileDecoder}
	 */
	private Bitmap decodeBitmapFromSDCardFile(File bitmapFile,
			int requiredWidth, int requiredHeight) throws IOException {
		return decoder.decodeFile(bitmapFile, requiredWidth, requiredHeight);
	}

}