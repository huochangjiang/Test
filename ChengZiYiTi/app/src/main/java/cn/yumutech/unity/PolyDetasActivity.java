package cn.yumutech.unity;

import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.yumutech.bean.LeaderActivitsDetails;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.CenterTextView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PolyDetasActivity extends BaseActivity {

    LeaderActivitsDetails data;
    private WebView webView;
    private CenterTextView title1;
    private TextView laiyan;
    private TextView tv_time;
    private View myprog;
    private ScrollView scrollView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_poly_detas;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        webView = (WebView) findViewById(R.id.webview);
        myprog=findViewById(R.id.myprog);
        scrollView= (ScrollView) findViewById(R.id.scrollview);
        myprog.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
//        webView.getSettings().setJavaScriptEnabled(true);
//// 设置可以支持缩放
//        webView.getSettings().setSupportZoom(true);
//// 设置出现缩放工具
//        webView.getSettings().setBuiltInZoomControls(false);
////扩大比例的缩放
//        webView.getSettings().setUseWideViewPort(true);
////自适应屏幕
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setDefaultFontSize(18);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        controlTitle(findViewById(R.id.back));
        title1 = (CenterTextView) findViewById(R.id.tv1);
        laiyan = (TextView) findViewById(R.id.laiyuan);
        tv_time = (TextView) findViewById(R.id.tv_time);
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
    }

    @Override
    protected void initData() {
        String id=getIntent().getStringExtra("id");
        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,"1234567890"),new RequestCanShu.DataBean(id));
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
        subscription = Api.getMangoApi1().getPolicyDetais(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
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
                title1.setText(channels.data.title);
                laiyan.setText(channels.data.original);
                tv_time.setText(channels.data.publish_date);
                myprog.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        }
    };
}
