package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import cn.yumutech.Adapter.YouQIngAdapter;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.YouQIngLianJie;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.StringUtils1;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FriendsUrlActivity extends BaseActivity {
    Subscription subscription;
    private List<YouQIngLianJie.DataBean> data;
    private YouQIngAdapter mAdapter;
    private ListView listView;
    private App app;
    private View net_connect;
    private View myprog;
    private View tishi;
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
        app=App.getContext();
        listView = (ListView) findViewById(R.id.listview);
        tishi=findViewById(R.id.tishi);
        tishi.setVisibility(View.GONE);
        mAdapter = new YouQIngAdapter(this,data);
        listView.setAdapter(mAdapter);
        controlTitle(findViewById(R.id.back));
        net_connect = findViewById(R.id.netconnect);
        myprog=findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        initLocal();
    }
    //加载缓存
    private void initLocal() {
        String readHomeJson = app.readHomeJson("YouQIngLianJie");// 首页内容
        if (!StringUtils1.isEmpty(readHomeJson)) {
            YouQIngLianJie data = new Gson().fromJson(readHomeJson, YouQIngLianJie.class);
            loadHome(data.data);
        }else{
            if(!app.isNetworkConnected(this)){
                listView.setVisibility(View.GONE);
                myprog.setVisibility(View.GONE);
                tishi.setVisibility(View.GONE);
                net_connect.setVisibility(View.VISIBLE);
            }
        }
        if (app.isNetworkConnected(FriendsUrlActivity.this)) {
            initData();
        }
    }
    /**
     * 加载列表数据
     */
    private void loadHome(List<YouQIngLianJie.DataBean> channels){
        if(channels.isEmpty()){
            tishi.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else {
            tishi.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        myprog.setVisibility(View.GONE);

        data=channels;
        mAdapter.dataChange(channels);
    }
    @Override
    protected void initData() {
        if(App.getContext().getLogo("logo")!=null){
            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.nickname,
                    App.getContext().getLogo("logo").data.id),
                    null);
            initDatas1(new Gson().toJson(canshus));
        }else {
            App.getContext().noLogin(FriendsUrlActivity.this);
        }

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
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.isNetworkConnected(FriendsUrlActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    myprog.setVisibility(View.VISIBLE);
                    tishi.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    initData();
                }
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
                app.savaHomeJson("YouQIngLianJie",new Gson().toJson(channels));
                loadHome(channels.data);
            }
        }
    };
}
