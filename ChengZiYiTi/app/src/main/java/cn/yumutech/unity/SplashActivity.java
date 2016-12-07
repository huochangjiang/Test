package cn.yumutech.unity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2016/12/6.
 */
public class SplashActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.sendEmptyMessageDelayed(1,3500);
    }





    protected void initData() {

    }

    protected void initListeners() {

    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(App.getContext().getLogo("logo")==null){
                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, LogoActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
            }
        }
    };
}
