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
import android.util.Log;
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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.ProjectMangerAdpater;
import cn.yumutech.Adapter.SBAdapter;
import cn.yumutech.bean.ModuleClassifyList;
import cn.yumutech.bean.ModuleClassifyListBeen;
import cn.yumutech.bean.ProjectManger;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.netUtil.Api;
import cn.yumutech.netUtil.ToosUtil;
import cn.yumutech.weight.MyEditText;
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
    private int mPageSearch=0;

    private boolean isHave;
    private View myprog;
    private String fenlei="";
    private LinearLayout ll_feilei;
//    private TextView bt1,bt2,bt3;
    private List<TextView> bts = new ArrayList<>();
    //判断上面的分类按钮是否被点击
    private boolean isClick1,isClick2,isClick3;
    private View tishi;
    Subscription subscription1;
    private ModuleClassifyList mKey;
    private LinearLayout linearLayout1;
    List<LinearLayout> linears = new ArrayList<LinearLayout>();
    private List<HorizontalScrollView> hors = new ArrayList<>();
    private HorizontalScrollView diqu;
    private MyEditText search;
    private boolean isSearch;
    private String searchKey;
    private ImageView next;
    private List<ModuleClassifyList.data> mData=new ArrayList<>();
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
        return R.layout.activity_project_manger;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app=App.getContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyleview);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        mAdapter = new ProjectMangerAdpater(this,leaderActivitys);
        ll_feilei= (LinearLayout) findViewById(R.id.ll_feilei);
//        bt1= (TextView) findViewById(R.id.bt1);
//        bt2= (TextView) findViewById(R.id.bt2);
//        bt3= (TextView) findViewById(R.id.bt3);
//        bts.add(bt1);
//        bts.add(bt2);
//        bts.add(bt3);
//        bt1.setOnClickListener(this);
//        bt2.setOnClickListener(this);
//        bt3.setOnClickListener(this);
        diqu=(HorizontalScrollView) findViewById(R.id.horscroll_one);
        linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linears.add(linearLayout1);
        hors.add(diqu);

        tishi=findViewById(R.id.tishi);
        tishi.setVisibility(View.GONE);
        gridView= (GridView) findViewById(R.id.gridView);
        adapter=new SBAdapter(ProjectMangerActivity.this,mData);
        gridView.setAdapter(adapter);
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
        initClassData();

        searchImage= (ImageView) findViewById(R.id.searchImage);
        search= (MyEditText) findViewById(R.id.search);
        next= (ImageView) findViewById(R.id.next);
    }
    //搜索到的内容的结果
    private void initSearch(String key) {
        if(App.getContext().getLogo("logo")!=null) {
            searchKey=key;
            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                    new RequestCanShu.DataBean(fenlei,searchKey,mPageSearch+"",mPageSize+""));
//            ProjectWorkSearchBeen canshus=new ProjectWorkSearchBeen(new ProjectWorkSearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
//                    new ProjectWorkSearchBeen.DataBean(key,mPageSearch+"",mPageSize+""));
            initSearch1(new Gson().toJson(canshus));
        }else {
//            Toast.makeText(this,"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }
    private void initSearch1(String canshu) {
        subscription = Api.getMangoApi1().getProject(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);
    }
    Observer<ProjectManger> observer2=new Observer<ProjectManger>() {
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
        public void onNext(ProjectManger channels) {
            if(channels.status.code.equals("0")){
//                if(channels.data.size()>0){
                loadHome(channels.data);
//                }
            }
            isMoreLoading = false;
            pullToRefresh.setRefreshing(false);
        }
    };
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
                tishi.setVisibility(View.GONE);
                ll_feilei.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        }
        if (app.isNetworkConnected(ProjectMangerActivity.this)) {
//            initData();
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
        if(leaderActivitys.isEmpty()){
            tishi.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            tishi.setVisibility(View.GONE);
        }
        myprog.setVisibility(View.GONE);
        net_connect.setVisibility(View.GONE);

        ll_feilei.setVisibility(View.VISIBLE);
        mAdapter.dataChange(leaderActivitys,isHave);
    }
    private void myInitData(){
        if(App.getContext().getLogo("logo")!=null){
            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                    new RequestCanShu.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
            initDatas1(new Gson().toJson(canshus));
        }else {
            App.getContext().noLogin(ProjectMangerActivity.this);
        }
    }
    @Override
    protected void initData() {
    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getProject(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    @Override
    protected void initListeners() {
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent().setClass(ProjectMangerActivity.this,SearchProjectMangerActivity.class);
                intent.putExtra("fenlei",fenlei);
                startActivity(intent);

            }
        });
        mAdapter.setLisener(new ProjectMangerAdpater.OnitemClick() {
            @Override
            public void onitemClice(ProjectManger.DataBean data) {
                Intent intent=new Intent(ProjectMangerActivity.this,ProjectDetaisActivity.class);
                intent.putExtra("id",data.id);
                intent.putExtra("fenlei",fenlei);
                intent.putExtra("type","3");
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
                fenlei=mData.get(position).value;
//                }
//                fenlei= cn.yumutech.weight.SaveData.getInstance().fenlei;
//        }
                mHandler.sendEmptyMessage(1);
                adapter.dataChange(mData,position);
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
//                            ProjectWorkSearchBeen canshus=new ProjectWorkSearchBeen(new ProjectWorkSearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
//                                    new ProjectWorkSearchBeen.DataBean(search.getText().toString().trim(),mPageSearch+"",mPageSize+""));
                            initSearch1(new Gson().toJson(canshus));
                        }
                    }else {
                        if (!isMoreLoading) {
                            isMoreLoading = true;
                            isRefresh=true;
                            mPage=leaderActivitys.size();
                            if(App.getContext().getLogo("logo")!=null){
                                RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                                        new RequestCanShu.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
                                Log.e("fenlei",fenlei);
                                initDatas1(new Gson().toJson(canshus));
                            }else {
                                App.getContext().noLogin(ProjectMangerActivity.this);
                            }

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
                    tishi.setVisibility(View.GONE);
                    ll_feilei.setVisibility(View.VISIBLE);
                    myprog.setVisibility(View.VISIBLE);
                    myInitData();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(2);
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
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.bt1:
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
//                    chanColor(0);
//                }
//                mHandler.sendEmptyMessage(1);
//                break;
//            case R.id.bt2:
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
//                break;
//            case R.id.bt3:
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
//                break;

        }
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(isSearch){
                        isSearch=true;
                        isRefresh=false;
                        mPageSearch=0;
                        isMoreLoading=true;
                        initSearch(search.getText().toString().trim());
                    }else {
                        isRefresh=false;
                        mPage=0;
                        searchKey="";
                        isMoreLoading=true;
                        if(App.getContext().getLogo("logo")!=null){
                            RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,"1234567890"),
                                    new RequestCanShu.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
                            initDatas1(new Gson().toJson(canshus));
                        }else {
                            App.getContext().noLogin(ProjectMangerActivity.this);
                        }
                    }
                    break;
                case 2:
                    diqu.scrollBy(ToosUtil.getInstance().getScreenWidth(ProjectMangerActivity.this)/4,0);
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
    int xiabiao = 0;
    public void initClassData(){
        if(App.getContext().getLogo("logo")!=null) {
            ModuleClassifyListBeen canshus=new ModuleClassifyListBeen(new ModuleClassifyListBeen.UserBeen(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                    new ModuleClassifyListBeen.DataBeen("ProjectWork"));
            initClassDatas1(new Gson().toJson(canshus));
        }else {
//            Toast.makeText(this,"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }
    public void initClassDatas1(String canshu){
        subscription = Api.getMangoApi1().getModuleClassifyList(canshu)
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
                mData=moduleClassifyList.data;
                fenlei=moduleClassifyList.data.get(0).value;
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
}
