package com.birin.imageloader.utils;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import android.text.TextUtils;
import android.widget.ImageView;

public class RequestStore {

	private static RequestStore requestStore;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());

	private RequestStore() {

	}

	public static synchronized RequestStore getInstance() {
		if (requestStore == null) {
			requestStore = new RequestStore();
		}
		return requestStore;
	}

	public synchronized void putRequest(String imageUrl, ImageView imageView) {
		imageViews.put(imageView, imageUrl);
	}

	public boolean shouldProcessImageData(ImageData imageData) {
		String url = imageViews.get(imageData.imageView);
		return (TextUtils.isEmpty(url) == false && url.equals(imageData.url));
	}

}
