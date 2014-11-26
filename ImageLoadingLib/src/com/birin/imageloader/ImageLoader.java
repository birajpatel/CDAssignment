package com.birin.imageloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.birin.imageloader.cache.HeapMemoryCache;
import com.birin.imageloader.utils.ImageData;
import com.birin.imageloader.utils.RequestStore;
import com.birin.imageloader.workers.BitmapGetter;

// References
// http://www.vogella.com/tutorials/JavaConcurrency/article.html#threadpools
// http://stackoverflow.com/questions/12191029/running-two-independent-tasks-simultaneously-using-threads
// https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/
// universalimageloader/core/ImageLoader.java
public class ImageLoader {

	public static final String TAG = ImageLoader.class.getSimpleName();

	private ImageLoaderConfiguration configuration;
	private static ImageLoader instance;

	private Context context;
	private String cacheDirectoryName;
	private ExecutorService executorService;

	private ImageLoader() {

	}

	public static ImageLoader getInstance() {
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
	 * To force initialization with new configuration you should
	 * {@linkplain #destroy() destroy ImageLoader} at first.
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
			Log.w(TAG, "Re-Init attempt");
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

	public void displayImage(String imageUrl, ImageView imageView) {
		ensureConfiguration();
		RequestStore.getInstance().putRequest(imageUrl, imageView);
		Bitmap bitmap = HeapMemoryCache.getHeapMemoryCache().get(imageUrl);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			ImageData imageData = new ImageData(imageUrl, imageView);
			imageData.setRequiredDimentions(imageView.getLayoutParams().width,
					imageView.getLayoutParams().height);
			executorService.submit(new BitmapGetter(context, imageData,
					cacheDirectoryName));
		}
	}

	public static int getDeviceWidth(Context context) {
		DisplayMetrics displaymetrics = context.getResources()
				.getDisplayMetrics();
		return displaymetrics.widthPixels;
	}

	public static int getDeviceHeight(Context context) {
		DisplayMetrics displaymetrics = context.getResources()
				.getDisplayMetrics();
		return displaymetrics.heightPixels;
	}
}
