package cn.yumutech.unity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.Gson;

import cn.yumutech.bean.CreateQunZu;
import cn.yumutech.bean.DeviceTokenBean;
import cn.yumutech.bean.RequestParams;
import cn.yumutech.bean.RequestParams2;
import cn.yumutech.bean.UserInfoDetail;
import cn.yumutech.bean.UserToken;
import cn.yumutech.netUtil.Api;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 霍长江 on 2016/11/18.
 */
public class UserGetToken {

    public static UserGetToken instance;
    public static String token;
    public static Context mContext;
    public CreateQunZu mQunZhus;
    //保存下载的路径
    public String path;
    private UserGetToken(){

    }
    public static UserGetToken getInstance(Context context){
        if(instance==null){
            instance=new UserGetToken();
        }
        mContext=context;
        return instance;
    }

    //获取token
    public  void getToken(String id){
        RequestParams userTokenBeen=new RequestParams(new RequestParams.UserBean(id,"1234678"),new RequestParams.DataBean(null));
        getUserToken(new Gson().toJson(userTokenBeen));
    }
    /**
     * 获取Token
     */
    Subscription subscription;
    Subscription subscription1;
    Subscription subscription4;
    Subscription subscription2;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    public  void getUserToken(String useToken){
        subscription = Api.getMangoApi1().getUserToken(useToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);

    }

    public void getUpLoadToken(String token){
        subscription4 = Api.getMangoApi1().getDevieTokenBean(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer4);
    }
    Observer<UserToken> observer2=new Observer<UserToken>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(UserToken userToken) {
            if(userToken!=null&&userToken.status.code!=null){
                if(userToken.status.code.equals("0")){
                    token=userToken.data.token;
                    connectRongYun(token);

                }else if(userToken.status.code.equals("-9")){
                    Intent intent=new Intent(mContext,LogoActivity.class);
                    mContext.startActivity(intent);
                }
            }
        }
    };

    //连接荣云服务器
    private void connectRongYun(String token){
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

            }
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

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
                    String a=new Gson().toJson(userToken);
                        App.getContext().saveLogo("userinfo",a);


                    UserInfo info=new UserInfo(userToken.data.id,userToken.data.nickname, Uri.parse(userToken.data.logo_path));
                    RongIM.getInstance().refreshUserInfoCache(info);

                }else if(userToken.status.code.equals("-9")){
                }
            }
        }
    };


    //上傳設備token到服務器上
    Observer<DeviceTokenBean> observer4=new Observer<DeviceTokenBean>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription4);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
        @Override
        public void onNext(DeviceTokenBean userToken) {
            if(userToken!=null&&userToken.code!=null){

            }
        }
    };


}
