package com.birin.imageloader;

import android.content.Context;

/**
 * This class is a model class for configuring image loader instance, useful to
 * configure the loader in custom way, for default value use
 * {@link #createDefault(Context)} method.
 */
public class ImageLoaderConfiguration {

	public static final int DEFAULT_THREAD_POOL_SIZE = 3;
	public static final String DEFAULT_CACHE_DIR = ".Cache";

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

	/**
	 * Builder to build config instance.
	 */
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

		/**
		 * Builds the config object using this builder.
		 */
		public ImageLoaderConfiguration build() {
			return new ImageLoaderConfiguration(this);
		}

	}
}
