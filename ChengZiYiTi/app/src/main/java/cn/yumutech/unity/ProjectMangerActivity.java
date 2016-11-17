package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import java.util.List;

import cn.yumutech.Adapter.ProjectMangerAdpater;
import cn.yumutech.bean.ProjectManger;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.StringUtils1;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProjectMangerActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView recyclerView;
    private SwipeRefreshLayout pullToRefresh;
    private ProjectMangerAdpater mAdapter;
    private List<ProjectManger.DataBean> leaderActivitys;
    Subscription subscription;
    private View net_connect;
    private App app;
    private int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private boolean isRefresh=false;
    private int mPage=0;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_manger;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app=App.getContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyleview);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        mAdapter = new ProjectMangerAdpater(this,leaderActivitys);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        controlTitle(findViewById(R.id.back));
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        pullToRefresh.setOnRefreshListener(this);
        net_connect = findViewById(R.id.netconnect);
        initLocal();
    }
    //加载缓存
    private void initLocal() {
        String readHomeJson = app.readHomeJson("ProjectManger");// 首页内容
        if (!StringUtils1.isEmpty(readHomeJson)) {
            ProjectManger data = new Gson().fromJson(readHomeJson, ProjectManger.class);
            loadHome(data.data);
        }else{
            if(!app.isNetworkConnected(this)){
                net_connect.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
        if (app.isNetworkConnected(ProjectMangerActivity.this)) {
            initData();
        }
    }
    /**
     * 加载列表数据
     */
    private void loadHome(List<ProjectManger.DataBean> data){
        if(isRefresh){
            leaderActivitys.addAll(data);
        }else {
            leaderActivitys=data;
        }
        net_connect.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        mAdapter.dataChange(leaderActivitys);
    }
    @Override
    protected void initData() {
        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                new RequestCanShu.DataBean("资阳",mPage+"","5"));
        initDatas1(new Gson().toJson(canshus));
    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getProject(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    @Override
    protected void initListeners() {
        mAdapter.setLisener(new ProjectMangerAdpater.OnitemClick() {
            @Override
            public void onitemClice(ProjectManger.DataBean data) {
                Intent intent=new Intent(ProjectMangerActivity.this,LeadersDetaislActivity.class);
                intent.putExtra("id",data.id);
                intent.putExtra("type","3");
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
                                new RequestCanShu.DataBean("省级",mPage+"","5"));
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
                if(app.isNetworkConnected(ProjectMangerActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    initData();
                }
            }
        });
    }
    Observer<ProjectManger> observer = new Observer<ProjectManger>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
            isMoreLoading = false;
            pullToRefresh.setRefreshing(false);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            isMoreLoading = false;
            pullToRefresh.setRefreshing(false);

        }
        @Override
        public void onNext(ProjectManger channels) {
            if(channels.status.code.equals("0")){
                if(mPage==0){
                    app.savaHomeJson("ProjectManger",new Gson().toJson(channels));
                }
                loadHome(channels.data);
            }
            isMoreLoading = false;
        pullToRefresh.setRefreshing(false);
        }
    };

    @Override
    public void onRefresh() {
        isRefresh=false;
        mPage=0;
        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                new RequestCanShu.DataBean("资阳",mPage+"","5"));
        initDatas1(new Gson().toJson(canshus));
    }
}
