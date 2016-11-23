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

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.PolicyAdapter;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.ZhengCeFile;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.StringUtils1;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PolicyFileActivity extends BaseActivity  implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView recyclerView;
    private SwipeRefreshLayout pullToRefresh;
    private List<ZhengCeFile.DataBean> mdatas=new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    Subscription subscription;
    private PolicyAdapter mAdapter;
    private int mPage=0;
    private int mPageSize = 10;
    private View net_connect;
    private App app;
    private int lastVisibleItem;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private boolean isRefresh=false;
    private View myprog;
    private LinearLayout ll_feilei;
    private Button bt1,bt2,bt3,bt4,bt5,bt6;
    private String fenlei="";
    //是否还是有数据
    private boolean isHave;
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
        app= (App) PolicyFileActivity.this.getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyleview);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        ll_feilei= (LinearLayout) findViewById(R.id.ll_feilei);
        bt1= (Button) findViewById(R.id.bt1);
        bt2= (Button) findViewById(R.id.bt2);
        bt3= (Button) findViewById(R.id.bt3);
        bt4= (Button) findViewById(R.id.bt4);
        bt5= (Button) findViewById(R.id.bt5);
        bt6= (Button) findViewById(R.id.bt6);
        myprog=findViewById(R.id.myprog);
        mAdapter = new PolicyAdapter(this,mdatas);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        pullToRefresh.setOnRefreshListener(this);
        controlTitle(findViewById(R.id.back));
        net_connect = findViewById(R.id.netconnect);
        myprog.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        initLocal();
    }
    //加载缓存
    private void initLocal() {
        String readHomeJson = app.readHomeJson("ZhengCeFile");// 首页内容
        if (!StringUtils1.isEmpty(readHomeJson)) {
            ZhengCeFile data = new Gson().fromJson(readHomeJson, ZhengCeFile.class);
            loadHome(data.data);

        }else{
            if(!app.isNetworkConnected(this)){
                net_connect.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                ll_feilei.setVisibility(View.GONE);
                myprog.setVisibility(View.GONE);
            }
        }
        if (app.isNetworkConnected(PolicyFileActivity.this)) {
            initData();
        }
    }
    /**
     * 加载列表数据
     */
    private void loadHome(List<ZhengCeFile.DataBean> data){
        if(data.size()>0){
            isHave=true;
        }
        if(data.size()==0){
            isHave=false;
        }
        if(isRefresh){
            mdatas.addAll(data);
        }else {
            mdatas=data;
        }
        mAdapter.dataChange(mdatas,isHave);
        myprog.setVisibility(View.GONE);
        net_connect.setVisibility(View.GONE);
        ll_feilei.setVisibility(View.VISIBLE);
       recyclerView.setVisibility(View.VISIBLE);
    }
    @Override
    protected void initData() {
        if(App.getContext().getLogo("logo")!=null){
            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.nickname,App.getContext().getLogo("logo").data.id),
                    new RequestCanShu.DataBean(fenlei,mPage+"",mPageSize+""));
            initDatas1(new Gson().toJson(canshus));
        }else {
           App.getContext().noLogin(PolicyFileActivity.this);
        }

    }
    @Override
    protected void initListeners() {
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fenlei=bt1.getText().toString().trim();
                mHandler.sendEmptyMessage(1);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fenlei=bt2.getText().toString().trim();
                mHandler.sendEmptyMessage(1);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fenlei=bt3.getText().toString().trim();
                mHandler.sendEmptyMessage(1);
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fenlei=bt4.getText().toString().trim();
                mHandler.sendEmptyMessage(1);
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fenlei=bt5.getText().toString().trim();
                mHandler.sendEmptyMessage(1);
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fenlei=bt6.getText().toString().trim();
                mHandler.sendEmptyMessage(1);
            }
        });
        mAdapter.setLisener(new PolicyAdapter.OnitemClick() {
            @Override
            public void onitemClice(ZhengCeFile.DataBean data) {
                Intent intent=new Intent(PolicyFileActivity.this,PolyDetasActivity.class);
                intent.putExtra("id",data.id);
                startActivity(intent);
            }
        });
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.isNetworkConnected(PolicyFileActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    myprog.setVisibility(View.VISIBLE);
                    ll_feilei.setVisibility(View.VISIBLE);
                    initData();
                }
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
                        mPage=mdatas.size();
                        if(App.getContext().getLogo("logo")!=null){
                            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.nickname,App.getContext().getLogo("logo").data.id),
                                    new RequestCanShu.DataBean(fenlei,mPage+"",mPageSize+""));
                            initDatas1(new Gson().toJson(canshus));
                        }else {
                            App.getContext().noLogin(PolicyFileActivity.this);
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
        public void onNext(ZhengCeFile channels) {
            if(channels.status.code.equals("0")){
                if(mPage==0){
                    app.savaHomeJson("ZhengCeFile",new Gson().toJson(channels));
                }
                loadHome(channels.data);
            }
            isMoreLoading = false;
            pullToRefresh.setRefreshing(false);
        }
    };

    //上拉刷新
    @Override
    public void onRefresh() {
        fenlei="";
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
                    if(App.getContext().getLogo("logo")!=null){
                        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.nickname,App.getContext().getLogo("logo").data.id),
                                new RequestCanShu.DataBean(fenlei,mPage+"",mPageSize+""));
                        initDatas1(new Gson().toJson(canshus));
                    }else {
                        App.getContext().noLogin(PolicyFileActivity.this);
                    }
                    break;
            }
        }
    };
}
