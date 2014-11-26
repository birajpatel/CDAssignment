package com.birin.imageloader.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageData {

	public static final int DEFAULT_IMAGE_WT_HT = 125;
	public static final int MAX_IMAGE_WT_HT = 300;

	public String url;
	public ImageView imageView;
	public int requiredWidth = DEFAULT_IMAGE_WT_HT;
	public int requiredHeight = DEFAULT_IMAGE_WT_HT;

	public ImageData(String url, ImageView imageView) {
		this.url = url;
		this.imageView = imageView;
	}

	public ImageData(String url, ImageView imageView, int width, int height) {
		this(url, imageView);
		this.requiredWidth = width;
		this.requiredHeight = height;
	}

	public void setBitmap(Bitmap bitmap) {
		if (null != imageView && null != bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	}

	public void setRequiredDimentions(int width, int height) {
		this.requiredWidth = width;
		this.requiredHeight = height;
	}
}