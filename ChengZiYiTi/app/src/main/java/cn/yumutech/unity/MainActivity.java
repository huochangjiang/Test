package cn.yumutech.unity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.fragments.HomeFragment;
import cn.yumutech.fragments.MailListFragment;
import cn.yumutech.fragments.PersonFragment;
import cn.yumutech.fragments.SuperviseFragment;
import cn.yumutech.netUtil.FileUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends BaseActivity implements View.OnClickListener,RongIMClient.ConnectionStatusListener, RongIM.UserInfoProvider,RongIM.GroupInfoProvider{


    List<ImageView> ivs=new ArrayList<>();
    List<TextView> tvs=new ArrayList<>();
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
        if(token.equals("default")){
            Intent intent=new Intent(this,LogoActivity.class);
            startActivity(intent);
        }else{
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

        login.setOnClickListener(this);
        ll_animation.setOnClickListener(this);
        ll_story.setOnClickListener(this);
        ll_applction.setOnClickListener(this);
        ll_shop.setOnClickListener(this);
//        ll_animation.performClick();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListeners() {

    }

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

    public String savePath;
    @Override
    protected void onDestroy() {
        super.onDestroy();
            FileUtils.deleteFolderFile(savePath, false);
    }

    //改变标签栏的颜色
    private void initTabs(){
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
                Intent intent=new Intent(MainActivity.this,LogoActivity.class);
                startActivity(intent);
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
                if(!(App.getContext().getLogo("logo").data==null)) {
                    UserGetToken.getInstance(MainActivity.this).getToken(App.getContext().getLogo("logo").data.id);
                }else{
                    Intent intent=new Intent(MainActivity.this,LogoActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Intent intent=new Intent(MainActivity.this,LogoActivity.class);
                startActivity(intent);
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
}
