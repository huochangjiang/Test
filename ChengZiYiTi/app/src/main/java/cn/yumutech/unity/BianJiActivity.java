package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.QunMenmberAdapter;
import cn.yumutech.bean.ChaXunQunMenmber;
import cn.yumutech.bean.JieSanQun;
import cn.yumutech.bean.RequestCanShu1;
import cn.yumutech.bean.RequestCanshu2;
import cn.yumutech.bean.TuiChuQun;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.MyGridView;
import cn.yumutech.weight.SignOutDilog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BianJiActivity extends BaseActivity {

    private String targid;
    Subscription subscription;
    Subscription subscription1;
    Subscription subscription2;
    private Button mButton;
    private MyGridView gridView;
    ChaXunQunMenmber mChannels=new ChaXunQunMenmber();
    private List<ChaXunQunMenmber.DataBean.UsersBean> mDatas=new ArrayList<>();
    private QunMenmberAdapter mAdapter;
    private int mPosition;
    private TextView title;

    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bian_ji;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Intent intent=getIntent();
        if(intent!=null){
            targid = intent.getStringExtra("mTargetId");
        }
        mButton = (Button) findViewById(R.id.jiesan);
        title = (TextView) findViewById(R.id.tv);
        gridView = (MyGridView) findViewById(R.id.gridView);
        mAdapter = new QunMenmberAdapter(this,mDatas);
        gridView.setAdapter(mAdapter);
        ImageView back= (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getContext().destoryMap.clear();
                finish();
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListeners() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChannels.data.create_user_id.equals(App.getContext().getLogo("logo").data.id)) {

                    RequestCanShu1 canshus = new RequestCanShu1(new RequestCanShu1.UserBean(App.getContext().getLogo("logo").data.id, "134669"),
                            new RequestCanShu1.DataBean(App.getContext().getLogo("logo").data.id,targid));
                    initDatas3(new Gson().toJson(canshus));
                }else{
                    RequestCanshu2 canshus = new RequestCanshu2(new RequestCanshu2.UserBean("3", "134669"),
                            new RequestCanshu2.DataBean(targid, App.getContext().getLogo("logo").data.id));
                    initDatas2(new Gson().toJson(canshus));
                }
            }
        });
        mAdapter.setLisener(new QunMenmberAdapter.onItemClickLisener() {
            @Override
            public void getUserBean(final ChaXunQunMenmber.DataBean.UsersBean beans, final int postion) {

                if(beans==null){
                    Intent intent=new Intent(BianJiActivity.this,QunMenmberSelectorActivity.class);
                    startActivity(intent);
                }else{
                    if(mChannels.data.create_user_id.equals(App.getContext().getLogo("logo").data.id)) {
                        SignOutDilog dilog = new SignOutDilog(BianJiActivity.this, "确认移除" + beans.user_name);
                        dilog.show();
                        dilog.setOnLisener(new SignOutDilog.onListern() {
                            @Override
                            public void send() {
                                mPosition = postion;
                                RequestCanshu2 canshus = new RequestCanshu2(new RequestCanshu2.UserBean("3", "134669"),
                                        new RequestCanshu2.DataBean(targid, beans.userId));
                                initDatas2(new Gson().toJson(canshus));
                            }
                        });
                    }
                }
            }
        });
    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getChaXunMember(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    private void initDatas2( String canshu){
        subscription1 = Api.getMangoApi1().getTuiChuQun(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }
    private void initDatas3( String canshu){
        subscription2 = Api.getMangoApi1().getJieSanQun(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);
    }
   //获取群成员
    Observer<ChaXunQunMenmber> observer = new Observer<ChaXunQunMenmber>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
        @Override
        public void onNext(ChaXunQunMenmber channels) {
            if(channels.status.code.equals("0")){
                title.setText(channels.data.groupName);
                if(channels.data.create_user_id.equals(App.getContext().getLogo("logo").data.id)){
                    mButton.setText("解散群组");
                }
                mChannels=channels;
                mDatas=channels.data.users;
                mAdapter.dataChange(channels.data.users);
            }
        }
    };
    //退出qun
    Observer<TuiChuQun> observer1 = new Observer<TuiChuQun>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription1);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
        @Override
        public void onNext(TuiChuQun channels) {
            if(channels.status.code.equals("0")){
                if(mChannels.data.create_user_id.equals(App.getContext().getLogo("logo").data.id)) {
                    mDatas.remove(mPosition);
                    mAdapter.dataChange(mDatas);
                }else{
                    App.getContext().destoryActivity("conversation");
                    finish();
                }
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        RequestCanshu2 canshus=new RequestCanshu2(new RequestCanshu2.UserBean("3","134669"),
                new RequestCanshu2.DataBean(targid));
        initDatas1(new Gson().toJson(canshus));
    }

    //解散群
    Observer<JieSanQun> observer2 = new Observer<JieSanQun>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription2);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(JieSanQun channels) {
            if(channels.status.code.equals("0")){
                App.getContext().destoryActivity("conversation");
                finish();
            }

        }
    };
}
