package com.birin.coupondunia;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.birin.imageloader.ImageLoader;
import com.birin.imageloader.ImageLoaderConfiguration;
import com.birin.imageloader.utils.ImageData;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initImageLoader();
		setContentView(R.layout.activity_welcome);
		ImageView imageView = (ImageView) findViewById(R.id.logo);
		ImageData imageData = new ImageData(
				"http://www.coupondunia.in/sitespecific/media/generated/offlineimages/cover_607.jpg",
				imageView, 125, 125);
		ImageLoader.getInstance().displayImage(imageData);
	}

	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).setImageCacheDirectory("TestCache")
				.build();
		ImageLoader.getInstance().init(config);
	}
}
