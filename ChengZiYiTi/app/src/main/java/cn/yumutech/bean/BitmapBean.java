package cn.yumutech.bean;

import android.graphics.Bitmap;

/**
 * Created by huo on 2016/11/22.
 */
public class BitmapBean {
    public Bitmap bitmap;

    public BitmapBean(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
