package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import cn.yumutech.Adapter.YouQIngAdapter;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.YouQIngLianJie;
import cn.yumutech.netUtil.Api;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FriendsUrlActivity extends BaseActivity {
    Subscription subscription;
    YouQIngLianJie data;
    private YouQIngAdapter mAdapter;
    private ListView listView;

    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_friends_url;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        listView = (ListView) findViewById(R.id.listview);
        mAdapter = new YouQIngAdapter(this,data);
listView.setAdapter(mAdapter);
        controlTitle(findViewById(R.id.back));

    }

    @Override
    protected void initData() {
        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
               null);
        initDatas1(new Gson().toJson(canshus));
    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getFrindeUrl(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    @Override
    protected void initListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(FriendsUrlActivity.this,FriendDetasActivity.class);
                intent.putExtra("url",mAdapter.getItem(i).getHref());
                intent.putExtra("title",mAdapter.getItem(i).getTitle());
                startActivity(intent);
            }
        });

    }
    Observer<YouQIngLianJie> observer = new Observer<YouQIngLianJie>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(YouQIngLianJie channels) {
            if(channels.status.code.equals("0")){
                data=channels;
                mAdapter.dataChange(channels);
            }

        }
    };
}
