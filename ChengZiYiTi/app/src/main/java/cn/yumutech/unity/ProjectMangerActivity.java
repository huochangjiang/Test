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

public class ProjectMangerActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{
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
    private int mPageSize=10;
    private boolean isHave;
    private View myprog;
    private String fenlei="";
    private LinearLayout ll_feilei;
    private Button bt1,bt2,bt3;
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
        ll_feilei= (LinearLayout) findViewById(R.id.ll_feilei);
        bt1= (Button) findViewById(R.id.bt1);
        bt2= (Button) findViewById(R.id.bt2);
        bt3= (Button) findViewById(R.id.bt3);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        controlTitle(findViewById(R.id.back));
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        pullToRefresh.setOnRefreshListener(this);
        net_connect = findViewById(R.id.netconnect);
        myprog=findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
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
                myprog.setVisibility(View.GONE);
                net_connect.setVisibility(View.VISIBLE);
                ll_feilei.setVisibility(View.GONE);
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
        myprog.setVisibility(View.GONE);
        net_connect.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        ll_feilei.setVisibility(View.VISIBLE);
        mAdapter.dataChange(leaderActivitys,isHave);
    }
    @Override
    protected void initData() {
        if(App.getContext().getLogo("logo")!=null){
            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.nickname,App.getContext().getLogo("logo").data.id),
                    new RequestCanShu.DataBean(fenlei,mPage+"",mPageSize+""));
            initDatas1(new Gson().toJson(canshus));
        }else {
            App.getContext().noLogin(ProjectMangerActivity.this);
        }

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
                        if(App.getContext().getLogo("logo")!=null){
                            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.nickname,App.getContext().getLogo("logo").data.id),
                                    new RequestCanShu.DataBean(fenlei,mPage+"",mPageSize+""));
                            initDatas1(new Gson().toJson(canshus));
                        }else {
                            App.getContext().noLogin(ProjectMangerActivity.this);
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
                if(app.isNetworkConnected(ProjectMangerActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    ll_feilei.setVisibility(View.VISIBLE);
                    myprog.setVisibility(View.VISIBLE);
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
        fenlei="";
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt1:
                fenlei=bt1.getText().toString().trim();
                mHandler.sendEmptyMessage(1);
                break;
            case R.id.bt2:
                fenlei=bt2.getText().toString().trim();
                mHandler.sendEmptyMessage(1);
                break;
            case R.id.bt3:
                fenlei=bt3.getText().toString().trim();
                mHandler.sendEmptyMessage(1);
                break;

        }
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    isRefresh=false;
                    mPage=0;
                    if(App.getContext().getLogo("logo")!=null){
                        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                                new RequestCanShu.DataBean(fenlei,mPage+"",mPageSize+""));
                        initDatas1(new Gson().toJson(canshus));
                    }else {
                        App.getContext().noLogin(ProjectMangerActivity.this);
                    }
                    break;
            }
        }
    };
}
