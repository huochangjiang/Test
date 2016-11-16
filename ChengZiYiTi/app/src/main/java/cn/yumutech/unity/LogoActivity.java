package cn.yumutech.unity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.yumutech.bean.MessageBean;
import cn.yumutech.bean.UserLogin;
import cn.yumutech.bean.UserLoginBeen;
import cn.yumutech.bean.UserToken;
import cn.yumutech.bean.UserTokenBeen;
import cn.yumutech.bean.YanZhenMessageBean;
import cn.yumutech.netUtil.Api;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LogoActivity extends BaseActivity implements View.OnClickListener{

    private EditText phone;
    private EditText password;
    private Button denglu;
    private TextView tv;
    Subscription subscription;
    Subscription subscription1;
    Subscription subscription2;
    private ImageView back;
    private int time=60;
    private TextView token;
    private UserLogin mLogin;
    private App app;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_logo;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app = (App) LogoActivity.this.getApplicationContext();
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        denglu = (Button) findViewById(R.id.denglu);
        tv = (TextView) findViewById(R.id.tv_huoqu);
        back= (ImageView) findViewById(R.id.back);
        token= (TextView) findViewById(R.id.token);
        controlTitle(findViewById(R.id.back));




    }

    @Override
    protected void initData() {

    }
    private void getYanzheng(){
        MessageBean yanzhengma=new MessageBean(new MessageBean.UserBean("",""),new MessageBean.DataBean(phone.getText().toString().trim()));
        getYanzheng1(new Gson().toJson(yanzhengma));
    }
    private void getYanzheng1( String yanzhengma){
        subscription = Api.getMangoApi1().getMessageVail(yanzhengma)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    Observer<YanZhenMessageBean> observer = new Observer<YanZhenMessageBean>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(YanZhenMessageBean channels) {
            if(channels!=null&&channels.status.code!=null){
                if(channels.status.code.equals("0")){
                    statrTimer();
                    Toast.makeText(LogoActivity.this,channels.status.message,Toast.LENGTH_SHORT).show();
                }else if(channels.status.code.equals("-6")){
                    //现在仅为测试，此处加了timer，正常的该加载成功后
                    statrTimer();
                    Toast.makeText(LogoActivity.this,channels.status.message,Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected void initListeners() {
        back.setOnClickListener(this);
        tv.setClickable(true);
        tv.setOnClickListener(this);
        denglu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_huoqu:

                if(TextUtils.isEmpty(phone.getText().toString().trim())){
//                    Snackbar.make(v,"请填写手机号码",Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(LogoActivity.this,"请填写手机号码",Toast.LENGTH_SHORT).show();
                    return ;
                }else {
//                    showDilog("获取中...");
                    if(phone.getText().toString().trim().length()==11) {
//                        statrTimer();
                        //获取验证码
                        getYanzheng();
                    }
                }
                if(TextUtils.isEmpty(phone.getText().toString().trim())){
                    Toast.makeText(LogoActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(!(phone.getText().toString().trim().length()==11)){
                    Toast.makeText(LogoActivity.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!checkPhone(phone.getText().toString().trim())){
                    if(!isMobileNO(phone.getText().toString().trim())) {
                        Toast.makeText(LogoActivity.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
//                Intent intent=new Intent(LogoActivity.this,RegistActivity.class);
//                startActivity(intent);
                break;
            case R.id.denglu:
                dengLu();
                break;
            case  R.id.back:
                finish();
                break;

        }
    }
    //登录
    public void dengLu(){

        if(TextUtils.isEmpty(phone.getText().toString().trim())){
            Toast.makeText(LogoActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return ;
        }
        if(TextUtils.isEmpty(password.getText().toString().trim())){
            Toast.makeText(LogoActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return ;
        }
        if(!(password.getText().toString().trim().length()==4)){
            Toast.makeText(LogoActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
            return ;
        }
      //  showDilog("登陆中...");
        userRegister();

    }
    /**
     * 登陆
     */
    public void userRegister(){
        UserLoginBeen userLoginBeen=new UserLoginBeen(new UserLoginBeen.UserBean("",""),new UserLoginBeen.DataBean(phone.getText().toString().trim(),password.getText().toString().trim()));
        getUserLogin(new Gson().toJson(userLoginBeen));
    }
    private void getUserLogin(String userLogin){
        subscription1 = Api.getMangoApi1().getUserLogin(userLogin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);

    }
    Observer<UserLogin> observer1=new Observer<UserLogin>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription1);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(UserLogin userLogin) {
            if(userLogin!=null&&userLogin.status.code.equals("0")){
                String a=new Gson().toJson(userLogin);
                Log.e("info",a);
                mLogin=userLogin;
                connectRongYun(mLogin.data.token);
                SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
                edit.putString("DEMO_TOKEN", mLogin.data.token);
                edit.apply();
              //  getToken(mLogin);
                if(userLogin.status.code.equals("0")){
                    String logoData=new Gson().toJson(userLogin);
                    app.saveLogo("logo",logoData);
                    //正常登录
                    Toast.makeText(LogoActivity.this,userLogin.status.message,Toast.LENGTH_SHORT).show();

                }else if(userLogin.status.code.equals("-6")){
                    //手机号不存在
                    Toast.makeText(LogoActivity.this,userLogin.status.message,Toast.LENGTH_SHORT).show();
                }else if(userLogin.status.code.equals("-8")){
                    //验证码过期了
                    Toast.makeText(LogoActivity.this,userLogin.status.message,Toast.LENGTH_SHORT).show();
                }
        }
    }
   };
    //获取token
    public void getToken(UserLogin userLogin){
        UserTokenBeen userTokenBeen=new UserTokenBeen(new UserTokenBeen.data(userLogin.data.nickname,userLogin.data.mobile));
        getUserToken(new Gson().toJson(userTokenBeen));
    }

    /**
     * 获取Token
     */
    public void getUserToken(String useToken){
        subscription2 = Api.getMangoApi1().getUserToken(useToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);

    }
    Observer<UserToken> observer2=new Observer<UserToken>() {
        @Override
        public void onCompleted() {
              unsubscribe(subscription2);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(UserToken userToken) {
            if(userToken!=null&&userToken.status.code!=null){
                token.setText(userToken.status.message);
               if(userToken.status.code.equals("0")){
                   token.setText(userToken.status.message);
               }else if(userToken.status.code.equals("-9")){
                   token.setText(userToken.status.message);
               }
            }
        }
    };
    Timer timer;
    TimerTask task;

    //发送验证码倒计时
    public void statrTimer(){
        if(timer==null)
            timer = new Timer();

        if(task==null){
            task=new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            };

            if(timer!=null&&task!=null){
                timer.schedule(task, 0, 1000);
            }

        }
    }
    public void stopTimer(){
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
        if(task!=null){
            task.cancel();
            task=null;
        }
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    tv.setText(time+""+"秒  ");
                    time--;
                    tv.setClickable(false);
                    if(time<=1){
                        stopTimer();
                        time=60;
                        tv.setClickable(true);
                        tv.setText("获取验证码");
                    }
                    break;
            }
        }
    };
    //检测输入号码是否为正确格式
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    public static boolean isMobileNO(String mobiles){


        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0,5-9])|(17[0-9]))\\d{8}$");


        Matcher m = p.matcher(mobiles);


        return m.matches();


    }


    //连接荣云服务器
    private void connectRongYun(String token){
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.e("info","-过期--");
            }

            @Override
            public void onSuccess(String s) {

                Intent intent=new Intent();
                    intent.setClass(LogoActivity.this,AfterLoginActivity.class);
                    intent.putExtra("name",mLogin.data.nickname);
                    intent.putExtra("logo",mLogin.data.logo_path);
                    startActivity(intent);
                finish();
//            Log.e("info",s+"---");
//                if(RongIM.getInstance()!=null){
//                    RongIM.getInstance().startPrivateChat(LogoActivity.this, "4", "title");
//
//                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });

    }

}
