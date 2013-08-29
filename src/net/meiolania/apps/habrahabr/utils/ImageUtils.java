package net.meiolania.apps.habrahabr.utils;

import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageUtils {

	public static ImageLoaderConfiguration createConfiguration(Context context) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).memoryCacheSize(3000000)
				.maxImageWidthForMemoryCache(200).discCacheSize(50000000).httpReadTimeout(5000).defaultDisplayImageOptions(options).build();
		
		return configuration;
	}

}