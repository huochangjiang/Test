package cn.yumutech.unity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.yumutech.bean.ShengJiRequest;
import cn.yumutech.bean.Update;
import cn.yumutech.fragments.HomeFragment;
import cn.yumutech.fragments.MailListFragment;
import cn.yumutech.fragments.PersonFragment;
import cn.yumutech.fragments.SuperviseFragment;
import cn.yumutech.netUtil.DeviceUtils;
import cn.yumutech.netUtil.FileUtils;
import cn.yumutech.netUtil.MD5Util;
import cn.yumutech.netUtil.ToosUtil;
import cn.yumutech.netUtil.UpdateManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends BaseActivity implements View.OnClickListener,RongIMClient.ConnectionStatusListener, RongIM.UserInfoProvider,RongIM.GroupInfoProvider{


    List<ImageView> ivs=new ArrayList<>();
    List<TextView> tvs=new ArrayList<>();
    String savePath ;
    String apkFilePath;
    public Fragment last_fragment;
    private ImageView login;
    private int index=0;
    private RelativeLayout head;
    private Toolbar id_toolbar;
    private App app;

    public int [] mBackgrounds=new int[]{
            R.drawable.two,R.drawable.two,R.drawable.two,R.drawable.two
    };
    private TextView mTitle;
    private String token;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app= App.getContext();
        savePath=getDir("update", 0).getAbsolutePath();
        apkFilePath= savePath + File.separator   + "cz.apk";
        App.getContext().downLoadPath=apkFilePath;
        addFragement(HomeFragment.newInstance());
        addFragement(SuperviseFragment.newInstance());
        addFragement(MailListFragment.newInstance());
        addFragement(PersonFragment.newInstance());
        initTabs();
        LinearLayout ll_story=(LinearLayout) findViewById(R.id.ll_story);
        showFragmentWithoutBackStackAndAnim(HomeFragment.newInstance(),last_fragment);
        if (DemoContext.getInstance() != null) {
            token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
        }
        if(!token.equals("default")){
            connectRongYun(token);
        }
        RongIM.setConnectionStatusListener(this);

        LinearLayout ll_animation=(LinearLayout) findViewById(R.id.ll_animation);
        LinearLayout ll_applction=(LinearLayout) findViewById(R.id.ll_applciton);
        LinearLayout ll_shop=(LinearLayout) findViewById(R.id.ll_shop);
        login= (ImageView) findViewById(R.id.login);
        head= (RelativeLayout) findViewById(R.id.head);
        id_toolbar= (Toolbar) findViewById(R.id.id_toolbar);
       RongIM.setUserInfoProvider(this,true);
       RongIM.setGroupInfoProvider(this,true);
        UserGetToken.getInstance(this).path=savePath;
        login.setOnClickListener(this);
        ll_animation.setOnClickListener(this);
        ll_story.setOnClickListener(this);
        ll_applction.setOnClickListener(this);
        ll_shop.setOnClickListener(this);
//        ll_animation.performClick();
        if(App.getContext().getLogo("logo")!=null){
            ShengJiRequest sheng=new ShengJiRequest(new ShengJiRequest.UserBean(App.getContext().getLogo("logo").data.id,"1233454"),new ShengJiRequest.DataBean("Android"));
            UpdateManager.getUpdateManager().initDatas1(new Gson().toJson(sheng),this, DeviceUtils.getAPPVersionCodeFromAPP(this),mHandler1);
        }
    }
    @Override
    protected void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initListeners() {

    }

//    public void onEventMainThread(Update userAboutPerson){
//        Log.e("info","gengxingle ");
//        showUpdateDialog(userAboutPerson);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_animation:
                id_toolbar.setVisibility(View.VISIBLE);
                index=1;
                showFragmentWithoutBackStackAndAnim(HomeFragment.newInstance(),last_fragment);
                last_fragment=HomeFragment.newInstance();
                changeColor(0);
                break;
            case R.id.ll_story:
                id_toolbar.setVisibility(View.VISIBLE);
                index=0;
                showFragmentWithoutBackStackAndAnim(MailListFragment.newInstance(),last_fragment);
                last_fragment=MailListFragment.newInstance();
                changeColor(1);
                break;
            case R.id.ll_applciton:
                id_toolbar.setVisibility(View.GONE);
                index=2;
                showFragmentWithoutBackStackAndAnim(MailListFragment.newInstance(),last_fragment);
                last_fragment=MailListFragment.newInstance();
                showFragmentWithoutBackStackAndAnim(SuperviseFragment.newInstance(),last_fragment);
                last_fragment=SuperviseFragment.newInstance();
                changeColor(2);
                break;
            case R.id.ll_shop:
                id_toolbar.setVisibility(View.VISIBLE);
                showFragmentWithoutBackStackAndAnim(PersonFragment.newInstance(),last_fragment);
                last_fragment=PersonFragment.newInstance();
                changeColor(3);
                break;
            case R.id.login:
                if(app.getLogo("logo")!=null){
                    Intent intent=new Intent();
                    App.getContext().addDestoryActivity(this,"mainactivity");
                    intent.setClass(MainActivity.this,AfterLoginActivity.class);
                    intent.putExtra("name",app.getLogo("logo").data.nickname);
                    intent.putExtra("logo",app.getLogo("logo").data.logo_path);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,LogoActivity.class);
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
            FileUtils.deleteFolderFile(savePath, false);
    }

    //改变标签栏的颜色
    private void initTabs(){
       Log.e("info",DeviceUtils.getAPPVersionCodeFromAPP(this)+"---") ;
        ImageView iv1=(ImageView) findViewById(R.id.iv_one);
        ImageView iv2=(ImageView) findViewById(R.id.iv_two);
        ImageView iv3=(ImageView) findViewById(R.id.iv_three);
        ImageView iv4=(ImageView) findViewById(R.id.iv_four);
        TextView tv1=(TextView) findViewById(R.id.tv_one);
        TextView tv2=(TextView) findViewById(R.id.tv_two);
        TextView tv3=(TextView) findViewById(R.id.tv_three);
        TextView tv4=(TextView) findViewById(R.id.tv_four);
        ivs.add(iv2);
        ivs.add(iv1);
        ivs.add(iv3);
        ivs.add(iv4);
        tvs.add(tv2);
        tvs.add(tv1);
        tvs.add(tv3);
        tvs.add(tv4);
    }
    private void  changeColor(int postion){
        for (int i = 0; i <tvs.size() ; i++) {
            if(postion==i){
                tvs.get(i).setTextColor(Color.parseColor("#04c878"));
            }else{
                tvs.get(i).setTextColor(Color.parseColor("#ff323334"));
                ivs.get(i).setBackgroundResource(mBackgrounds[i]);
            }
        }
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        switch (connectionStatus){

            case CONNECTED://连接成功。

                break;
            case DISCONNECTED://断开连接。

                break;
            case CONNECTING://连接中。

                break;
//            case NETWORK_UNAVAILABLE://网络不可用。
////                Toast.makeText(MainActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
//                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                Toast.makeText(MainActivity.this, "用户账户在其他设备登录", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    //连接荣云服务器
    private void connectRongYun(String token){
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                if(!(App.getContext().getLogo("logo")==null)) {
                    UserGetToken.getInstance(MainActivity.this).getToken(App.getContext().getLogo("logo").data.id);
                }
            }
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
//                Intent intent=new Intent(MainActivity.this,LogoActivity.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    public UserInfo getUserInfo(String s) {
        Log.e("info","ccccccccccccccccc");
//            if(s.equals(App.getContext().getLogo("userinfo").data.id)){
                UserInfo info=new UserInfo(App.getContext().getLogo("userinfo").data.id,App.getContext().getLogo("userinfo").data.nickname, Uri.parse(App.getContext().getLogo("userinfo").data.logo_path));

                return info;

//            }
//        return null;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                exit();
            } else {
                super.onBackPressed();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case EXIT:
                    isExit = false;
                    break;
                default:
                    break;
            }
        }
    };
    Handler mHandler1=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
//                case 0:
//                    MissDilog();
//                    TiShiDilog tiShiDilog=new TiShiDilog(MainActivity.this);
//                    tiShiDilog.show();
//                    break;
                case 1:
                    MissDilog();
                    UpdateManager.getUpdateManager().Download(UpdateManager.getUpdateManager().mUpdate.data.url,App.getContext().downLoadPath);

//                    SignOutDilog mDilog=new SignOutDilog(MainActivity.this,"发现新版本,是否更新?");
//                    mDilog.show();
//                    mDilog.setOnLisener(new SignOutDilog.onListern() {
//                        @Override
//                        public void send() {
//                            UpdateManager.getUpdateManager().Download(UpdateManager.getUpdateManager().mUpdate.data.url,App.getContext().downLoadPath);
//                            showDilog("升级中，请稍后...");
//                        }
//                    });
                    break;
                case 2:
                    MissDilog();
                    showUpdateDialog(UpdateManager.mInstanceUpdate);
                    break;
                case 3:
                    Toast.makeText(MainActivity.this, "网络出错，请清新下载", Toast.LENGTH_SHORT).show();
                    break;
                case 4:

                    break;
            }
        }
    };
    /**
     * 安装提示对话框
     */
    private void showUpdateDialog(Update mUpdate) {
        View view = LayoutInflater.from(this).inflate(R.layout.welcomedilog, null);

        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view);


        TextView textView_version = (TextView) view.findViewById(R.id.bh);
        TextView shaohou = (TextView) view.findViewById(R.id.text1);
        TextView newNow = (TextView) view.findViewById(R.id.text2);
        TextView textView_log = (TextView) view.findViewById(R.id.tv);
        textView_log.setText(mUpdate.data.getRemarks());
        newNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                installApk( App.getContext().downLoadPath);
                dialog.dismiss();
            }
        });
        shaohou.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 获取屏幕分辨率来控制宽度
        int width = ToosUtil.getInstance().getScreenWidth(this);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = width * 8 / 10;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);

        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }
    public static final int EXIT = 1005;
    public  boolean isExit = false;
    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(EXIT, 2000);
        } else {
            finish();

        }
    }
    @Override
    public Group getGroupInfo(String s) {

        return null;
    }
    /**
     * 安装apk
     *
     */
    private void installApk(String apk_path) {

        if (!new File(apk_path).exists()) {
            return;
        }

        // 安装之前先修改apk的权限，避免出现解析包错误的问题
        try {
            String command = "chmod 777 " + apk_path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apk_path), "application/vnd.android.package-archive");
        startActivity(i);
    }
    public  String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    /**
     * 校验下载的apk文件的md5值
     *
     * @param md5
     *            期望的md5值
     * @return
     */
    private boolean MD5Check(String md5) {
        boolean b = false;

        String local_md5 = MD5Util.getFileMD5String(new File(apkFilePath));

        if (local_md5.equals(md5)) {
            b = true;
        }

        return b;
    }



}
