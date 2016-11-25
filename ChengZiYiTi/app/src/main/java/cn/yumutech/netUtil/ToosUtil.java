package cn.yumutech.netUtil;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by 霍长江 on 2016/8/11.
 */
public class ToosUtil {
    public static ToosUtil instance;
    public static ToosUtil getInstance(){
        if(instance==null){
            instance=new ToosUtil();

        }
        return instance;
    }
    private ToosUtil(){

    }
    /**
     * 获取屏幕的宽和搞
     */
    public int getScreenWidth(Activity mcontext){
        WindowManager wm;
        wm = mcontext.getWindowManager();
        return wm.getDefaultDisplay().getWidth();
    }
    /**
     * 获取屏幕的宽和搞
     */
    public int getScreenHeight(Activity mcontext){
        WindowManager wm;
        wm = mcontext.getWindowManager();
        return wm.getDefaultDisplay().getHeight();
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
