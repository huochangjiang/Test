package cn.yumutech.unity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.TaShanZhiShiAdapter;
import cn.yumutech.bean.ExchangeListBeen;
import cn.yumutech.bean.HuDongJIaoLiu;
import cn.yumutech.bean.ModuleClassifyList;
import cn.yumutech.bean.ModuleClassifyListBeen;
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
    private boolean isSearch=false;
    private View tishi;
    private List<TextView> bts = new ArrayList<>();
    private String searchKey="";
    private String fenlei="";
    private List<HorizontalScrollView> hors = new ArrayList<>();
    List<LinearLayout> linears = new ArrayList<LinearLayout>();
    private LinearLayout linearLayout1;
    private HorizontalScrollView diqu;
    private List<HuDongJIaoLiu.DataBean> leaderActivitys=new ArrayList<>();
    private boolean isRefresh=false;
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
        tishi=findViewById(R.id.tishi);
        tishi.setVisibility(View.GONE);

        diqu=(HorizontalScrollView) findViewById(R.id.horscroll_one);
//        bt1= (TextView) findViewById(R.id.bt1);
//        bt2= (TextView) findViewById(R.id.bt2);
//        bt3= (TextView) findViewById(R.id.bt3);
//        bts.add(bt1);
//        bts.add(bt2);
//        bts.add(bt3);
        linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linears.add(linearLayout1);
        hors.add(diqu);


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
        initClassData();
        initLocal();
    }
    /**
     * 获取分类信息
     */
    int shangbiao = 0;
    int xiabiao = 0;
    public void initClassData(){
        if(App.getContext().getLogo("logo")!=null) {
            ModuleClassifyListBeen canshus=new ModuleClassifyListBeen(new ModuleClassifyListBeen.UserBeen(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                    new ModuleClassifyListBeen.DataBeen("Exchange"));
            initClassDatas1(new Gson().toJson(canshus));
        }else {
//            Toast.makeText(this,"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }
    public void initClassDatas1(String canshu){
        subscription1 = Api.getMangoApi1().getModuleClassifyList(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }
    Observer<ModuleClassifyList> observer1=new Observer<ModuleClassifyList>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription1);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(ModuleClassifyList moduleClassifyList) {
            if(moduleClassifyList!=null&&moduleClassifyList.data.size()>0){
                addView(moduleClassifyList.data);
//                for(int i=0;i<moduleClassifyList.data.size();i++){
//                    bts.get(i).setText(moduleClassifyList.data.get(i).value);
//                }
            }
        }
    };
    private void addView(final List<ModuleClassifyList.data> a) {
        hors.get(0).removeAllViews();
        for (int j = 0; j < a.size()+1; j++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT);
            final TextView tv = new TextView(this);

            if(j==0){
                tv.setText("推荐");
                tv.setVisibility(View.GONE);
            }else {
                tv.setText(a.get(j-1).value);
            }

            tv.setTextSize(18);
            bts.add(tv);
            linears.get(0).addView(tv);
            if (j == 1 ) {
//                tv.setBackgroundResource(R.drawable.logo);
                tv.setTextColor(Color.parseColor("#DD3237"));
            } else {
//                tv.setBackgroundResource(R.drawable.logo_no);
                tv.setTextColor(Color.parseColor("#7F000000"));
            }
            tv.setLayoutParams(layoutParams);
            if (!(j==1)) {
                layoutParams.leftMargin = 60;
                tv.setGravity(Gravity.CENTER);
            }
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    setAnimation();
                    isSearch=false;
                    search.setText("");
                    searchKey="";
                    tishi.setVisibility(View.GONE);
                    getIndex(tv);
                    if(xiabiao==0){
                        fenlei="";
                    }else {
                        fenlei=a.get(xiabiao-1).key;
                    }
                    mData.clear();
                    chanColor(xiabiao);
                    mhandler.sendEmptyMessage(0);
                }
            });
        }
        hors.get(0).addView(linears.get(0));
    }
    private void getIndex(TextView tv) {
        for (int i = 0; i < linears.size(); i++) {
            for (int j = 0; j < linears.get(i).getChildCount(); j++) {
                TextView tv1 = (TextView) linears.get(i).getChildAt(j);
                if (tv.equals(tv1)) {
                    xiabiao = j;
                }
            }
        }
    }
    //判断那个button变背景
    private void chanColor(int postion) {
        for (int i = 0; i < bts.size(); i++) {
            TextView bt = bts.get(i);
            if (i == postion) {
//                bt.setBackgroundResource(R.drawable.logo);
                bt.setTextColor(Color.parseColor("#DD3237"));
            } else {
//                bt.setBackgroundResource(R.drawable.logo_no);
                bt.setTextColor(Color.parseColor("#7F000000"));
            }
        }
    }

    //搜索到的内容的结果
    private void initSearch(String key) {
        if(App.getContext().getLogo("logo")!=null) {
            searchKey=key;
            ExchangeListBeen exchangeItemBeen = new ExchangeListBeen(new ExchangeListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new ExchangeListBeen.DataBean(fenlei,searchKey,mPageSearch+"",mPageSize+""));
            getData1(new Gson().toJson(exchangeItemBeen));
//            ExchangeSearchBeen canshus=new ExchangeSearchBeen(new ExchangeSearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
//                    new ExchangeSearchBeen.DataBean(key,mPageSearch+"",mPageSize+""));
//            initSearch1(new Gson().toJson(canshus));
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
            }else {
                tishi.setVisibility(View.VISIBLE);
                myprog.setVisibility(View.GONE);
                net_connect.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
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
                        mData.clear();
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
                    mhandler.sendEmptyMessage(0);
                }
            }
        });
    }

    /**
     * 获取他山之石列表数据
     */
    public void getData() {
        ExchangeListBeen exchangeItemBeen = new ExchangeListBeen(new ExchangeListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                new ExchangeListBeen.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
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
            if(huDongItem!=null&&huDongItem.data!=null&&huDongItem.data.size()>0&&huDongItem.status.code.equals("0")){
                if(isShangla){
                    mData.addAll(huDongItem.data);
                }else {
                    mData=huDongItem.data;
                }
               loadHome(mData);
            }else if(mData!=null&&mData.size()>0){
                loadHome(mData);
            }else {
                tishi.setVisibility(View.VISIBLE);
                myprog.setVisibility(View.GONE);
                net_connect.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                pullToRefresh.setVisibility(View.GONE);
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
        tishi.setVisibility(View.GONE);
        net_connect.setVisibility(View.GONE);
        listview.setVisibility(View.VISIBLE);
        pullToRefresh.setVisibility(View.VISIBLE);
    }
    private void initLocal() {
        String readHomeJson = app.readHomeJson("tslist");// 首页内容
        if (!StringUtils1.isEmpty(readHomeJson)) {
            HuDongJIaoLiu data = new Gson().fromJson(readHomeJson, HuDongJIaoLiu.class);
            loadHome(data.data);

        }else{
            if(!app.isNetworkConnected(this)){
                net_connect.setVisibility(View.VISIBLE);
                tishi.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                myprog.setVisibility(View.GONE);
            }
        }
        if (app.isNetworkConnected(TaShanZhiShiActivity.this)) {
            initData();
        }
    }

    private boolean isShangla=false;
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if(isSearch){
            isSearch=true;
            isShangla=false;
            isRefresh=false;
            mPageSearch=0;
            if(App.getContext().getLogo("logo")!=null) {
                ExchangeListBeen exchangeItemBeen = new ExchangeListBeen(new ExchangeListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                        new ExchangeListBeen.DataBean(fenlei,searchKey,mPageSearch+"",mPageSize+""));
                getData1(new Gson().toJson(exchangeItemBeen));
//                ExchangeSearchBeen canshus=new ExchangeSearchBeen(new ExchangeSearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
//                        new ExchangeSearchBeen.DataBean(search.getText().toString().trim(),mPageSearch+"",mPageSize+""));
//                initSearch1(new Gson().toJson(canshus));
            }
        }else {
            isShangla=false;
            isSearch=false;
            isRefresh=false;
            mPage=0;
            ExchangeListBeen exchangeItemBeen = new ExchangeListBeen(new ExchangeListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new ExchangeListBeen.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
            getData1(new Gson().toJson(exchangeItemBeen));
        }

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if(isSearch){
            mPageSearch=mData.size();
            isShangla=true;
            isSearch=true;
            isRefresh=true;
            if(App.getContext().getLogo("logo")!=null) {
                ExchangeListBeen exchangeItemBeen = new ExchangeListBeen(new ExchangeListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                        new ExchangeListBeen.DataBean(fenlei,searchKey,mPageSearch+"",mPageSize+""));
                getData1(new Gson().toJson(exchangeItemBeen));
//                ExchangeSearchBeen canshus=new ExchangeSearchBeen(new ExchangeSearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
//                        new ExchangeSearchBeen.DataBean(search.getText().toString().trim(),mPageSearch+"",mPageSize+""));
//                initSearch1(new Gson().toJson(canshus));
            }
        }else {
            isSearch=false;
            mPage=mData.size();
            isShangla=true;
            isRefresh=true;
            ExchangeListBeen exchangeItemBeen = new ExchangeListBeen(new ExchangeListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new ExchangeListBeen.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
            getData1(new Gson().toJson(exchangeItemBeen));
        }
    }
    Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if(isSearch){
                        isSearch=true;
                        mPageSearch=0;
                        initSearch(search.getText().toString().trim());
                    }else {
                        mPage=0;
                        searchKey="";
                        ExchangeListBeen exchangeItemBeen = new ExchangeListBeen(new ExchangeListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                                new ExchangeListBeen.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
                        getData1(new Gson().toJson(exchangeItemBeen));
//                        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,"1234567890"),
//                                new RequestCanShu.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
//                        initDatas1(new Gson().toJson(canshus));
                    }
                    break;
            }
        }
    };
//    Handler mHandler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 0:
//                    if(isSearch){
//                        isSearch=true;
//                        mPageSearch=0;
//                        initSearch(search.getText().toString().trim());
//                    }else {
//                        mPage=0;
//                        searchKey="";
//                        ExchangeListBeen exchangeItemBeen = new ExchangeListBeen(new ExchangeListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
//                                new ExchangeListBeen.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
//                        getData1(new Gson().toJson(exchangeItemBeen));
////                        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,"1234567890"),
////                                new RequestCanShu.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
////                        initDatas1(new Gson().toJson(canshus));
//                    }
//                    break;
//            }
//        }
//    };
}
