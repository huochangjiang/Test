package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
    private int mPageSize = 5;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private App app;
    private View net_connect;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private boolean isRefresh=false;
    private boolean isHave;
    private View myprog;
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
        myprog=  findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
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
                net_connect.setVisibility(View.VISIBLE);
            }
        }
        if (app.isNetworkConnected(LeaderActivitysActivity.this)) {
            initData();
        }
    }
    @Override
    protected void initData() {
        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                new RequestCanShu.DataBean("省级",mPage+"",mPageSize+""));
        initDatas1(new Gson().toJson(canshus));
    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getLeaderActiviys(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    @Override
    protected void initListeners() {
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
                        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                                new RequestCanShu.DataBean("省级",mPage+"",mPageSize+""));
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
                    myprog.setVisibility(View.VISIBLE);
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
        recyclerView.setVisibility(View.VISIBLE);
        net_connect.setVisibility(View.GONE);
        pullToRefresh.setRefreshing(false);
        isMoreLoading = false;
    }
    @Override
    public void onRefresh() {
        mPage=0;
        isRefresh=false;
        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                new RequestCanShu.DataBean("省级",mPage+"",mPageSize+""));
        initDatas1(new Gson().toJson(canshus));
    }
}
