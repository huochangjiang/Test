package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.MyJoinMenberAdpater;
import cn.yumutech.bean.JoinQun;
import cn.yumutech.bean.RequestParams;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.netUtil.Api;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SelectorJoinActivity extends BaseActivity {
    private boolean isBaoHan=false;
    private List<UserAboutPerson.DataBean> mDatas1 = new ArrayList<>();
    String mIds;
    private String groupId;
    private String groupName;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_selector_join;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Intent intent=getIntent();
        groupId = intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");
        for (int i=0;i<App.getContext().mApbutPerson.size();i++){
            isBaoHan=false;
            for (int j=0;j<App.getContext().qunMember.size();j++){
                if ((App.getContext().mApbutPerson.get(i).id.equals(App.getContext().qunMember.get(j).userId))) {
                    isBaoHan = true;
                }
                if(j==App.getContext().qunMember.size()-1) {
                    if (!isBaoHan) {
                        mDatas1.add(App.getContext().mApbutPerson.get(i));
                    }
                }
            }

        }
        for (int k=0;k<mDatas1.size();k++){
            UserAboutPerson.DataBean bean=mDatas1.get(k);
            bean.type= UserAboutPerson.DataBean.TYPE_NOCHECKED;
        }

        Button button = (Button) findViewById(R.id.denglu);
        ListView listView = (ListView) findViewById(R.id.listview);

       MyJoinMenberAdpater mAdapter = new MyJoinMenberAdpater(mDatas1, this);
        listView.setAdapter(mAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIds==null||mIds.equals("")){
                    Toast.makeText(SelectorJoinActivity.this, "請添加群成員", Toast.LENGTH_SHORT).show();
                    RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                            new RequestParams.DataBean(mIds,groupId,groupName));
                    initDatas1(new Gson().toJson(canshus));
                }

            }
        });
    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getJoinQun(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {

    }
    Subscription subscription;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    //加入群组
    Observer<JoinQun> observer = new Observer<JoinQun>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(JoinQun channels) {
            if(channels.status.code.equals("0")){
                for (int i=0;i<App.getContext().mApbutPerson.size();i++){
                    App.getContext().mApbutPerson.get(i).type=0;
                }
                finish();
            }

        }
    };
}
