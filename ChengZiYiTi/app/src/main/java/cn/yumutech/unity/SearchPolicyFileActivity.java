package cn.yumutech.unity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.PolicyAdapter;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.ZhengCeFile;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.MyEditText;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/2/18.
 */
public class SearchPolicyFileActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private MyEditText search;
    private String fenlei;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout pullToRefresh;
    private PolicyAdapter mAdapter;
    private List<ZhengCeFile.DataBean> leaderActivitys=new ArrayList<>();
    private int mPage=0;
    private int mPageSize = 10;
    private int mPageSearch=0;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;
    Subscription subscription;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private boolean isRefresh=false;
    private View myprog;
    private boolean isHave;
    private LinearLayout ll_feilei;
    private View tishi;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_policy_file;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initExtra();
        controlTitle(findViewById(R.id.back));
        search= (MyEditText) findViewById(R.id.search);
        recyclerView = (RecyclerView) findViewById(R.id.recyleview);
        recyclerView.setVisibility(View.GONE);
        ll_feilei= (LinearLayout) findViewById(R.id.ll_feilei);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        mAdapter = new PolicyAdapter(this,leaderActivitys);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        pullToRefresh.setOnRefreshListener(this);
        myprog=  findViewById(R.id.myprog);
        tishi=findViewById(R.id.tishi);
        tishi.setVisibility(View.GONE);
    }
    private void initExtra() {
        if(getIntent()!=null){
            fenlei=getIntent().getStringExtra("fenlei");
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {
        mAdapter.setLisener(new PolicyAdapter.OnitemClick() {
            @Override
            public void onitemClice(ZhengCeFile.DataBean data) {
                Intent intent=new Intent(SearchPolicyFileActivity.this,PolyDetasActivity.class);
                intent.putExtra("id",data.id);
                startActivity(intent);
            }
        });
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction()==KeyEvent.ACTION_UP){
                    if(keyCode == KeyEvent.KEYCODE_ENTER){
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isActive()) {
                            inputMethodManager.hideSoftInputFromWindow(
                                    v.getApplicationWindowToken(), 0);
                        }
                    }
                    if(search.getText().toString().length()>0){
                        mPageSearch=0;
                        isRefresh=false;
                        initSearch(search.getText().toString().trim());
                    }else {
//                        search.setText("");
                    }
                }
                return false;
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(search.getText().toString().trim().length()==0){
                    recyclerView.setVisibility(View.GONE);
                    tishi.setVisibility(View.GONE);
//                    search.setText("");
                }
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    isMoreLoading = true;
                    isRefresh=true;
                    mPageSearch=leaderActivitys.size();
                    if(App.getContext().getLogo("logo")!=null) {
                        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                                new RequestCanShu.DataBean(fenlei,search.getText().toString().trim(),mPageSearch+"",mPageSize+""));
//                            LeaderActivitySearchBeen canshus=new LeaderActivitySearchBeen(new LeaderActivitySearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
//                                    new LeaderActivitySearchBeen.DataBean(search.getText().toString().trim(),mPageSearch+"",mPageSize+""));
                        initSearch1(new Gson().toJson(canshus));
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

    @Override
    public void onRefresh() {
        mPageSearch=0;
        isRefresh=false;
        isMoreLoading=true;
        initSearch(search.getText().toString().trim());
    }
    //搜索到的内容的结果
    private void initSearch(String key) {
        if(App.getContext().getLogo("logo")!=null) {
            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                    new RequestCanShu.DataBean(fenlei,key,mPageSearch+"",mPageSize+""));
//            PolicyFileSearchBeen canshus=new PolicyFileSearchBeen(new PolicyFileSearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
//                    new PolicyFileSearchBeen.DataBean(key,mPageSearch+"",mPageSize+""));
            initSearch1(new Gson().toJson(canshus));
        }else {
//            Toast.makeText(this,"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }
    private void initSearch1(String canshu) {
        subscription = Api.getMangoApi1().getPolicy(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);
    }
    Observer<ZhengCeFile> observer2=new Observer<ZhengCeFile>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
            pullToRefresh.setRefreshing(false);
            isMoreLoading = false;
        }

        @Override
        public void onError(Throwable e) {
            pullToRefresh.setRefreshing(false);
            e.printStackTrace();
            isMoreLoading = false;
        }

        @Override
        public void onNext(ZhengCeFile channels) {
            if(channels.status.code.equals("0")){
                loadHome(channels.data);
            }
            isMoreLoading = false;
            pullToRefresh.setRefreshing(false);
        }
    };
    /**
     * 加载列表数据
     */
    private void loadHome(List<ZhengCeFile.DataBean> data){
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

        ll_feilei.setVisibility(View.VISIBLE);
        pullToRefresh.setRefreshing(false);
        isMoreLoading = false;
    }
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
