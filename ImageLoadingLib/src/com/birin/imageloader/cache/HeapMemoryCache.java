package com.birin.imageloader.cache;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

/**
 * Class that is responsible for pointing to the bitmap cache which will be
 * stored on heap memory, we will limit this cache to around
 * {@link HeapMemoryCache#CACHE_HEAP_PERCENT}, this cache will ensure after
 * putting the new bitmap it will remove least recently used bitmaps from memory
 * to ensure that heap max is not reached.
 */

// References :
// http://stackoverflow.com/questions/2630158/detect-application-heap-size-in-android
// http://www.java2s.com/Code/Java/Collections-Data-Structure/ImplementingaLeastRecentlyUsedLRUCache.htm
// http://codereview.stackexchange.com/questions/3138/linkedhashmap-as-lru-cache
// http://stackoverflow.com/questions/2407565/bitmap-byte-size-after-decoding
public class HeapMemoryCache {

	private static final String TAG = HeapMemoryCache.class.getSimpleName();

	private static final float CACHE_HEAP_PERCENT = 0.2f;
	private static final float LOAD_FACTOR = 1.5f;

	private static final int INITIAL_MAP_CAPACITY = 10;

	private long currentCacheSize = 0;
	private long maxAllowedCacheSize = 0;

	private static HeapMemoryCache heapMemoryCache;
	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(
					INITIAL_MAP_CAPACITY, LOAD_FACTOR, true));

	private HeapMemoryCache() {
		// use 20% of available heap memory
		setLimit((long) (Runtime.getRuntime().maxMemory() * CACHE_HEAP_PERCENT));
	}

	public static synchronized HeapMemoryCache getHeapMemoryCache() {
		if (heapMemoryCache == null) {
			heapMemoryCache = new HeapMemoryCache();
		}
		return heapMemoryCache;
	}

	public void setLimit(long limit) {
		maxAllowedCacheSize = limit;
	}

	public Bitmap get(String url) {
		if (null != cache && TextUtils.isEmpty(url) == false
				&& cache.containsKey(url)) {
			return cache.get(url);
		}
		return null;
	}

	/**
	 * Put new bitmap in cache & after putting clean up least recently used
	 * bitmaps to avoid crossing max allowed size limit.
	 */
	public void put(String id, Bitmap bitmap) {
		if (cache.containsKey(id) == false) {
			cache.put(id, bitmap);
			currentCacheSize += getBitmapSizeInBytes(bitmap);
			if (isCacheFull() == true) {
				cleanLRUsedBitmaps();
			}
		}
	}

	/**
	 * By using the property of {@link LinkedHashMap} we will clear out the
	 * least recently used bitmaps till the time we have reduced the cache size
	 * less than max allowed size.
	 */
	private void cleanLRUsedBitmaps() {
		Log.i(TAG, "CurrentCacheSize = " + currentCacheSize + " length="
				+ cache.size());
		Iterator<Entry<String, Bitmap>> lruIterator = cache.entrySet()
				.iterator();
		while (lruIterator.hasNext() == true && isCacheFull() == true) {
			Entry<String, Bitmap> lruEntry = lruIterator.next();
			currentCacheSize -= getBitmapSizeInBytes(lruEntry.getValue());
			lruIterator.remove();
		}
		Log.i(TAG, "New size " + currentCacheSize + " length=" + cache.size());
	}

	/**
	 * returns true if current cache size exceeds the max allowed size.
	 */
	public boolean isCacheFull() {
		return currentCacheSize > maxAllowedCacheSize;
	}

	@SuppressLint("NewApi")
	private long getBitmapSizeInBytes(Bitmap bitmap) {
		int size = 0;
		if (null != bitmap) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
				size = bitmap.getRowBytes() * bitmap.getHeight();
			} else {
				size = bitmap.getByteCount();
			}
		}
		System.out.println("biraj getBitmapSizeInBytes " + size);
		return size;
	}

	/**
	 * Clear the heap-memory-cache.
	 */
	public void clear() {
		if (null != cache) {
			cache.clear();
		}
		currentCacheSize = 0;
	}
}