package cn.yumutech.unity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

import cn.yumutech.bean.RequestParams2;
import cn.yumutech.bean.UserInfoDetail;
import cn.yumutech.netUtil.Api;
import cn.yumutech.netUtil.MyExtensionModule;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 霍长江 on 2016/11/15.
 */
public class ConversationActivity extends FragmentActivity implements    RongIMClient.OnReceiveMessageListener,
        RongIMClient.ConnectionStatusListener{

    private TextView mTitle;
    private RelativeLayout mBack;

    private String mTargetId;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    private ImageView mTitle3;
    private String title;
    String token;
    private ConversationFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);

        Intent intent = getIntent();
        setActionBar();
        getIntentDate(intent);
        RongIM.setConnectionStatusListener(this);
        if (App.getContext().getLogo("logo").data == null) {
            startActivity(new Intent(ConversationActivity.this, LogoActivity.class));
        }
        if (DemoContext.getInstance() != null) {
            token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
            isFirstRongYun();
            if (token.equals("default")) {
                startActivity(new Intent(ConversationActivity.this, LogoActivity.class));
            } else {
            }
        }
        isReconnect(intent);
        DemoContext.getInstance().title=title;
        RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());


        setMyExtensionModule();
    }

    @Override
    protected void onResume() {
        super.onResume();
      mTitle.setText( DemoContext.getInstance().title);
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {
        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        title = intent.getData().getQueryParameter("title");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        setActionBarTitle(mConversationType,mTargetId);
        enterFragment(mConversationType, mTargetId);
        setActionBarTitle(mTargetId);
    }
    /**
     * 设置会话页面 Title
     *
     * @param conversationType 会话类型
     * @param targetId         目标 Id
     */
    private void setActionBarTitle(Conversation.ConversationType conversationType, String targetId) {

        if (conversationType == null)
            return;

        if (conversationType.equals(Conversation.ConversationType.PRIVATE)) {
            mTitle3.setVisibility(View.GONE);
            setPrivateActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.GROUP)) {
            setGroupActionBar(targetId);
        }

    }
    /**
     * 设置群聊界面 ActionBar
     *
     * @param targetId 会话 Id
     */
    private void setGroupActionBar(String targetId) {
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        } else {
            setTitle(targetId);
        }
    }
    /**
     * 设置私聊界面 ActionBar
     */
    private void setPrivateActionBar(String targetId) {
        if (!TextUtils.isEmpty(title)) {
            if (title.equals("null")) {
                if (!TextUtils.isEmpty(targetId)) {
                    UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
                    if (userInfo != null) {
                        setTitle(userInfo.getName());
                    }
                }
            } else {
                setTitle(title);
            }

        } else {
            asyneUserInfo(mTargetId);
        }
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {
if(fragment==null) {
    fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

    Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
            .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
            .appendQueryParameter("targetId", mTargetId).build();

    fragment.setUri(uri);
}
    }


    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect(Intent intent) {


        String token = null;

        if (DemoContext.getInstance() != null) {

            token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
        }


        //push或通知过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true")) {

                reconnect(token);
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

                    reconnect(token);
                } else {
                    enterFragment(mConversationType, mTargetId);
                }
            }
        }
    }

    /**
     * 设置 actionbar 事件
     */
    private void setActionBar() {

        mTitle = (TextView) findViewById(R.id.txt1);
        mTitle3 = (ImageView) findViewById(R.id.img3);
        ImageView  iv1 = (ImageView) findViewById(R.id.img1);


        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ConversationActivity.this,BianJiActivity.class);
                App.getContext().addDestoryActivity(ConversationActivity.this,"conversation");
                intent.putExtra("mTargetId",mTargetId);
                startActivity(intent);
            }
        });
    }



    /**
     * 设置 actionbar title
     */
    private void setActionBarTitle(String targetid) {

        mTitle.setText(title);
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {

        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    if(!(App.getContext().getLogo("logo").data==null)) {
                        UserGetToken.getInstance(ConversationActivity.this).getToken(mTargetId);
                    }else{
                        Intent intent=new Intent(ConversationActivity.this,LogoActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onSuccess(String s) {

                    enterFragment(mConversationType, mTargetId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    public void setMyExtensionModule() {
        RongIM.setOnReceiveMessageListener(this);
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
            }
        }
    }
    //链接荣yun
    public void isFirstRongYun(){
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                if(!(App.getContext().getLogo("logo").data==null)) {
                    UserGetToken.getInstance(ConversationActivity.this).getToken(mTargetId);
                }else{
                    Intent intent=new Intent(ConversationActivity.this,LogoActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }


    //获取用户详情数据
    /**
     * 获取Token
     */
    Subscription subscription;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    Observer<UserInfoDetail> observer2=new Observer<UserInfoDetail>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(UserInfoDetail userToken) {
            if(userToken!=null&&userToken.status.code!=null){
                if(userToken.status.code.equals("0")){


                }else if(userToken.status.code.equals("-9")){
                }
            }
        }
    };
    Subscription subscription1;

    public void asyneUserInfo(final String s){
        RequestParams2 userTokenBeen=new RequestParams2(new RequestParams2.UserBean(s,"1234678"),new RequestParams2.DataBean(s));
        subscription1 = Api.getMangoApi1().getUserDetais(new Gson().toJson(userTokenBeen))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer3);

    }
    Observer<UserInfoDetail> observer3=new Observer<UserInfoDetail>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription1);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
        @Override
        public void onNext(UserInfoDetail userToken) {
            if(userToken!=null&&userToken.status.code!=null){
                if(userToken.status.code.equals("0")){
                    mTitle.setText(userToken.data.nickname);
//                    UserInfo info=new UserInfo(userToken.data.id,userToken.data.nickname, Uri.parse(userToken.data.logo_path));
//                    RongIM.getInstance().refreshUserInfoCache(info);

                }else if(userToken.status.code.equals("-9")){
                }
            }
        }
    };



    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        switch (connectionStatus){

            case CONNECTED://连接成功。

                break;
            case DISCONNECTED://断开连接。
              reconnect(token);
                break;
            case CONNECTING://连接中。

                break;
//            case NETWORK_UNAVAILABLE://网络不可用。
//                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                break;
        }
    }


    @Override
    public boolean onReceived(Message message, int i) {
        Log.e("info",message.toString());
        return false;
    }
}
