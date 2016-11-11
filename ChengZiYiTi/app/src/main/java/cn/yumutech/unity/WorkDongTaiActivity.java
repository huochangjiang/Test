package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import cn.yumutech.Adapter.WorkDongTaiAdapter;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.WorkListManger;
import cn.yumutech.netUtil.Api;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WorkDongTaiActivity extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout pullToRefresh;
    private WorkDongTaiAdapter mAdapter;
    WorkListManger leaderActivitys;
    Subscription subscription;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_work_dong_tai;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        recyclerView = (RecyclerView) findViewById(R.id.recyleview);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        mAdapter = new WorkDongTaiAdapter(this,leaderActivitys);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        pullToRefresh.setOnRefreshListener(this);
        controlTitle(findViewById(R.id.back));
    }

    @Override
    protected void initData() {
        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                new RequestCanShu.DataBean("","0","10"));
        initDatas1(new Gson().toJson(canshus));
    }


    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getWorkStatus(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    @Override
    protected void initListeners() {
        mAdapter.setLisener(new WorkDongTaiAdapter.OnitemClick() {
            @Override
            public void onitemClice(WorkListManger.DataBean data) {
                Intent intent=new Intent(WorkDongTaiActivity.this,LeadersDetaislActivity.class);
                intent.putExtra("id",data.id);
                intent.putExtra("type","2");
                startActivity(intent);

            }
        });
    }
    Observer<WorkListManger> observer = new Observer<WorkListManger>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
            pullToRefresh.setRefreshing(false);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            pullToRefresh.setRefreshing(false);

        }
        @Override
        public void onNext(WorkListManger channels) {
            if(channels.status.code.equals("0")){
                leaderActivitys=channels;
                mAdapter.dataChange(channels);
            }
            pullToRefresh.setRefreshing(false);

        }
    };

    @Override
    public void onRefresh() {
        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                new RequestCanShu.DataBean("","0","10"));
        initDatas1(new Gson().toJson(canshus));
    }
}
