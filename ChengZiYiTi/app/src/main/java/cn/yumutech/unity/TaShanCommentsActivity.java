package cn.yumutech.unity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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

import cn.yumutech.Adapter.TaShanCommentAdapter;
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
 * Created by Allen on 2016/11/13.
 */
public class TaShanCommentsActivity extends BaseActivity  implements PullToRefreshBase.OnRefreshListener2<ScrollView>{
    private ImageView back;
    private Button button;
    private MyListview comments_list;
    Subscription subscription;
    Subscription subscription1;
    private TaShanCommentAdapter adapter;
    private List<ExchangeCommenList.data> mData;
    private RelativeLayout shurukuang;
    private EditText edit;
    private TextView send;
    private App app;
    private String mId,userId;
    private RelativeLayout rl;
    private int page=0;
    private PullToRefreshScrollView pullToRefresh;
    private String type;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_tashan_comments;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app = (App) TaShanCommentsActivity.this.getApplicationContext();
        getExtra();
        back= (ImageView) findViewById(R.id.back);
        button= (Button) findViewById(R.id.button);
        shurukuang= (RelativeLayout) findViewById(R.id.shurukuang);
        comments_list= (MyListview) findViewById(R.id.comments_list);
        edit= (EditText) findViewById(R.id.edit);
        send= (TextView) findViewById(R.id.send);
         rl = (RelativeLayout) findViewById(R.id.rl);
        pullToRefresh = (PullToRefreshScrollView) findViewById(R.id.pull_to_refresh);
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefresh.setOnRefreshListener(this);
        adapter=new TaShanCommentAdapter(TaShanCommentsActivity.this,mData);
        comments_list.setAdapter(adapter);
        button.setFocusable(true);
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
        if(type!=null&&type.equals("yes")){
//            button.requestFocus();
//            edit.setFocusable(true);
//            edit.setText("");
//            button.setVisibility(View.GONE);
//            shurukuang.setVisibility(View.VISIBLE);
//            //弹出软键盘
//            InputMethodManager inputManager =
//                    (InputMethodManager)edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
        }
    }

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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.requestFocus();
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
        comments_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(TaShanCommentsActivity.this, ReplyToCommentActivity.class);
                if(mData!=null){
                    intent.putExtra("commentId",mData.get(position).comment_id);
                }
                startActivity(intent);
            }
        });
    }
    /**
     * 获取他山之石评论列表
     */
    private ExchangeCommenListBeen exchangeCommenList;
    private void getData(){
         exchangeCommenList=new ExchangeCommenListBeen(new ExchangeCommenListBeen.UserBean("1","1234567890"),
                new ExchangeCommenListBeen.DataBean(mId,"0","10"));
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
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    /**
     * 添加他山之石评论
     */
    private void addPinglun(){
        if(mId!=null&&userId!=null){
            AddPingLunBeen addPingLunBeen=new AddPingLunBeen(new AddPingLunBeen.userBeen("1","1234567890"),
                    new AddPingLunBeen.dataBeen(edit.getText().toString(),mId,userId,""));
            addPinglun1(new Gson().toJson(addPingLunBeen));
        }else if(userId==null){
            Toast.makeText(TaShanCommentsActivity.this,"您还未登陆",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TaShanCommentsActivity.this,errorPinglun.status.message,Toast.LENGTH_SHORT).show();
               mHandler.sendEmptyMessage(1);
            } else if(errorPinglun.status.code.equals("-9")){
                Toast.makeText(TaShanCommentsActivity.this,errorPinglun.status.message,Toast.LENGTH_SHORT).show();
            }
            pullToRefresh.onRefreshComplete();
        }
    };
    /**
     * 获取传入的数据
     */
    private void getExtra(){
        if(getIntent()!=null){
            mId=getIntent().getStringExtra("id");
            type=getIntent().getStringExtra("type");
        }
        if(app.getLogo("logo")!=null){
            userId=app.getLogo("logo").data.id;
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

    @Override
    protected void onResume() {
        super.onResume();
        rl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rl.getRootView().getHeight() - rl.getHeight();
                if (heightDiff > dpToPx(TaShanCommentsActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
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
    private boolean isShangla=false;
    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        isShangla=false;

        exchangeCommenList=new ExchangeCommenListBeen(new ExchangeCommenListBeen.UserBean("1","1234567890"),
                new ExchangeCommenListBeen.DataBean(mId,"0","10"));
        getData1(new Gson().toJson(exchangeCommenList));
    }
    //上拉加载更多
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        page=mData.size();
        isShangla=true;
        exchangeCommenList=new ExchangeCommenListBeen(new ExchangeCommenListBeen.UserBean("1","1234567890"),
                new ExchangeCommenListBeen.DataBean(mId,page+"","10"));
        getData1(new Gson().toJson(exchangeCommenList));
    }
}
