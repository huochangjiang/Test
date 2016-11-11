package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import cn.yumutech.Adapter.LeaderActivityAdapter;
import cn.yumutech.bean.LeaderActivitys;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.netUtil.Api;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LeaderActivitysActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView recyclerView;
    private SwipeRefreshLayout pullToRefresh;
    private LeaderActivityAdapter mAdapter;
    LeaderActivitys leaderActivitys;
    Subscription subscription;
    private int mPage=0;
    private int mPageSize = 10;
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
        recyclerView = (RecyclerView) findViewById(R.id.recyleview);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        mAdapter = new LeaderActivityAdapter(this,leaderActivitys);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        pullToRefresh.setOnRefreshListener(this);



        controlTitle(findViewById(R.id.back));
    }

    @Override
    protected void initData() {
        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                new RequestCanShu.DataBean("省级","0","10"));
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
    }
    Observer<LeaderActivitys> observer = new Observer<LeaderActivitys>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
            pullToRefresh.setRefreshing(false);
        }
        @Override
        public void onError(Throwable e) {
            pullToRefresh.setRefreshing(false);
            e.printStackTrace();

        }
        @Override
        public void onNext(LeaderActivitys channels) {
            if(channels.status.code.equals("0")){
                leaderActivitys=channels;
                mAdapter.dataChange(channels);
            }
            pullToRefresh.setRefreshing(false);

        }
    };

    @Override
    public void onRefresh() {
    mPage=1;
        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                new RequestCanShu.DataBean("省级","0","10"));
        initDatas1(new Gson().toJson(canshus));
    }
}
