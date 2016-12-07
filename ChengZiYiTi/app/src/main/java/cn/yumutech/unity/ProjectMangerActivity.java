package cn.yumutech.unity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.ProjectMangerAdpater;
import cn.yumutech.bean.ModuleClassifyList;
import cn.yumutech.bean.ModuleClassifyListBeen;
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
                tishi.setVisibility(View.GONE);
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
                    tishi.setVisibility(View.GONE);
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
    //判断那个button变背景
    private void chanColor(int postion) {
        for (int i = 0; i < bts.size(); i++) {
            TextView bt = bts.get(i);
            if (i == postion) {
//                bt.setBackgroundResource(R.drawable.logo);
                bt.setTextColor(Color.parseColor("#388AEF"));
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
            ModuleClassifyListBeen canshus=new ModuleClassifyListBeen(new ModuleClassifyListBeen.UserBeen(App.getContext().getLogo("logo").data.nickname,App.getContext().getLogo("logo").data.id),
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
                tv.setText("全部");
            }else {
                tv.setText(a.get(j-1).value);
            }

            tv.setTextSize(18);
            bts.add(tv);
            linears.get(0).addView(tv);
            if (j == 0 ) {
//                tv.setBackgroundResource(R.drawable.logo);
                tv.setTextColor(Color.parseColor("#388AEF"));
            } else {
//                tv.setBackgroundResource(R.drawable.logo_no);
                tv.setTextColor(Color.parseColor("#7F000000"));
            }
            tv.setLayoutParams(layoutParams);
            if (!(j == 0)) {
                layoutParams.leftMargin = 60;
                tv.setGravity(Gravity.CENTER);
            }
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    setAnimation();
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
