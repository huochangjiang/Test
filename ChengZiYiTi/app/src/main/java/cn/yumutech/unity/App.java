package cn.yumutech.unity;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;

/**
 * Created by 霍长江 on 2016/11/6.
 */
public class App extends Application{
    private static App INSTANCE;
    public static String CachePath = "image_loaders_local";
    public static App getContext() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE=this;
        initImageLoader();
    }
    private void initImageLoader() {
        // DisplayImageOptions options = new DisplayImageOptions.Builder()
        // .showImageOnLoading(R.drawable.icon_default)
        // .bitmapConfig(Config.RGB_565).cacheOnDisk(true)
        // .cacheInMemory(true).build();
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // .showImageOnLoading(R.drawable.icon_default)
                .showImageOnLoading(R.drawable.zhanweitu)
                .showImageForEmptyUri(R.drawable.zhanweitu).showImageOnFail(R.drawable.zhanweitu)
                // .showImageOnLoading(LayoutToDrawable());
//				.showImageOnLoading(new BitmapDrawable(convertViewToBitmap()))

                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
                // .cacheOnDisc(true)
                // .considerExifParams(true)
                .build();
        File dir = getDir(CachePath, 0).getAbsoluteFile();
        ImageLoaderConfiguration loaderConfiguration = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(options).threadPoolSize(3)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheExtraOptions(480, 800)
                .diskCache(new UnlimitedDiscCache(dir)).build();
        ImageLoader.getInstance().init(loaderConfiguration);
    }
}
