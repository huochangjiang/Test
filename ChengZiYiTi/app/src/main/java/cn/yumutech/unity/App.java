package cn.yumutech.unity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.yumutech.bean.ChaXunQunMenmber;
import cn.yumutech.bean.DepartListNew;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.bean.UserLogin;
import cn.yumutech.weight.ACache;
import cn.yumutech.weight.StringUtils1;
import io.rong.imkit.RongIM;


/**
 * Created by 霍长江 on 2016/11/6.
 */
public class App extends MultiDexApplication{
    public ACache aCache;
    private static App INSTANCE;
    public static final String ExternalImageDir = "zhushou";

    public String avatarpath = "useravatar";
    public String music = "music";
    public String avatarpath_bk = "useravatar_bk";
    public String mDeviceToken;

    public static String CachePath = "image_loaders_local";
    public static App getContext() {
        return INSTANCE;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE=this;
        aCache = ACache.get(this, "zhushou");
/**
 *
 * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
 * io.rong.push 为融云 push 进程名称，不可修改。
 */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
//            try {
////                RongPushClient.registerGCM(this);
//            } catch (RongException e) {
//                e.printStackTrace();
//            }
            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
                DemoContext.init(this);
            }
        }
        initImageLoader();
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);


        /**
         * 自定义行为的回调处理
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Log.e("info","laile ");
                if(msg.custom!=null){
                    Intent intent=new Intent(getApplicationContext(),LeaderActivitysActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);

                }
            }
        };
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("deviceToken",deviceToken);
                mDeviceToken=deviceToken;
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }
    private void initConnect(){

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
    /**
     * 保存登陆信息
     */
    // 保存登录信息
    public void saveLogo(String key,String value){
        aCache.put(key, value);
    }
    //返回用户信息
    public UserLogin getLogo(String key){
        String readJson =aCache.getAsString(key);
        if(StringUtils1.isEmpty(readJson))
        {
            return null;
        }else {
            Gson gson = new Gson();
            UserLogin user = gson.fromJson(readJson, UserLogin.class);
            return user;
        }
    }

    //返回用户部门信息
    //返回用户信息
    public DepartListNew getContactGroup(String key){
        String readJson =aCache.getAsString(key);
        if(StringUtils1.isEmpty(readJson))
        {
            return null;
        }else {
            Gson gson = new Gson();
            DepartListNew user = gson.fromJson(readJson, DepartListNew.class);
            return user;
        }
    }
    /**
     * 清除登陆信息
     */
    public void cleanContactGroup() {
        aCache.remove("Contact");
    }
    // 缓存首页数据
    public void savaHomeJson(String key,String value){
        aCache.put(key, value);
    }
    public String readHomeJson(String key){
        String readJson =aCache.getAsString(key);
        return readJson;
    }
    /**
     * 返回用户部门信息
     */

    /**
     * 清除登陆信息
     */
    public void cleanLogoInformation() {
        aCache.remove("logo");
    }


    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
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
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */
      Map<String,Activity> destoryMap = new HashMap<>();
    public  void addDestoryActivity(Activity activity, String activityName) {
        destoryMap.put(activityName,activity);
    }
    /**
     *销毁指定Activity
     */
    public  void destoryActivity(String activityName) {
        Set<String> keySet=destoryMap.keySet();
        for (String key:keySet){
            destoryMap.get(key).finish();
        }
    }

    /**
     * 没登陆的时候做的事
     */
    public void noLogin(Context context){
        Toast.makeText(context,"您还未登陆",Toast.LENGTH_SHORT).show();
    }
    public List<UserAboutPerson.DataBean> mApbutPerson=new ArrayList<>();
    public List<ChaXunQunMenmber.DataBean.UsersBean> qunMember=new ArrayList<>();

}

