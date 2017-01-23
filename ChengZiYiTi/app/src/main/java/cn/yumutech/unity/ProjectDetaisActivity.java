package cn.yumutech.unity;

import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.bean.LeaderActivitsDetails;
import cn.yumutech.bean.ProjectialsXiangqing;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.WorkDetails;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.CenterTextView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProjectDetaisActivity extends BaseActivity implements View.OnClickListener{

    LeaderActivitsDetails data;
    WorkDetails data1;
    ProjectialsXiangqing data2;
    private WebView webView;
    private String type;
    private CenterTextView title1;
    private TextView laiyan;
    private TextView tv_time;
    private TextView guihua;
    private TextView money;
    private View myprog;
    private ScrollView scrollview;
    private TextView tv_jiben;
    private TextView tv_work;
    private TextView tv_tuijin;
    private TextView tv_cunzai;
    private ProgressBar progressBar;
    private TextView tv_progress;
    private TextView tv_leixing;
    private TextView tv_time1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_detais;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        webView = (WebView) findViewById(R.id.webview);
        scrollview= (ScrollView) findViewById(R.id.scrollview);
        myprog=findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
        scrollview.setVisibility(View.GONE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        tv_jiben = (TextView) findViewById(R.id.tv_jiben);
        tv_work = (TextView) findViewById(R.id.tv_work);
        tv_tuijin = (TextView) findViewById(R.id.tv_tuijin);
        tv_cunzai = (TextView) findViewById(R.id.tv_cunzai);
        tv_jiben.setOnClickListener(this);
        tv_work.setOnClickListener(this);
        tv_tuijin.setOnClickListener(this);
        tv_cunzai.setOnClickListener(this);
        tvs.add(tv_jiben);
        tvs.add(tv_work);
        tvs.add(tv_tuijin);
        tvs.add(tv_cunzai);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_leixing = (TextView) findViewById(R.id.laiyuan);
        tv_time1 = (TextView) findViewById(R.id.tv_time);
        title1= (CenterTextView) findViewById(R.id.tv1);
//        webView.getSettings().setDefaultFontSize(18);
        controlTitle(findViewById(R.id.back));


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
        type = getIntent().getStringExtra("type");
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
            subscription = Api.getMangoApi1().getProjectDetais(canshu)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer3);
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
                title1.setText(channels.data.title);
                laiyan.setText(channels.data.original);
                tv_time.setText(channels.data.publish_date);
                myprog.setVisibility(View.GONE);
                scrollview.setVisibility(View.VISIBLE);
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
                webView.loadDataWithBaseURL(null, channels.data.content, "text/html", "utf-8", null);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
                title1.setText(channels.data.title);
                laiyan.setText(channels.data.original);
                tv_time.setText(channels.data.publish_date);
                myprog.setVisibility(View.GONE);
                scrollview.setVisibility(View.VISIBLE);
            }
        }
    };
    Observer<ProjectialsXiangqing> observer3 = new Observer<ProjectialsXiangqing>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(ProjectialsXiangqing channels) {
            if(channels.status.code.equals("0")){
                data2=channels;
//                webView.loadDataWithBaseURL(null, channels.data.progress, "text/html", "utf-8", null);
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.setWebChromeClient(new WebChromeClient());
//                title1.setText(channels.data.title);
////                laiyan.setText(channels.data.original);
//                tv_time.setText(channels.data.publish_date);
//                if(channels.data.amount.equals("")){
//                    money.setText("项目金额:"+"0元");
//                }else {
//                    money.setText("项目金额" + channels.data.amount + "元");
//                }
//                guihua.setText("类型:"+channels.data.type);
                tv_leixing.setText(channels.data.classify);
                title1.setText(channels.data.title);
                tv_time1.setText(channels.data.publish_date);
                tv_progress.setText("已完成:  "+channels.data.progress+"%");
                progressBar.setProgress(Integer.valueOf(channels.data.progress).intValue());
                initLoadWebView(channels.data.basic);
                myprog.setVisibility(View.GONE);
                scrollview.setVisibility(View.VISIBLE);
            }

        }
    };
    private void initLoadWebView(String url){
        webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_jiben:
                changeColor(0);
                initLoadWebView(data2.data.basic);
                break;
            case R.id.tv_work:
                changeColor(1);
                initLoadWebView(data2.data.requirement);
                break;
            case R.id.tv_tuijin:
                changeColor(2);
                initLoadWebView(data2.data.promotion);
                break;
            case R.id.tv_cunzai:
                changeColor(3);
                initLoadWebView(data2.data.problem);
                break;
        }
    }

    List<TextView> tvs=new ArrayList<>();
    private void changeColor(int position){
        for(int i=0;i<tvs.size();i++){
            if(position==i){
                tvs.get(i).setBackgroundColor(getResources().getColor(R.color.white));
            }else{
                tvs.get(i).setBackgroundColor(getResources().getColor(R.color.item_title));

            }
        }
    }
}