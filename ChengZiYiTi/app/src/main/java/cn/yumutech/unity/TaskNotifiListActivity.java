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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.TaskNotifiListAdapter;
import cn.yumutech.bean.ModuleClassifyList;
import cn.yumutech.bean.ModuleClassifyListBeen;
import cn.yumutech.bean.TaskNotifiList;
import cn.yumutech.bean.TaskNotifiListBeen;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.MyEditText;
import cn.yumutech.weight.StringUtils1;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/1/18.
 */
public class TaskNotifiListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    Subscription subscription;
    Subscription subscription1;
    private int mPage=0;
    private int mPageSize = 10;
    private int mPageSearch=0;
    private ImageView back;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout pullToRefresh;
    private TaskNotifiListAdapter mAdapter;
    private List<TaskNotifiList.DataBean> leaderActivitys=new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private App app;
    private View net_connect;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private boolean isRefresh=false;
    private boolean isHave;
    private View myprog;
    private String fenlei="督查通知";
    private LinearLayout ll_feilei;
    private HorizontalScrollView diqu;
    private List<TextView> bts = new ArrayList<>();
    private ModuleClassifyList mKey;
    private LinearLayout linearLayout1;
    List<LinearLayout> linears = new ArrayList<LinearLayout>();
    private List<HorizontalScrollView> hors = new ArrayList<>();
    private View tishi;
    private MyEditText search;
    private boolean isSearch;
    private String searchKey="";
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_task_notifi_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        back = (ImageView) findViewById(R.id.back);
        app= (App) TaskNotifiListActivity.this.getApplicationContext();
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
        initClassData();
        tishi=findViewById(R.id.tishi);
        myprog=  findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
        tishi.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        mAdapter = new TaskNotifiListAdapter(TaskNotifiListActivity.this,leaderActivitys);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        pullToRefresh.setOnRefreshListener(this);
        controlTitle(findViewById(R.id.back));
        net_connect = findViewById(R.id.netconnect);

        initLocal();
        search= (MyEditText) findViewById(R.id.search);
    }
    //加载缓存
    private void initLocal() {
        String readHomeJson = app.readHomeJson("TaskNotifiListActivity");// 首页内容
        if (!StringUtils1.isEmpty(readHomeJson)) {
            TaskNotifiList data = new Gson().fromJson(readHomeJson, TaskNotifiList.class);
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
        if (app.isNetworkConnected(TaskNotifiListActivity.this)) {
            initData();
        }
    }
    /**
     * 加载列表数据
     */
    private void loadHome(List<TaskNotifiList.DataBean> data){
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
    protected void initData() {
        TaskNotifiListBeen taskNotifiItemBeen = new TaskNotifiListBeen(new TaskNotifiListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                new TaskNotifiListBeen.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
        getData1(new Gson().toJson(taskNotifiItemBeen));
    }

    //获取列表
    private void getData1(String data) {
        subscription = Api.getMangoApi1().getTaskNotifiList(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<TaskNotifiList> observer=new Observer<TaskNotifiList>() {
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
        public void onNext(TaskNotifiList taskNotifiList) {
            String data=new Gson().toJson(taskNotifiList);
            Log.e("tongzhi",data);
            if(taskNotifiList.status.code.equals("0")){
//                if(channels.data.size()>0){
                loadHome(taskNotifiList.data);
//                }
                if(mPage==0){
                    app.savaHomeJson("TaskNotifiListActivity",new Gson().toJson(taskNotifiList));
                }
            }
        }
    };

    @Override
    protected void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAdapter.setLisener(new TaskNotifiListAdapter.OnitemClick() {
            @Override
            public void onitemClice(TaskNotifiList.DataBean data) {
                Intent intent=new Intent(TaskNotifiListActivity.this,TaskNotifiListDetailActivity.class);
                intent.putExtra("id",data.id);
                startActivity(intent);
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
                            TaskNotifiListBeen canshus = new TaskNotifiListBeen(new TaskNotifiListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                                    new TaskNotifiListBeen.DataBean(fenlei,search.getText().toString().trim(),mPageSearch+"",mPageSize+""));
//                            TaskNotificationSearchBeen canshus=new TaskNotificationSearchBeen(new TaskNotificationSearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
//                                    new TaskNotificationSearchBeen.DataBean(search.getText().toString().trim(),mPageSearch+"",mPageSize+""));
                            initSearch1(new Gson().toJson(canshus));
                        }
                    }else {
                        if (!isMoreLoading) {
                            isMoreLoading = true;
                            isRefresh=true;
                            mPage=leaderActivitys.size();
                            TaskNotifiListBeen taskNotifiItemBeen = new TaskNotifiListBeen(new TaskNotifiListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                                    new TaskNotifiListBeen.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
                            getData1(new Gson().toJson(taskNotifiItemBeen));
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
                if(app.isNetworkConnected(TaskNotifiListActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    ll_feilei.setVisibility(View.VISIBLE);
                    myprog.setVisibility(View.VISIBLE);
                    tishi.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
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
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessage(1);
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
            TaskNotifiListBeen canshus = new TaskNotifiListBeen(new TaskNotifiListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new TaskNotifiListBeen.DataBean(fenlei,searchKey,mPageSearch+"",mPageSize+""));
//            TaskNotificationSearchBeen canshus=new TaskNotificationSearchBeen(new TaskNotificationSearchBeen.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
//                    new TaskNotificationSearchBeen.DataBean(key,mPageSearch+"",mPageSize+""));
            initSearch1(new Gson().toJson(canshus));
        }else {
//            Toast.makeText(this,"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }

    private void initSearch1(String canshu) {
        subscription = Api.getMangoApi1().getTaskNotifiList(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);
    }
    Observer<TaskNotifiList> observer2=new Observer<TaskNotifiList>() {
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
        public void onNext(TaskNotifiList channels) {
            if(channels.status.code.equals("0")){
//                if(channels.data.size()>0){
                loadHome(channels.data);
//                }

            }
        }
    };
    /**
     * 获取分类信息
     */
    int shangbiao = 0;
    int xiabiao = 0;
    public void initClassData(){
        if(App.getContext().getLogo("logo")!=null) {
            ModuleClassifyListBeen canshus=new ModuleClassifyListBeen(new ModuleClassifyListBeen.UserBeen(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                    new ModuleClassifyListBeen.DataBeen("TaskNotification"));
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
                fenlei=moduleClassifyList.data.get(0).value;
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
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(isSearch){
                        isSearch=true;
                        mPageSearch=0;
                        isMoreLoading=true;
                        initSearch(search.getText().toString().trim());
                    }else {
                        mPage=0;
                        isRefresh=false;
                        isMoreLoading=true;
                        TaskNotifiListBeen taskNotifiItemBeen = new TaskNotifiListBeen(new TaskNotifiListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                                new TaskNotifiListBeen.DataBean(fenlei,searchKey,mPage+"",mPageSize+""));
                        getData1(new Gson().toJson(taskNotifiItemBeen));
                    }
                    break;
            }
        }
    };
}
