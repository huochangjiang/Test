package cn.yumutech.unity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.yumutech.bean.PublishExchange;
import cn.yumutech.bean.PublishExchangeBeen;
import cn.yumutech.netUtil.Api;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/3/2.
 */
public class SuggestActivity extends BaseActivity{
    private EditText edit_title,edit_neirong;
    private RelativeLayout rl_send;
    Subscription subscription;
    public ImageView back;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_suggest;
    }
    @Override
    protected void initViews(Bundle savedInstanceState) {
        edit_title= (EditText) findViewById(R.id.edit_title);
        edit_neirong= (EditText) findViewById(R.id.edit_neirong);
        rl_send= (RelativeLayout) findViewById(R.id.rl_send);
        back= (ImageView) findViewById(R.id.back);
    }
    @Override
    protected void initData() {

    }
    @Override
    protected void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rl_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(1);
            }
        });
    }
    //发布按钮点击事件
    private void initSend(String canshu){
        subscription = Api.getMangoApi1().getPublishExchange(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<PublishExchange> observer=new Observer<PublishExchange>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(PublishExchange publishTask) {
            if(publishTask!=null&&publishTask.status.code.equals("0")){
                MissDilog();
                Toast.makeText(SuggestActivity.this,"发布成功，后台审核中",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(edit_title.getText().toString().trim().isEmpty()){
                        Toast.makeText(SuggestActivity.this,"您还未填写标题，请完善",Toast.LENGTH_SHORT).show();
                    }else if(edit_neirong.getText().toString().trim().isEmpty()){
                        Toast.makeText(SuggestActivity.this,"您还未填写内容，请完善",Toast.LENGTH_SHORT).show();
                    }else {
                        showDilog("发布中...");
                        PublishExchangeBeen been=new PublishExchangeBeen(new PublishExchangeBeen.UserBean(App.getContext().getLogo("logo").data.id,"")
                                ,new PublishExchangeBeen.DataBean("",edit_neirong.getText().toString().trim(),edit_title.getText().toString().trim()));
                        initSend(new Gson().toJson(been));
                    }
                    break;
            }
        }
    };
}
