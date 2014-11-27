package com.birin.imageloader.workers;

import android.graphics.Bitmap;

import com.birin.imageloader.utils.ImageData;
import com.birin.imageloader.utils.RequestStore;

/**
 * This setter runnable will be used to post the runnable on handler class on
 * Main thread .
 */
public class BitmapSetter implements Runnable {

	private Bitmap bitmap;
	private ImageData imageData;

	public BitmapSetter(Bitmap bitmap, ImageData imageData) {
		this.bitmap = bitmap;
		this.imageData = imageData;
	}

	/**
	 * Before running make sure ImageView & its URL matches the value present in
	 * current request store.
	 */
	public void run() {
		if (RequestStore.getInstance().shouldProcessImageData(imageData)) {
			imageData.setBitmap(bitmap);
		}
	}
}