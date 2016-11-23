package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.yumutech.bean.PublishTask;
import cn.yumutech.bean.PublishTaskBeen;
import cn.yumutech.netUtil.Api;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/22.
 */
public class ReleaseTaskActivity extends BaseActivity implements View.OnClickListener{
    private EditText edit_title;
    private EditText edit_neirong;
    private RelativeLayout rl_who;
    private RelativeLayout rl_end_time;
    private RelativeLayout rl_send;
    private TextView who;
    private ImageView back;
    Subscription subscription;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_task;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        edit_title= (EditText) findViewById(R.id.edit_title);
        edit_neirong= (EditText) findViewById(R.id.edit_neirong);
        rl_who= (RelativeLayout) findViewById(R.id.rl_who);
        rl_end_time= (RelativeLayout) findViewById(R.id.rl_end_time);
        rl_send= (RelativeLayout) findViewById(R.id.rl_send);
        who= (TextView) findViewById(R.id.who);
        back= (ImageView) findViewById(R.id.back);
        rl_who.setOnClickListener(this);
        rl_end_time.setOnClickListener(this);
        rl_send.setOnClickListener(this);
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
            case R.id.rl_who:
                Intent intent =new Intent();
                intent.setClass(ReleaseTaskActivity.this,TaskToWhoActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_end_time:
                Intent intent1 =new Intent();
                intent1.setClass(ReleaseTaskActivity.this,TaskEndDateActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_send:
                mHandler.sendEmptyMessage(1);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(edit_title.getText().toString().trim().isEmpty()){
                        Toast.makeText(ReleaseTaskActivity.this,"您还未填写标题，请完善",Toast.LENGTH_SHORT).show();
                    }else if(edit_neirong.getText().toString().trim().isEmpty()){
                        Toast.makeText(ReleaseTaskActivity.this,"您还未填写内容，请完善",Toast.LENGTH_SHORT).show();
                    }else {
                        PublishTaskBeen been=new PublishTaskBeen(new PublishTaskBeen.UserBean(App.getContext().getLogo("logo").data.id,"")
                                ,new PublishTaskBeen.DataBean(edit_title.getText().toString().trim(),edit_neirong.getText().toString().trim(),
                                "2016-11-16","2","3"));
                        initSend(new Gson().toJson(been));
                    }
                    break;
            }
        }
    };
    private void initSend(String canshu){
        subscription = Api.getMangoApi1().getPublishTask(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<PublishTask> observer=new Observer<PublishTask>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(PublishTask publishTask) {
            if(publishTask!=null&&publishTask.status.code.equals("0")){
                Toast.makeText(ReleaseTaskActivity.this,publishTask.status.message,Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
