package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.LeaderActivityAdapter;
import cn.yumutech.bean.LeaderActivitys;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.StringUtils1;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LeaderActivitysActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView recyclerView;
    private SwipeRefreshLayout pullToRefresh;
    private LeaderActivityAdapter mAdapter;
    private List<LeaderActivitys.DataBean> leaderActivitys=new ArrayList<>();
    Subscription subscription;
    private int mPage=0;
    private int mPageSize = 10;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private App app;
    private View net_connect;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private boolean isRefresh=false;
    private boolean isHave;
    private View myprog;
    private String fenlei="";
    private LinearLayout ll_feilei;
    private Button bt1,bt2,bt3;
    private List<Button> bts = new ArrayList<>();
    //判断上面的分类按钮是否被点击
    private boolean isClick1,isClick2,isClick3;
    private View tishi;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_leader_activitys;
    }
    @Override
    protected void initViews(Bundle savedInstanceState) {
        app= (App) LeaderActivitysActivity.this.getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyleview);
        ll_feilei= (LinearLayout) findViewById(R.id.ll_feilei);
        bt1= (Button) findViewById(R.id.bt1);
        bt2= (Button) findViewById(R.id.bt2);
        bt3= (Button) findViewById(R.id.bt3);
        bts.add(bt1);
        bts.add(bt2);
        bts.add(bt3);
        tishi=findViewById(R.id.tishi);
        myprog=  findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
        tishi.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        mAdapter = new LeaderActivityAdapter(this,leaderActivitys);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        pullToRefresh.setOnRefreshListener(this);
        controlTitle(findViewById(R.id.back));
        net_connect = findViewById(R.id.netconnect);
        initLocal();
    }
    //加载缓存
    private void initLocal() {
        String readHomeJson = app.readHomeJson("LeaderActivitys");// 首页内容
        if (!StringUtils1.isEmpty(readHomeJson)) {
            LeaderActivitys data = new Gson().fromJson(readHomeJson, LeaderActivitys.class);
            loadHome(data.data);

        }else{
            if(!app.isNetworkConnected(this)){
                myprog.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                ll_feilei.setVisibility(View.GONE);
                tishi.setVisibility(View.GONE);
                net_connect.setVisibility(View.VISIBLE);
            }
        }
        if (app.isNetworkConnected(LeaderActivitysActivity.this)) {
            initData();
        }
    }
    @Override
    protected void initData() {
        if(App.getContext().getLogo("logo")!=null) {
            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.nickname,App.getContext().getLogo("logo").data.id),
                    new RequestCanShu.DataBean(fenlei,mPage+"",mPageSize+""));
            initDatas1(new Gson().toJson(canshus));
        }else {
            Toast.makeText(this,"您还未登陆",Toast.LENGTH_SHORT).show();
        }

    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getLeaderActiviys(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    @Override
    protected void initListeners() {
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClick1){
                    isClick1=false;
                    fenlei="";
                    bt1.setBackgroundResource(R.drawable.logo_no);
                }else{
                    isClick1=true;
                    isClick2=false;
                    isClick3=false;
                    fenlei=bt1.getText().toString().trim();
                    chanColor(0);
                }
                mHandler.sendEmptyMessage(1);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClick2){
                    isClick2=false;
                    fenlei="";
                    bt2.setBackgroundResource(R.drawable.logo_no);
                }else{
                    isClick1=false;
                    isClick2=true;
                    isClick3=false;
                    fenlei=bt2.getText().toString().trim();
                    chanColor(1);
                }
                mHandler.sendEmptyMessage(1);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClick3){
                    isClick3=false;
                    fenlei="";
                    bt3.setBackgroundResource(R.drawable.logo_no);
                }else{
                    isClick1=false;
                    isClick2=false;
                    isClick3=true;
                    fenlei=bt3.getText().toString().trim();
                    chanColor(2);
                }
                mHandler.sendEmptyMessage(1);
            }
        });

        mAdapter.setLisener(new LeaderActivityAdapter.OnitemClick() {
            @Override
            public void onitemClice(LeaderActivitys.DataBean data) {
                Intent intent=new Intent(LeaderActivitysActivity.this,LeadersDetaislActivity.class);
                intent.putExtra("id",data.id);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    if (!isMoreLoading) {
                        isMoreLoading = true;
                        isRefresh=true;
                        mPage=leaderActivitys.size();
                        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.nickname,App.getContext().getLogo("logo").data.id),
                                new RequestCanShu.DataBean(fenlei,mPage+"",mPageSize+""));
                        initDatas1(new Gson().toJson(canshus));
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.isNetworkConnected(LeaderActivitysActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    ll_feilei.setVisibility(View.VISIBLE);
                    myprog.setVisibility(View.VISIBLE);
                    tishi.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    initData();
                }
            }
        });
    }
    Observer<LeaderActivitys> observer = new Observer<LeaderActivitys>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
            pullToRefresh.setRefreshing(false);
            isMoreLoading = false;
            net_connect.setVisibility(View.GONE);
        }
        @Override
        public void onError(Throwable e) {
            pullToRefresh.setRefreshing(false);
            e.printStackTrace();
            isMoreLoading = false;
        }
        @Override
        public void onNext(LeaderActivitys channels) {
            if(channels.status.code.equals("0")){
//                if(channels.data.size()>0){
                    loadHome(channels.data);
//                }
                if(mPage==0){
                    app.savaHomeJson("LeaderActivitys",new Gson().toJson(channels));
                }
            }
        }
    };
    /**
     * 加载列表数据
     */
    private void loadHome(List<LeaderActivitys.DataBean> data){
        if(data.size()==0){
            isHave=false;
        }
        if(data.size()>0){
            isHave=true;
        }
        if(isRefresh){
            leaderActivitys.addAll(data);
        }else {
            leaderActivitys=data;
        }
        myprog.setVisibility(View.GONE);
        mAdapter.dataChange(leaderActivitys,isHave);
        if(leaderActivitys.isEmpty()){
            tishi.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            tishi.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        net_connect.setVisibility(View.GONE);
        ll_feilei.setVisibility(View.VISIBLE);
        pullToRefresh.setRefreshing(false);
        isMoreLoading = false;
    }
    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessage(1);
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    mPage=0;
                    isRefresh=false;
                    RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                            new RequestCanShu.DataBean(fenlei,mPage+"",mPageSize+""));
                    initDatas1(new Gson().toJson(canshus));
                    break;
            }
        }
    };
    //判断那个button变背景
    private void chanColor(int postion) {
        for (int i = 0; i < bts.size(); i++) {
            TextView bt = bts.get(i);
            if (i == postion) {
                bt.setBackgroundResource(R.drawable.logo);
            } else {
                bt.setBackgroundResource(R.drawable.logo_no);
            }
        }
    }
}
