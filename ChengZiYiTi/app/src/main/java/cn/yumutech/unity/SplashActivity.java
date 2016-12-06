package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2016/12/6.
 */
public class SplashActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mHandler.sendEmptyMessageDelayed(1,3500);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {

    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Intent intent=new Intent();
                    intent.setClass(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
