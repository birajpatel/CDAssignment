package com.birin.imageloader.workers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.birin.imageloader.utils.ImageData;
import com.birin.imageloader.utils.Utils;

/**
 * 
 * Decodes the file from given file object, also while decoding considers the
 * required height,width by calculating the inSampleSize
 * 
 */
public class BitmapFileDecoder {

	private static final String TAG = BitmapFileDecoder.class.getSimpleName();

	public Bitmap decodeFile(File file, int requiredWidth, int requiredHeight)
			throws IOException {
		Bitmap bitmap = null;
		if (null != file && file.exists() == true) {
			BitmapFactory.Options imageOptions = new BitmapFactory.Options();
			imageOptions.inJustDecodeBounds = true;
			FileInputStream dimentionCheckerStream = new FileInputStream(file);
			BitmapFactory.decodeStream(dimentionCheckerStream, null,
					imageOptions);
			dimentionCheckerStream.close();

			// decode with inSampleSize
			// Calculate inSampleSize
			imageOptions.inSampleSize = calculateInSampleSize(imageOptions,
					requiredWidth, requiredHeight);

			// Decode bitmap with inSampleSize set
			imageOptions.inJustDecodeBounds = false;
			FileInputStream bitmapStream = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(bitmapStream, null,
					imageOptions);
			bitmapStream.close();
		}
		return bitmap;
	}

	/**
	 * Calculates max power of 2 which is inSampleSize for decoding bitmap.
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int requiredWidth, int requiredHeight) {
		final int REQUIRED_WT = Math.min(requiredWidth,
				ImageData.MAX_IMAGE_WT_HT);
		final int REQUIRED_HT = Math.min(requiredHeight,
				ImageData.MAX_IMAGE_WT_HT);
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		Utils.log(TAG, "Required WT " + REQUIRED_WT + " HT " + REQUIRED_HT);
		if (height > REQUIRED_HT || width > REQUIRED_WT) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both height and width larger than the requested height and
			// width.
			while ((halfHeight / inSampleSize) > REQUIRED_HT
					&& (halfWidth / inSampleSize) > REQUIRED_WT) {
				inSampleSize *= 2;
			}
		}
		Utils.log(TAG, "inSampleSize " + inSampleSize);
		return inSampleSize;
	}
}
