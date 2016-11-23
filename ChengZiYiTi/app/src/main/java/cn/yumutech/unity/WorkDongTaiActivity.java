package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import java.util.List;

import cn.yumutech.Adapter.WorkDongTaiAdapter;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.WorkListManger;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.StringUtils1;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WorkDongTaiActivity extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout pullToRefresh;
    private WorkDongTaiAdapter mAdapter;
    private List<WorkListManger.DataBean> leaderActivitys;
    Subscription subscription;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private App app;
    private View net_connect;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private boolean isRefresh=false;
    private int mPage=0;
    private int mPageSize=10;
    //是否还有数据
    private boolean isHave;
    private View myprog;
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
        app= (App) WorkDongTaiActivity.this.getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyleview);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        mAdapter = new WorkDongTaiAdapter(this,leaderActivitys);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        pullToRefresh.setOnRefreshListener(this);
        controlTitle(findViewById(R.id.back));
        net_connect = findViewById(R.id.netconnect);
        myprog=  findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        initLocal();
    }
    //加载缓存
    private void initLocal() {
        String readHomeJson = app.readHomeJson("WorkListManger");// 首页内容
        if (!StringUtils1.isEmpty(readHomeJson)) {
            WorkListManger data = new Gson().fromJson(readHomeJson, WorkListManger.class);
            loadHome(data.data);

        }else{
            if(!app.isNetworkConnected(this)){
                net_connect.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                myprog.setVisibility(View.GONE);
            }
        }
        if (app.isNetworkConnected(WorkDongTaiActivity.this)) {
            initData();
        }
    }
    @Override
    protected void initData() {
        if(App.getContext().getLogo("logo")!=null) {
            RequestCanShu canshus = new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.nickname, App.getContext().getLogo("logo").data.id),
                    new RequestCanShu.DataBean("", "0", mPageSize + ""));
            initDatas1(new Gson().toJson(canshus));
        }else {
            App.getContext().noLogin(WorkDongTaiActivity.this);
        }
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
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    if (!isMoreLoading) {
                        isMoreLoading = true;
                        isRefresh=true;
                        mPage=leaderActivitys.size();
                        if(App.getContext().getLogo("logo")!=null) {
                            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.nickname,App.getContext().getLogo("logo").data.id),
                                    new RequestCanShu.DataBean("",mPage+"",mPageSize+""));
                            initDatas1(new Gson().toJson(canshus));
                        }else {
                           App.getContext().noLogin(WorkDongTaiActivity.this);
                        }

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
                if(app.isNetworkConnected(WorkDongTaiActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    myprog.setVisibility(View.VISIBLE);
                    initData();
                }
            }
        });
    }
    Observer<WorkListManger> observer = new Observer<WorkListManger>() {
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
        public void onNext(WorkListManger channels) {
            if(channels.status.code.equals("0")){
                if(mPage==0){
                    app.savaHomeJson("WorkListManger",new Gson().toJson(channels));
                }

                    loadHome(channels.data);


            }


        }
    };
    /**
     * 加载列表数据
     */
    private void loadHome(List<WorkListManger.DataBean> data){
        if(data.size()>0){
            isHave=true;
        }
        if(data.size()==0){
            isHave=false;
        }
        if(isRefresh){
            leaderActivitys.addAll(data);
        }else {
            leaderActivitys=data;
        }
        mAdapter.dataChange(leaderActivitys,isHave);
        isMoreLoading = false;
        myprog.setVisibility(View.GONE);
        net_connect.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        pullToRefresh.setRefreshing(false);
    }
    @Override
    public void onRefresh() {
        mPage=0;
        isRefresh=false;
        if(App.getContext().getLogo("logo")!=null){
            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.nickname,App.getContext().getLogo("logo").data.id),
                    new RequestCanShu.DataBean("","0",mPageSize+""));
            initDatas1(new Gson().toJson(canshus));
        }else {
           App.getContext().noLogin(WorkDongTaiActivity.this);
        }

    }
}
