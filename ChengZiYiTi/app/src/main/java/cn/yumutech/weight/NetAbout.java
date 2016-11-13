package cn.yumutech.weight;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;

/**
 * Created by 霍长江 on 2016/6/22.
 */
public class NetAbout {



    public static NetAbout instance;
    private NetAbout(){
    }
    public static NetAbout getInstance(){
        if(instance==null){
            instance=new NetAbout();
        }
        return instance;
    }
    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
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
}
