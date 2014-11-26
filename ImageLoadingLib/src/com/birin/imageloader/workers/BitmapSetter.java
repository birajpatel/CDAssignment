package com.birin.imageloader.workers;

import android.graphics.Bitmap;

import com.birin.imageloader.utils.ImageData;
import com.birin.imageloader.utils.RequestStore;

public class BitmapSetter implements Runnable {

	private Bitmap bitmap;
	private ImageData imageData;

	public BitmapSetter(Bitmap bitmap, ImageData imageData) {
		this.bitmap = bitmap;
		this.imageData = imageData;
	}

	public void run() {
		if (RequestStore.getInstance().shouldProcessImageData(imageData)) {
			imageData.setBitmap(bitmap);
		}
	}
}