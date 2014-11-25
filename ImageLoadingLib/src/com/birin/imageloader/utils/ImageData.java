package com.birin.imageloader.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageData {

	public String url;
	public ImageView imageView;

	public ImageData(String url, ImageView imageView) {
		this.url = url;
		this.imageView = imageView;
	}

	public void setBitmap(Bitmap bitmap) {
		if (null != imageView && null != bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	}
}