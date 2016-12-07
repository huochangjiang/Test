package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.XianStatusListAdapter;
import cn.yumutech.bean.XianStatusList;
import cn.yumutech.bean.XianStatusListBeen;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.StringUtils1;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/7.
 */
public class XianStatusListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView recyclerView;
    private SwipeRefreshLayout pullToRefresh;
    private List<XianStatusList.DataBean> mdatas=new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private XianStatusListAdapter mAdapter;
    private View tishi;
    private boolean isHave;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private boolean isRefresh=false;
    private View myprog;
    private int mPage=0;
    private int mPageSize = 10;
    private App app;
    private View net_connect;
    Subscription subscription;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_xian_status_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app= (App) XianStatusListActivity.this.getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyleview);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        tishi=findViewById(R.id.tishi);
        tishi.setVisibility(View.GONE);
        myprog=findViewById(R.id.myprog);
        mAdapter = new XianStatusListAdapter(this,mdatas);
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
        //获取分类信息，。暂时没得
//        initClassData();
    }
    //加载缓存
    private void initLocal() {
        String readHomeJson = app.readHomeJson("XianStatusList");// 首页内容
        if (!StringUtils1.isEmpty(readHomeJson)) {
            XianStatusList data = new Gson().fromJson(readHomeJson, XianStatusList.class);
            loadHome(data.data);

        }else{
            if(!app.isNetworkConnected(this)){
                tishi.setVisibility(View.GONE);
                net_connect.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                myprog.setVisibility(View.GONE);
            }
        }
        if (app.isNetworkConnected(XianStatusListActivity.this)) {
            initData();
        }
    }
    /**
     * 加载列表数据
     */
    private void loadHome(List<XianStatusList.DataBean> data){
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
        if(mdatas.isEmpty()){
            tishi.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            tishi.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        myprog.setVisibility(View.GONE);
        net_connect.setVisibility(View.GONE);

    }
    @Override
    protected void initData() {
        if(App.getContext().getLogo("logo")!=null){
            XianStatusListBeen canshus=new XianStatusListBeen(new XianStatusListBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.id),
                    new XianStatusListBeen.DataBean(mPage+"",mPageSize+""));
            initDatas1(new Gson().toJson(canshus));
        }else {
            App.getContext().noLogin(XianStatusListActivity.this);
        }

    }

    @Override
    protected void initListeners() {
        mAdapter.setLisener(new XianStatusListAdapter.OnitemClick() {
            @Override
            public void onitemClice(XianStatusList.DataBean data) {
                Intent intent=new Intent(XianStatusListActivity.this,XianStatusListDetailActivity.class);
                intent.putExtra("id",data.id);
                startActivity(intent);
            }
        });
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.isNetworkConnected(XianStatusListActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    myprog.setVisibility(View.VISIBLE);
                    tishi.setVisibility(View.GONE);
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
                            XianStatusListBeen canshus=new XianStatusListBeen(new XianStatusListBeen.UserBean(App.getContext().getLogo("logo").data.nickname,App.getContext().getLogo("logo").data.id),
                                    new XianStatusListBeen.DataBean(mPage+"",mPageSize+""));
                            initDatas1(new Gson().toJson(canshus));
                        }else {
                            App.getContext().noLogin(XianStatusListActivity.this);
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
        subscription = Api.getMangoApi1().getXianStatusList(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    Observer<XianStatusList> observer = new Observer<XianStatusList>() {
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
        public void onNext(XianStatusList channels) {
            if(channels.status.code.equals("0")){
                if(mPage==0){
                    app.savaHomeJson("XianStatusList",new Gson().toJson(channels));
                }
                loadHome(channels.data);
            }
            isMoreLoading = false;
            pullToRefresh.setRefreshing(false);
        }
    };
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
                    if(App.getContext().getLogo("logo")!=null){
                        XianStatusListBeen canshus=new XianStatusListBeen(new XianStatusListBeen.UserBean(App.getContext().getLogo("logo").data.nickname,App.getContext().getLogo("logo").data.id),
                                new XianStatusListBeen.DataBean(mPage+"",mPageSize+""));
                        initDatas1(new Gson().toJson(canshus));
                    }else {
                        App.getContext().noLogin(XianStatusListActivity.this);
                    }
                    break;
            }
        }
    };
}
