package com.birin.coupondunia;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.birin.imageloader.ImageLoader;
import com.birin.imageloader.ImageLoaderConfiguration;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initImageLoader();
		setContentView(R.layout.activity_welcome);
		ImageLoader
				.getInstance()
				.displayImage(
						"http://www.coupondunia.in/sitespecific/media/generated/offlineimages/cover_607.jpg",
						(ImageView) findViewById(R.id.logo));
	}

	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).setImageCacheDirectory("TestCache")
				.build();
		ImageLoader.getInstance().init(config);
	}
}
