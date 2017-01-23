package cn.yumutech.unity;

import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.yumutech.bean.TaskNotificationItem;
import cn.yumutech.bean.TaskNotificationItemBeen;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.StringUtils1;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/1/23.
 */
public class TaskNotifiListDetailActivity extends BaseActivity{
    Subscription subscription;
    private String myId;
    private View net_connect;
    private ImageView back;
    private TextView title,laiyuan,tv_time;
    private App app;
    private View myprog;
    private ScrollView scrollview;
    private WebView webView;
    private String readHomeJson;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_task_notifi_list_detail;
    }
    private void getExtra(){
        if(getIntent()!=null){
            myId=getIntent().getStringExtra("id");
        }
    }
    @Override
    protected void initViews(Bundle savedInstanceState) {
        app= (App) TaskNotifiListDetailActivity.this.getApplicationContext();
        getExtra();
        back= (ImageView) findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title).findViewById(R.id.tv1);
        laiyuan= (TextView) findViewById(R.id.title).findViewById(R.id.laiyuan);
        tv_time= (TextView) findViewById(R.id.title).findViewById(R.id.tv_time);

        scrollview= (ScrollView) findViewById(R.id.scrollview);
        scrollview.setVisibility(View.GONE);
        net_connect = findViewById(R.id.netconnect);
        myprog=findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setDefaultFontSize(18);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        // disable scroll on touch
        webView.setScrollContainer(false);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        initLocal();

    }

    private void initLocal() {
        if(myId!=null){
            readHomeJson = app.readHomeJson(myId);// 首页内容
        }
        if (!StringUtils1.isEmpty(readHomeJson)) {
            TaskNotificationItem data = new Gson().fromJson(readHomeJson, TaskNotificationItem.class);
            loadHome(data);
        }else{
            if(!app.isNetworkConnected(this)){
                net_connect.setVisibility(View.VISIBLE);
                scrollview.setVisibility(View.GONE);
                myprog.setVisibility(View.GONE);
            }
        }
        if (app.isNetworkConnected(TaskNotifiListDetailActivity.this)) {
            initData();
        }
    }

    private void loadHome(TaskNotificationItem taskNotificationItem) {
        if(taskNotificationItem!=null&&taskNotificationItem.data!=null){
            webView.loadDataWithBaseURL(null, taskNotificationItem.data.content, "text/html", "utf-8", null);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            title.setText(taskNotificationItem.data.title);
            laiyuan.setText(taskNotificationItem.data.original);
            tv_time.setText(taskNotificationItem.data.publish_date);

            scrollview.setVisibility(View.VISIBLE);
            myprog.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initData() {
        TaskNotificationItemBeen exchangeItemBeen = new TaskNotificationItemBeen(new TaskNotificationItemBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                new TaskNotificationItemBeen.DataBean(myId));
        getData1(new Gson().toJson(exchangeItemBeen));
    }

    private void getData1(String detail) {
        subscription = Api.getMangoApi1().getTaskNotificationItem(detail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<TaskNotificationItem> observer=new Observer<TaskNotificationItem>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(TaskNotificationItem taskNotificationItem) {
            if(taskNotificationItem!=null&&taskNotificationItem.data!=null){
                loadHome(taskNotificationItem);

            }
        }
    };
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
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
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.isNetworkConnected(TaskNotifiListDetailActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    myprog.setVisibility(View.VISIBLE);
                    initData();
                }
            }
        });
    }
}
