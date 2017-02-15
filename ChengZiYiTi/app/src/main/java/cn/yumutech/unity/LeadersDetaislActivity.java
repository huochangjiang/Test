package cn.yumutech.unity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.yumutech.bean.LeaderActivitsDetails;
import cn.yumutech.bean.PrijectDetaisl;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.WorkDetails;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.CenterTextView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LeadersDetaislActivity extends BaseActivity {

    LeaderActivitsDetails data;
    WorkDetails data1;
    PrijectDetaisl data2;
    private WebView webView;
    private String type;
    private CenterTextView title1;
    private TextView laiyan;
    private TextView tv_time;
    private TextView guihua;
    private TextView money;
    private RelativeLayout relativeLayout;
    private View myprog;
    private TextView tv_home;
//    private ScrollView scrollview;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_leaders_detaisl;
    }


    Handler mHandelr=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("info",data.data.title+"--");
            title1.setText(data.data.title);
        }
    };
    @Override
    protected void initViews(Bundle savedInstanceState) {
        webView = (WebView) findViewById(R.id.webview);
//        scrollview= (ScrollView) findViewById(R.id.scrollview);
        myprog=findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
        tv_home= (TextView) findViewById(R.id.tv_home);
//        scrollview.setVisibility(View.GONE);
//        WebSettings settings = webView.getSettings();
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        settings.setUseWideViewPort(true);//设定支持viewport
//
//        settings.setLoadWithOverviewMode(true);
//
//        settings.setBuiltInZoomControls(true);
//
//        settings.setSupportZoom(true);//设定支持缩放

//        settings.setDisplayZoomControls(false);

//        settings.setLoadWithOverviewMode(true);


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
        webView.getSettings().setDisplayZoomControls(false);
//        webView.getSettings().setDefaultFontSize(18);
        controlTitle(findViewById(R.id.back));
        title1 = (CenterTextView) findViewById(R.id.title).findViewById(R.id.tv1);
        laiyan = (TextView) findViewById(R.id.laiyuan);
        relativeLayout = (RelativeLayout) findViewById(R.id.rlll);
        guihua = (TextView) findViewById(R.id.guihua);
        money = (TextView) findViewById(R.id.money);
        tv_time = (TextView) findViewById(R.id.tv_time);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
//         disable scroll on touch
//        webView.setScrollContainer(false);
//        webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return (event.getAction() == MotionEvent.ACTION_MOVE);
//        scrollview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                webView.requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//        webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                scrollview.requestDisallowInterceptTouchEvent(false);
//                return false;
//            }
//        });
    }
    @Override
    protected void initData() {
        String id=getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
//        if(type.equals("2")){
//            tv_home.setText("重点项目");
//        }else {
            tv_home.setText("详情");
//        }
            RequestCanShu canshus = new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"), new RequestCanShu.DataBean(id));
            initDatas1(new Gson().toJson(canshus));

    }

    @Override
    protected void initListeners() {

    }
    Subscription subscription;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    private void initDatas1( String canshu){
        if(type.equals("1")) {
            subscription = Api.getMangoApi1().getLeaderActivitsDetails(canshu)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer1);
        }else if(type.equals("2")){
            subscription = Api.getMangoApi1().getWorkDetais(canshu)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer2);
        }else if(type.equals("3")){
            relativeLayout.setVisibility(View.VISIBLE);
//            subscription = Api.getMangoApi1().getProjectDetais(canshu)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(observer3);
        }

    }
    Observer<LeaderActivitsDetails> observer1 = new Observer<LeaderActivitsDetails>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(LeaderActivitsDetails channels) {
            if(channels.status.code.equals("0")){
                data=channels;
                webView.loadDataWithBaseURL(null, channels.data.content, "text/html", "utf-8", null);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
                laiyan.setText(channels.data.original);
                tv_time.setText(channels.data.publish_date);
                mHandelr.sendEmptyMessage(0);
                myprog.setVisibility(View.GONE);
//                scrollview.setVisibility(View.VISIBLE);
            }

        }
    };
    Observer<WorkDetails> observer2 = new Observer<WorkDetails>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(WorkDetails channels) {
            if(channels.status.code.equals("0")){
                data1=channels;
//                webView.loadUrl(channels.data.content);
                webView.loadDataWithBaseURL(null, channels.data.content, "text/html", "utf-8", null);
                title1.setText(channels.data.title);
                laiyan.setText(channels.data.original);
                tv_time.setText(channels.data.publish_date);
                myprog.setVisibility(View.GONE);
//                scrollview.setVisibility(View.VISIBLE);
            }
        }
    };

//    Observer<PrijectDetaisl> observer3 = new Observer<PrijectDetaisl>() {
//        @Override
//        public void onCompleted() {
//            unsubscribe(subscription);
//        }
//        @Override
//        public void onError(Throwable e) {
//            e.printStackTrace();
//
//        }
//        @Override
//        public void onNext(PrijectDetaisl channels) {
//            if(channels.status.code.equals("0")){
//                data2=channels;
//                webView.loadDataWithBaseURL(null, channels.data.content, "text/html", "utf-8", null);
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.setWebChromeClient(new WebChromeClient());
//                title1.setText(channels.data.title);
//                laiyan.setText(channels.data.original);
//                tv_time.setText(channels.data.publish_date);
//            if(channels.data.amount.equals("")){
//                money.setText("项目金额:"+"0元");
//            }else {
//                money.setText("项目金额" + channels.data.amount + "元");
//            }
//                guihua.setText("类型:"+channels.data.type);
//                myprog.setVisibility(View.GONE);
//                scrollview.setVisibility(View.VISIBLE);
//            }
//
//        }
//    };
}
