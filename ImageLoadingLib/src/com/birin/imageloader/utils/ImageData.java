package com.birin.imageloader.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

/**
 * Model class which is used to hold all the data for image loading purpose.
 */
public class ImageData {

	public static final int MAX_IMAGE_WT_HT = 200;

	public String url;
	public ImageView imageView;
	public int requiredWidth = -1;
	public int requiredHeight = -1;

	/**
	 * Initialize new image data instance.
	 * 
	 * @param url
	 *            URL of image
	 * @param imageView
	 *            image-view on which image needs to be displayed.
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 * 
	 * @throws IllegalArgumentException
	 *             in case of 1. Empty/null URL. 2. ImageView null 3.
	 *             width/height <= 0
	 * 
	 */
	public ImageData(String url, ImageView imageView, int width, int height) {
		validataParams(url, imageView, width, height);
		this.url = url;
		this.imageView = imageView;
		this.requiredWidth = width;
		this.requiredHeight = height;
	}

	/**
	 * Validates URL params
	 */
	private void validataParams(String url, ImageView imageView, int width,
			int height) {
		if (TextUtils.isEmpty(url)) {
			throw new IllegalArgumentException("Url can not be empty");
		}
		if (null == imageView) {
			throw new IllegalArgumentException("ImageView can not be null");
		}
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException(
					"Invalid imageview dimensions width : " + width
							+ " height " + height);
		}
	}

	/**
	 * Sets bitmap on this class's Image-View.
	 * 
	 * @param bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		if (null != bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	}
}