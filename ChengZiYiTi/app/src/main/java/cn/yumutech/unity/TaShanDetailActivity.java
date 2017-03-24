package cn.yumutech.unity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.yumutech.bean.ExchangeItemBeen;
import cn.yumutech.bean.HuDongItem;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.CenterTextView;
import cn.yumutech.weight.StringUtils1;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 */
public class TaShanDetailActivity extends BaseActivity{
    private TextView fenlei,time,laiyuan,pinglun;
    private CenterTextView title;
    private RelativeLayout comments;
    private WebView webView;
    private String myId;
//    private ScrollView scrollView;
    Subscription subscription;
    private ImageView back;
    private HuDongItem detail;
    private Button button;
    private App app;
    private View net_connect;
    private String readHomeJson;
    private View myprog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_tashan_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app= (App) TaShanDetailActivity.this.getApplicationContext();
        getExtra();
        title= (CenterTextView) findViewById(R.id.title).findViewById(R.id.xinwentitle);
        fenlei= (TextView) findViewById(R.id.title).findViewById(R.id.myclass);
        time= (TextView) findViewById(R.id.title).findViewById(R.id.time);
        laiyuan= (TextView) findViewById(R.id.title).findViewById(R.id.tv_source);
        pinglun= (TextView) findViewById(R.id.title).findViewById(R.id.browse_num);
        comments= (RelativeLayout) findViewById(R.id.title).findViewById(R.id.comments);
        button= (Button) findViewById(R.id.button);
        webView = (WebView) findViewById(R.id.webview);
        back= (ImageView) findViewById(R.id.back);
//        scrollView= (ScrollView) findViewById(R.id.scrollView);
        myprog=findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
//        scrollView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        net_connect = findViewById(R.id.netconnect);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        //隐藏缩放按钮
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setSupportZoom(true);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        //影藏缩放按钮
////        webView.getSettings().setDisplayZoomControls(true);
////        webView.getSettings().setDefaultFontSize(18);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
//        } else {
//            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
//        }
        // disable scroll on touch
//        webView.setScrollContainer(false);
//        webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return (event.getAction() == MotionEvent.ACTION_MOVE);
//            }
//        });
        initLocal();
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
        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(TaShanDetailActivity.this,TaShanCommentsActivity.class);
                if(detail!=null&&detail.data!=null&&detail.data.id!=null){
                    intent.putExtra("id",detail.data.id);
                    intent.putExtra("type","no");
                }
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(TaShanDetailActivity.this,TaShanCommentsActivity.class);
                if(detail!=null&&detail.data!=null&&detail.data.id!=null){
                    intent.putExtra("id",detail.data.id);
                    intent.putExtra("type","yes");
                }
                startActivity(intent);
            }
        });
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.isNetworkConnected(TaShanDetailActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    initData();
                }
            }
        });
    }
    private void getExtra(){
        if(getIntent()!=null){
            myId=getIntent().getStringExtra("id");
        }
    }
    /**
     * 获取他山之石详情数据
     */
    private void getData() {
        ExchangeItemBeen exchangeItemBeen = new ExchangeItemBeen(new ExchangeItemBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                new ExchangeItemBeen.DataBean(myId));
        getData1(new Gson().toJson(exchangeItemBeen));
    }
    private void getData1(String detail){
        subscription = Api.getMangoApi1().getHuDongItem(detail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<HuDongItem> observer = new Observer<HuDongItem>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(HuDongItem huDongItem) {
            if(huDongItem!=null&&huDongItem.status.code.equals("0")){
                detail=huDongItem;
                app.savaHomeJson(myId,new Gson().toJson(huDongItem));
                loadHome(huDongItem);
            }
        }
    };

    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    private void initLocal() {
        if(myId!=null){
            readHomeJson = app.readHomeJson(myId);// 首页内容
        }
        if (!StringUtils1.isEmpty(readHomeJson)) {
            HuDongItem data = new Gson().fromJson(readHomeJson, HuDongItem.class);
            loadHome(data);
        }else{
            if(!app.isNetworkConnected(this)){
                net_connect.setVisibility(View.VISIBLE);
//                scrollView.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
            }
        }
        if (app.isNetworkConnected(TaShanDetailActivity.this)) {
            initData();
        }
    }
    //加载详情数据
    private void loadHome(HuDongItem huDongItem){
        webView.loadDataWithBaseURL(null, huDongItem.data.content, "text/html", "utf-8", null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        title.setText(huDongItem.data.title);
        fenlei.setText(huDongItem.data.classify);
        laiyuan.setText(huDongItem.data.original);
        time.setText(huDongItem.data.publish_date);
        pinglun.setText(huDongItem.data.comment_count+"条回复");
        net_connect.setVisibility(View.GONE);
//        scrollView.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        myprog.setVisibility(View.GONE);
//        scrollView.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
    }
}

