package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import cn.yumutech.Adapter.PolicyAdapter;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.ZhengCeFile;
import cn.yumutech.netUtil.Api;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PolicyFileActivity extends BaseActivity  implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView recyclerView;
    private SwipeRefreshLayout pullToRefresh;
    ZhengCeFile mdatas;

    Subscription subscription;
    private PolicyAdapter mAdapter;
    private int mPage=1;
    private int mPageSize = 15;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_policy_file;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        recyclerView = (RecyclerView) findViewById(R.id.recyleview);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        mAdapter = new PolicyAdapter(this,mdatas);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        pullToRefresh.setOnRefreshListener(this);
        controlTitle(findViewById(R.id.back));
    }

    @Override
    protected void initData() {
        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                new RequestCanShu.DataBean("中央","0","10"));
        initDatas1(new Gson().toJson(canshus));
    }

    @Override
    protected void initListeners() {
        mAdapter.setLisener(new PolicyAdapter.OnitemClick() {
            @Override
            public void onitemClice(ZhengCeFile.DataBean data) {
                Intent intent=new Intent(PolicyFileActivity.this,PolyDetasActivity.class);
                intent.putExtra("id",data.id);
                startActivity(intent);

            }
        });
    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getPolicy(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    Observer<ZhengCeFile> observer = new Observer<ZhengCeFile>() {
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
        public void onNext(ZhengCeFile channels) {
            if(channels.status.code.equals("0")){
                mdatas=channels;
                mAdapter.dataChange(channels);
            }
            pullToRefresh.setRefreshing(false);

        }
    };

    @Override
    public void onRefresh() {
        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                new RequestCanShu.DataBean("中央","0","10"));
        initDatas1(new Gson().toJson(canshus));
    }
}
