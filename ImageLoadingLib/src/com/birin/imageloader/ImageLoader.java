package com.birin.imageloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;

import com.birin.imageloader.cache.HeapMemoryCache;
import com.birin.imageloader.utils.ImageData;
import com.birin.imageloader.utils.RequestStore;
import com.birin.imageloader.utils.Utils;
import com.birin.imageloader.workers.BitmapGetter;

/**
 * This is the main Image loader class, which handles the display image method,
 * To display image {@link #init(ImageLoaderConfiguration)} needs to be called &
 * configure the loader for next time usages.
 */
public class ImageLoader {

	public static final String TAG = ImageLoader.class.getSimpleName();

	private ImageLoaderConfiguration configuration;
	private static ImageLoader instance;

	private Context context;
	private String cacheDirectoryName;
	private ExecutorService executorService;

	private ImageLoader() {

	}

	/**
	 * Initializes ImageLoader instance, after calling this method configuration
	 * need to be set using {@link #init(ImageLoaderConfiguration)} method.
	 */
	public synchronized static ImageLoader getInstance() {
		if (instance == null) {
			synchronized (ImageLoader.class) {
				if (instance == null) {
					instance = new ImageLoader();
				}
			}
		}
		return instance;
	}

	/**
	 * Initializes ImageLoader instance with configuration.<br />
	 * If configurations was set before ( {@link #isInited()} == true) then this
	 * method does nothing.<br />
	 * 
	 * @param configuration
	 *            {@linkplain ImageLoaderConfiguration ImageLoader
	 *            configuration}
	 * @throws IllegalArgumentException
	 *             if <b>configuration</b> parameter is null
	 */
	public synchronized void init(ImageLoaderConfiguration configuration) {
		if (configuration == null) {
			throw new IllegalArgumentException(
					"Pass valid coniguration to init image loader");
		}
		if (this.configuration == null) {
			this.configuration = configuration;
			this.context = configuration.context;
			this.cacheDirectoryName = configuration.cacheDirectory;
			this.executorService = Executors
					.newFixedThreadPool(configuration.threadPoolSize);
		} else {
			Utils.warn(TAG, "Re-Init-Attempt-Ignore");
		}
	}

	/**
	 * Returns <b>true</b> - if ImageLoader
	 * {@linkplain #init(ImageLoaderConfiguration) is initialized with
	 * configuration}; <b>false</b> - otherwise
	 */
	public boolean isInited() {
		return configuration != null;
	}

	/**
	 * Checks if ImageLoader's configuration was initialized
	 * 
	 * @throws IllegalStateException
	 *             if configuration wasn't initialized
	 */
	private void ensureConfiguration() {
		if (isInited() == false) {
			throw new IllegalStateException(
					"Image loader need to be inited before usage");
		}
	}

	/**
	 * Checks if ImageLoader's configuration was initialized & sets image in
	 * below steps. 1. Check in heap cache & set bitmap if available. 2. Queue
	 * the request in executor for SD card/ From server loading.
	 * 
	 * @param ImageData
	 *            image-data that needs to be loaded.
	 * @throws IllegalStateException
	 *             if imagedata is null.
	 */
	public void displayImage(ImageData imageData) {
		if (null == imageData) {
			throw new IllegalArgumentException("Image data can not be null");
		}
		ensureConfiguration();
		RequestStore.getInstance().putRequest(imageData);
		Bitmap bitmap = HeapMemoryCache.getHeapMemoryCache().get(imageData.url);
		if (bitmap != null) {
			imageData.imageView.setImageBitmap(bitmap);
		} else {
			executorService.submit(new BitmapGetter(context, imageData,
					cacheDirectoryName));
		}
	}
}
