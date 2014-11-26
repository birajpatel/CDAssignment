package com.birin.imageloader;

import android.content.Context;

// Reference
// https://github.com/nostra13/Android-Universal-Image-Loader
// /blob/master/library/src/com/nostra13/universalimageloader/core/ImageLoaderConfiguration.java
public class ImageLoaderConfiguration {

	public static final int DEFAULT_THREAD_POOL_SIZE = 3;
	public static final String DEFAULT_CACHE_DIR = "Cache";

	Context context;
	int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
	String cacheDirectory = DEFAULT_CACHE_DIR;

	private ImageLoaderConfiguration(final Builder builder) {
		this.context = builder.context;
		this.threadPoolSize = builder.threadPoolSize;
		this.cacheDirectory = builder.cacheDirectory;
	}

	public static ImageLoaderConfiguration createDefault(Context context) {
		return new Builder(context).build();
	}

	public static class Builder {

		private Context context;
		private int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
		private String cacheDirectory = DEFAULT_CACHE_DIR;

		public Builder(Context context) {
			this.context = context.getApplicationContext();
		}

		/**
		 * Sets thread pool size for image display tasks.<br />
		 * Default value - {@link #DEFAULT_THREAD_POOL_SIZE this}
		 */
		public Builder threadPoolSize(int threadPoolSize) {
			this.threadPoolSize = threadPoolSize;
			return this;
		}

		/**
		 * Sets cache directory path
		 */
		public Builder setImageCacheDirectory(String directoryPath) {
			this.cacheDirectory = directoryPath;
			return this;
		}

		public ImageLoaderConfiguration build() {
			return new ImageLoaderConfiguration(this);
		}

	}
}
