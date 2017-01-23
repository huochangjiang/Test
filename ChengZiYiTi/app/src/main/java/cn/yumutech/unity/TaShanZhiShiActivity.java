package cn.yumutech.unity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.List;

import cn.yumutech.Adapter.TaShanZhiShiAdapter;
import cn.yumutech.bean.ExchangeListBeen;
import cn.yumutech.bean.ExchangeSearchBeen;
import cn.yumutech.bean.HuDongJIaoLiu;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.MyEditText;
import cn.yumutech.weight.StringUtils1;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 2016/11/13.
 */
public class TaShanZhiShiActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollView>{
    private ImageView back;
    private ListView listview;
    Subscription subscription;
    Subscription subscription1;
    private TaShanZhiShiAdapter adapter;
    private List<HuDongJIaoLiu.DataBean> mData;
    private App app;
    private View net_connect;
    private PullToRefreshScrollView pullToRefresh;
    private View myprog;
    private int mPage=0;
    private int mPageSize = 10;
    private int mPageSearch=0;
    private MyEditText search;
    private boolean isSearch;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_tashanzhishi;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app= (App) TaShanZhiShiActivity.this.getApplicationContext();
        back = (ImageView) findViewById(R.id.back);
        listview = (ListView) findViewById(R.id.listview);
        adapter=new TaShanZhiShiAdapter(TaShanZhiShiActivity.this,mData);
        listview.setAdapter(adapter);
        net_connect = findViewById(R.id.netconnect);
        myprog=  findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
        listview.setVisibility(View.GONE);
        search= (MyEditText) findViewById(R.id.search);

        pullToRefresh = (PullToRefreshScrollView) findViewById(R.id.pull_to_refresh);
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefresh.setOnRefreshListener(this);
        //下拉刷新设置
        ILoadingLayout startLabels = pullToRefresh
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        //上拉加载更多设置
        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        initLocal();
    }
    //搜索到的内容的结果
    private void initSearch(String key) {
        if(App.getContext().getLogo("logo")!=null) {
            ExchangeSearchBeen canshus=new ExchangeSearchBeen(new ExchangeSearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                    new ExchangeSearchBeen.DataBean(key,mPageSearch+"",mPageSize+""));
            initSearch1(new Gson().toJson(canshus));
        }else {
//            Toast.makeText(this,"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }

    private void initSearch1(String canshu) {
        subscription1 = Api.getMangoApi1().getExchangeSearch(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);
    }
    Observer<HuDongJIaoLiu> observer2=new Observer<HuDongJIaoLiu>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription1);
            pullToRefresh.setRefreshing(false);
            net_connect.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            pullToRefresh.setRefreshing(false);
            e.printStackTrace();
        }

        @Override
        public void onNext(HuDongJIaoLiu huDongItem) {
            if(huDongItem!=null&&huDongItem.status.code.equals("0")){
                if(isShangla){
                    mData.addAll(huDongItem.data);
                }else {
                    mData=huDongItem.data;
                }
                app.savaHomeJson("tslist",new Gson().toJson(huDongItem));
                loadHome(mData);
            }
            pullToRefresh.onRefreshComplete();
        }
    };
    @Override
    protected void initData() {
        getData();
    }

    @Override
    protected void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setClass(TaShanZhiShiActivity.this,TaShanDetailActivity.class);
                if(mData!=null&&mData!=null&&mData.get(i).id!=null){
                    intent.putExtra("id",mData.get(i).id);
                }
                startActivity(intent);
            }
        });
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.isNetworkConnected(TaShanZhiShiActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    myprog.setVisibility(View.VISIBLE);
                    initData();
                }
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
                        isSearch=true;
                        initSearch(search.getText().toString().trim());
                    }else {
                        isSearch=false;
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
                    isSearch=false;
                    getData();
                }
            }
        });
    }

    /**
     * 获取他山之石列表数据
     */
    public void getData() {
        ExchangeListBeen exchangeItemBeen = new ExchangeListBeen(new ExchangeListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                new ExchangeListBeen.DataBean("国内","0","5"));
        getData1(new Gson().toJson(exchangeItemBeen));
    }

    public void getData1(String data) {
        subscription = Api.getMangoApi1().getHuDongJiaoLiu(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<HuDongJIaoLiu> observer = new Observer<HuDongJIaoLiu>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
            pullToRefresh.onRefreshComplete();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            pullToRefresh.onRefreshComplete();

        }

        @Override
        public void onNext(HuDongJIaoLiu huDongItem) {
            if(huDongItem!=null&&huDongItem.status.code.equals("0")){
                if(isShangla){
                    mData.addAll(huDongItem.data);
                }else {
                    mData=huDongItem.data;
                }
               loadHome(mData);
            }
            pullToRefresh.onRefreshComplete();
        }
    };

    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    /**
     * 加载列表数据
     */
    private void loadHome(List<HuDongJIaoLiu.DataBean> data){
        adapter.dataChange(data);
        myprog.setVisibility(View.GONE);
        net_connect.setVisibility(View.GONE);
        listview.setVisibility(View.VISIBLE);
    }
    private void initLocal() {
        String readHomeJson = app.readHomeJson("tslist");// 首页内容
        if (!StringUtils1.isEmpty(readHomeJson)) {
            HuDongJIaoLiu data = new Gson().fromJson(readHomeJson, HuDongJIaoLiu.class);
            loadHome(data.data);

        }else{
            if(!app.isNetworkConnected(this)){
                net_connect.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
                myprog.setVisibility(View.GONE);
            }
        }
        if (app.isNetworkConnected(TaShanZhiShiActivity.this)) {
            initData();
        }
    }
    private int page=0;
    private boolean isShangla=false;
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if(isSearch){
            isSearch=true;
            isShangla=false;
            mPageSearch=0;
            if(App.getContext().getLogo("logo")!=null) {
                ExchangeSearchBeen canshus=new ExchangeSearchBeen(new ExchangeSearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                        new ExchangeSearchBeen.DataBean(search.getText().toString().trim(),mPageSearch+"",mPageSize+""));
                initSearch1(new Gson().toJson(canshus));
            }
        }else {
            isShangla=false;
            isSearch=false;
            page=0;
            ExchangeListBeen exchangeItemBeen = new ExchangeListBeen(new ExchangeListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new ExchangeListBeen.DataBean("国内",page+"","5"));
            getData1(new Gson().toJson(exchangeItemBeen));
        }

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if(isSearch){
            mPageSearch=mData.size();
            isShangla=true;
            isSearch=true;
            if(App.getContext().getLogo("logo")!=null) {
                ExchangeSearchBeen canshus=new ExchangeSearchBeen(new ExchangeSearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                        new ExchangeSearchBeen.DataBean(search.getText().toString().trim(),mPageSearch+"",mPageSize+""));
                initSearch1(new Gson().toJson(canshus));
            }
        }else {
            isSearch=false;
            page=mData.size();
            isShangla=true;
            ExchangeListBeen exchangeItemBeen = new ExchangeListBeen(new ExchangeListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new ExchangeListBeen.DataBean("国内",page+"","5"));
            getData1(new Gson().toJson(exchangeItemBeen));
        }

    }
}
