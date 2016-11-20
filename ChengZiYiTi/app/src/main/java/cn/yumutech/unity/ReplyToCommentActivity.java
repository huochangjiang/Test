package cn.yumutech.unity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.List;

import cn.yumutech.Adapter.ReplyToCommentAdapter;
import cn.yumutech.bean.AddErrorPinglun;
import cn.yumutech.bean.AddPingLunBeen;
import cn.yumutech.bean.ExchangeCommenList;
import cn.yumutech.bean.ExchangeCommenListBeen;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.MyListview;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 2016/11/14.
 */
public class ReplyToCommentActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollView>{
    private ImageView back;
    private ReplyToCommentAdapter adapter;
    private MyListview listView;
    private List<ExchangeCommenList.data> mData;
    private int page=0;
    Subscription subscription;
    Subscription subscription1;
    private PullToRefreshScrollView pullToRefresh;
    private String commentId;
    private App app;
    private Button button;
    private EditText edit;
    private TextView send;
    private RelativeLayout shurukuang;
    private String userId;
    private RelativeLayout rl;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_reply_to_comment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app=App.getContext();
        initExtra();
        back= (ImageView) findViewById(R.id.back);
        listView= (MyListview) findViewById(R.id.comments_list);
        adapter=new ReplyToCommentAdapter(ReplyToCommentActivity.this,mData);

        pullToRefresh = (PullToRefreshScrollView) findViewById(R.id.pull_to_refresh);
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefresh.setOnRefreshListener(this);
        rl = (RelativeLayout) findViewById(R.id.rl);
        button= (Button) findViewById(R.id.button);
        edit= (EditText) findViewById(R.id.edit);
        send= (TextView) findViewById(R.id.send);
        shurukuang= (RelativeLayout) findViewById(R.id.shurukuang);
        listView.setAdapter(adapter);
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
    }

    @Override
    protected void initData() {
        if(commentId!=null&&commentId.length()!=0){
            getData();
        }

    }

    @Override
    protected void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.requestFocus();
                listView.setFocusable(false);
                edit.setFocusable(true);
                edit.setText("");
                button.setVisibility(View.GONE);
                shurukuang.setVisibility(View.VISIBLE);
                //弹出软键盘
                InputMethodManager inputManager =
                        (InputMethodManager)button.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit.getText().toString()!=null&&edit.getText().toString().length()!=0){
                    addPinglun();
                }
            }
        });
    }
    //获取回复列表
    private ExchangeCommenListBeen exchangeCommenList;
    private void getData(){
        exchangeCommenList=new ExchangeCommenListBeen(new ExchangeCommenListBeen.UserBean("1","1234567890"),
                new ExchangeCommenListBeen.DataBean(commentId,"0","10"));
        getData1(new Gson().toJson(exchangeCommenList));
    }
    private void getData1(String list){
        subscription = Api.getMangoApi1().getExchangeCommenList(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<ExchangeCommenList> observer=new Observer<ExchangeCommenList>() {
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
        public void onNext(ExchangeCommenList exchangeCommenList) {
            if(exchangeCommenList!=null&&exchangeCommenList.status.code.equals("0")){
                if(isShangla){
                    mData.addAll(exchangeCommenList.data);
                }else {
                    mData = exchangeCommenList.data;
                }
                adapter.dataChange(mData);
            }
            pullToRefresh.onRefreshComplete();
        }
    };
    /**
     * 添加他山之石评论
     */
    private void addPinglun(){
        if(commentId!=null&&userId!=null){
            AddPingLunBeen addPingLunBeen=new AddPingLunBeen(new AddPingLunBeen.userBeen("1","1234567890"),
                    new AddPingLunBeen.dataBeen(edit.getText().toString(),commentId,userId,""));
            addPinglun1(new Gson().toJson(addPingLunBeen));
        }else if(userId==null){
            Toast.makeText(ReplyToCommentActivity.this,"您还未登陆",Toast.LENGTH_SHORT).show();
        }

    }
    private void addPinglun1(String data){
        subscription1 = Api.getMangoApi1().getAddPingLun(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }
    Observer<AddErrorPinglun> observer1=new Observer<AddErrorPinglun>() {
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
        public void onNext(AddErrorPinglun errorPinglun) {
            String json=new Gson().toJson(errorPinglun);
            Log.e("json",json);
            if(errorPinglun.status.code.equals("0")){
                edit.setText("");
                Toast.makeText(ReplyToCommentActivity.this,errorPinglun.status.message,Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessage(1);
            } else if(errorPinglun.status.code.equals("-9")){
                Toast.makeText(ReplyToCommentActivity.this,errorPinglun.status.message,Toast.LENGTH_SHORT).show();
            }
            pullToRefresh.onRefreshComplete();
        }
    };
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    getData();
                    button.setVisibility(View.VISIBLE);
                    shurukuang.setVisibility(View.GONE);
                    //1.得到InputMethodManager对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//2.调用hideSoftInputFromWindow方法隐藏软键盘
                    imm.hideSoftInputFromWindow(send.getWindowToken(), 0); //强制隐藏键盘
                    break;
                case 2:
                    break;
            }
        }
    };
    private boolean isShangla=false;
    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        isShangla=false;

        exchangeCommenList=new ExchangeCommenListBeen(new ExchangeCommenListBeen.UserBean("1","1234567890"),
                new ExchangeCommenListBeen.DataBean(commentId,"0","10"));
        getData1(new Gson().toJson(exchangeCommenList));
    }
    //上拉加载更多
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        page=mData.size();
        isShangla=true;
        exchangeCommenList=new ExchangeCommenListBeen(new ExchangeCommenListBeen.UserBean("1","1234567890"),
                new ExchangeCommenListBeen.DataBean(commentId,page+"","10"));
        getData1(new Gson().toJson(exchangeCommenList));
    }
    //获取传入的数据
    private void initExtra(){
        if (getIntent()!=null){
            commentId=getIntent().getStringExtra("commentId");
        }
        if(app.getLogo("logo")!=null){
            userId=app.getLogo("logo").data.id;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        rl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rl.getRootView().getHeight() - rl.getHeight();
                if (heightDiff > dpToPx(ReplyToCommentActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    Log.e("TAG","aaaa");//显示
                    button.setVisibility(View.GONE);
                    shurukuang.setVisibility(View.VISIBLE);
                }else {
                    Log.e("TAG","bbbb");//消失
                    button.setVisibility(View.VISIBLE);
                    shurukuang.setVisibility(View.GONE);

                }
            }
        });
    }

    //    COMPLEX_UNIT_DIP
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }
}
