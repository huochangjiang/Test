package cn.yumutech.unity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.LeaderActivityAdapter;
import cn.yumutech.Adapter.SBAdapter;
import cn.yumutech.bean.LeaderActivitys;
import cn.yumutech.bean.ModuleClassifyList;
import cn.yumutech.bean.ModuleClassifyListBeen;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.MyEditText;
import cn.yumutech.weight.StringUtils1;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LeaderActivitysActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView recyclerView;
    private SwipeRefreshLayout pullToRefresh;
    private LeaderActivityAdapter mAdapter;
    private List<LeaderActivitys.DataBean> leaderActivitys=new ArrayList<>();
    private ModuleClassifyList.data oneData;
    private List<ModuleClassifyList.data> myData=new ArrayList<>();
    private List<ModuleClassifyList.data> mData=new ArrayList<>();
    Subscription subscription;
    Subscription subscription1;
    Subscription subscription2;
    private int mPage=0;
    private int mPageSize = 10;
    private int mPageSearch=0;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private App app;
    private View net_connect;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private boolean isRefresh=false;
    private boolean isHave;
    private View myprog;
    private String fenlei;
    private LinearLayout ll_feilei;
    private HorizontalScrollView diqu;
//    private TextView bt1,bt2,bt3;
    private List<TextView> bts = new ArrayList<>();
    //判断上面的分类按钮是否被点击
    private boolean isClick1,isClick2,isClick3;
    private ModuleClassifyList mKey;
    private LinearLayout linearLayout1;
    List<LinearLayout> linears = new ArrayList<LinearLayout>();
    private List<HorizontalScrollView> hors = new ArrayList<>();
    private View tishi;
    private MyEditText search;
    private boolean isSearch;
    private String searchKey="";
    private GridView gridView;
    private SBAdapter adapter;
    private ImageView searchImage;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_leader_activitys;
    }
    @Override
    protected void initViews(Bundle savedInstanceState) {
        app= (App) LeaderActivitysActivity.this.getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyleview);
        ll_feilei= (LinearLayout) findViewById(R.id.ll_feilei);
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
        tishi=findViewById(R.id.tishi);
        myprog=  findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
        tishi.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        gridView= (GridView) findViewById(R.id.gridView);
        adapter=new SBAdapter(LeaderActivitysActivity.this,mData);
        gridView.setAdapter(adapter);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        mAdapter = new LeaderActivityAdapter(this,leaderActivitys);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        pullToRefresh.setOnRefreshListener(this);
        controlTitle(findViewById(R.id.back));
        net_connect = findViewById(R.id.netconnect);
        initLocal();
        initClassData();
        searchImage= (ImageView) findViewById(R.id.searchImage);
        search= (MyEditText) findViewById(R.id.search);
    }

    //搜索到的内容的结果
    private void initSearch(String key) {
        if(App.getContext().getLogo("logo")!=null) {
            searchKey=key;
            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                    new RequestCanShu.DataBean(fenlei,searchKey,mPageSearch+"",mPageSize+""));
            initSearch1(new Gson().toJson(canshus));
        }
//        if(App.getContext().getLogo("logo")!=null) {
//            LeaderActivitySearchBeen canshus=new LeaderActivitySearchBeen(new LeaderActivitySearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
//                    new LeaderActivitySearchBeen.DataBean(key,mPageSearch+"",mPageSize+""));
//            initSearch1(new Gson().toJson(canshus));
//        }else {
////            Toast.makeText(this,"您还未登陆",Toast.LENGTH_SHORT).show();
//        }
    }

    private void initSearch1(String canshu) {
        subscription = Api.getMangoApi1().getLeaderActiviys(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);
    }
    Observer<LeaderActivitys> observer2=new Observer<LeaderActivitys>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
            pullToRefresh.setRefreshing(false);
            isMoreLoading = false;
            net_connect.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            pullToRefresh.setRefreshing(false);
            e.printStackTrace();
            isMoreLoading = false;
        }

        @Override
        public void onNext(LeaderActivitys channels) {
            if(channels.status.code.equals("0")){
//                if(channels.data.size()>0){
                loadHome(channels.data);
//                }

            }
        }
    };

    //加载缓存
    private void initLocal() {
        String readHomeJson = app.readHomeJson("LeaderActivitys");// 首页内容
        if (!StringUtils1.isEmpty(readHomeJson)) {
            LeaderActivitys data = new Gson().fromJson(readHomeJson, LeaderActivitys.class);
            loadHome(data.data);

        }else{
            if(!app.isNetworkConnected(this)){
                myprog.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                ll_feilei.setVisibility(View.GONE);
                tishi.setVisibility(View.GONE);
                net_connect.setVisibility(View.VISIBLE);
            }
        }
        if (app.isNetworkConnected(LeaderActivitysActivity.this)) {
//            initData();
        }
    }
    private void myInitData(){
        if(App.getContext().getLogo("logo")!=null) {
            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                    new RequestCanShu.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
            initDatas1(new Gson().toJson(canshus));
        }else {
            Toast.makeText(this,"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void initData() {

    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getLeaderActiviys(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    @Override
    protected void initListeners() {
//        bt1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isClick1){
//                    isClick1=false;
//                    fenlei="";
//                    bt1.setTextColor(Color.parseColor("#7F000000"));
//                    bt1.setBackgroundResource(R.drawable.logo_no);
//                }else{
//                    isClick1=true;
//                    isClick2=false;
//                    isClick3=false;
//                    if(mKey!=null&&mKey.data.size()>0){
//                        fenlei=mKey.data.get(0).key;
//                    }
//
//                    chanColor(0);
//                }
//                mHandler.sendEmptyMessage(1);
//            }
//        });
//        bt2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isClick2){
//                    isClick2=false;
//                    fenlei="";
//                    bt2.setTextColor(Color.parseColor("#7F000000"));
//                    bt2.setBackgroundResource(R.drawable.logo_no);
//                }else{
//                    isClick1=false;
//                    isClick2=true;
//                    isClick3=false;
//                    if(mKey!=null&&mKey.data.size()>1){
//                        fenlei=mKey.data.get(1).key;
//                    }
//                    chanColor(1);
//                }
//                mHandler.sendEmptyMessage(1);
//            }
//        });
//        bt3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isClick3){
//                    isClick3=false;
//                    fenlei="";
//                    bt3.setTextColor(Color.parseColor("#7F000000"));
//                    bt3.setBackgroundResource(R.drawable.logo_no);
//                }else{
//                    isClick1=false;
//                    isClick2=false;
//                    isClick3=true;
//                    if(mKey!=null&&mKey.data.size()>2){
//                        fenlei=mKey.data.get(2).key;
//                    }
//                    chanColor(2);
//                }
//                mHandler.sendEmptyMessage(1);
//            }
//        });

        mAdapter.setLisener(new LeaderActivityAdapter.OnitemClick() {
            @Override
            public void onitemClice(LeaderActivitys.DataBean data) {
                Intent intent=new Intent(LeaderActivitysActivity.this,LeadersDetaislActivity.class);
                intent.putExtra("id",data.id);
                intent.putExtra("type","1");
                intent.putExtra("classy","news");
                startActivity(intent);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isSearch=false;
                search.setText("");
                searchKey="";
                tishi.setVisibility(View.GONE);
//                if(position==0){
//                    fenlei="";
//                }else {
                    fenlei=myData.get(position).value;
//                }
//                fenlei= cn.yumutech.weight.SaveData.getInstance().fenlei;
//        }
                mHandler.sendEmptyMessage(1);
                adapter.dataChange(myData,position);
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    if(isSearch){
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
                    }else {
                        if (!isMoreLoading) {
                            isMoreLoading = true;
                            isRefresh=true;
                            mPage=leaderActivitys.size();
                            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                                    new RequestCanShu.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
                            initDatas1(new Gson().toJson(canshus));
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
                if(app.isNetworkConnected(LeaderActivitysActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    ll_feilei.setVisibility(View.VISIBLE);
                    myprog.setVisibility(View.VISIBLE);
                    tishi.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    myInitData();
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
                        mPageSearch=0;
                        isRefresh=false;
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
                    mHandler.sendEmptyMessage(1);
                }
            }
        });
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent().setClass(LeaderActivitysActivity.this,SearchLeaderActivity.class);
                intent.putExtra("fenlei",fenlei);
                startActivity(intent);

            }
        });
    }
    Observer<LeaderActivitys> observer = new Observer<LeaderActivitys>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
            pullToRefresh.setRefreshing(false);
            isMoreLoading = false;
            net_connect.setVisibility(View.GONE);
        }
        @Override
        public void onError(Throwable e) {
            pullToRefresh.setRefreshing(false);
            e.printStackTrace();
            isMoreLoading = false;
        }
        @Override
        public void onNext(LeaderActivitys channels) {
            if(channels.status.code.equals("0")){
//                if(channels.data.size()>0){
                    loadHome(channels.data);
//                }
                if(mPage==0){
                    app.savaHomeJson("LeaderActivitys",new Gson().toJson(channels));
                }
            }
        }
    };
    /**
     * 加载列表数据
     */
    private void loadHome(List<LeaderActivitys.DataBean> data){
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

        net_connect.setVisibility(View.GONE);
        ll_feilei.setVisibility(View.VISIBLE);
        pullToRefresh.setRefreshing(false);
        isMoreLoading = false;
    }
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
                    if(isSearch){
                        isSearch=true;
                        mPageSearch=0;
                        isRefresh=false;
                        isMoreLoading=true;
                        initSearch(search.getText().toString().trim());
                    }else {
                        mPage=0;
                        isRefresh=false;
                        searchKey="";
                        isMoreLoading=true;
                        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,"1234567890"),
                                new RequestCanShu.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
                        initDatas1(new Gson().toJson(canshus));
                    }
                    break;
            }
        }
    };
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

    /**
     * 获取分类信息
     */
    int shangbiao = 0;
    int xiabiao = 0;
    public void initClassData(){
        if(App.getContext().getLogo("logo")!=null) {
            ModuleClassifyListBeen canshus=new ModuleClassifyListBeen(new ModuleClassifyListBeen.UserBeen(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                    new ModuleClassifyListBeen.DataBeen("LeaderActivity"));
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
                mKey=moduleClassifyList;
                myData.clear();
//                oneData=new ModuleClassifyList.data("推荐","推荐");
//                myData.add(oneData);
                myData.addAll(moduleClassifyList.data);
                if(moduleClassifyList.data.size()<4) {
                    gridView.setNumColumns(3);
                }
                fenlei=myData.get(0).value;
                adapter.dataChange(moduleClassifyList.data,0);
                myInitData();
//                addView(moduleClassifyList.data);

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
            if (!(j == 1)) {
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
                    chanColor(xiabiao);
                    mHandler.sendEmptyMessage(1);
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
    //分类那儿的点击
//    public void onEventMainThread(ModuleClassifyList moduleClassifyList){
//        isSearch=false;
//        search.setText("");
//        searchKey="";
//        tishi.setVisibility(View.GONE);
////        getIndex(tv);
////        if(xiabiao==0){
////            fenlei="";
////        }else {
////            fenlei=a.get(xiabiao-1).key;
//            fenlei= cn.yumutech.weight.SaveData.getInstance().fenlei;
////        }
////        chanColor(xiabiao);
//        mHandler.sendEmptyMessage(1);
//    }

}
