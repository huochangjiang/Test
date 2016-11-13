package cn.yumutech.unity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.yumutech.bean.ExchangeItemBeen;
import cn.yumutech.bean.ExchangeListBeen;
import cn.yumutech.bean.HuDongItem;
import cn.yumutech.bean.HuDongJIaoLiu;
import cn.yumutech.netUtil.Api;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 2016/11/13.
 */
public class TaShanDetailActivity extends BaseActivity{
    private TextView fenlei,time,laiyuan,pinglun,title;
    private RelativeLayout comments;
    private WebView webView;
    private String myId;
    Subscription subscription;
    private ImageView back;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_tashan_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getExtra();
        title= (TextView) findViewById(R.id.title).findViewById(R.id.xinwentitle);
        fenlei= (TextView) findViewById(R.id.title).findViewById(R.id.myclass);
        time= (TextView) findViewById(R.id.title).findViewById(R.id.time);
        laiyuan= (TextView) findViewById(R.id.title).findViewById(R.id.tv_source);
        pinglun= (TextView) findViewById(R.id.title).findViewById(R.id.browse_num);
        comments= (RelativeLayout) findViewById(R.id.title).findViewById(R.id.comments);
        webView = (WebView) findViewById(R.id.webview);
        back= (ImageView) findViewById(R.id.back);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDefaultFontSize(18);
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
                startActivity(intent);
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
        ExchangeItemBeen exchangeItemBeen = new ExchangeItemBeen(new ExchangeItemBeen.UserBean("unity", "1234567890"),
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
                webView.loadDataWithBaseURL(null, huDongItem.data.content, "text/html", "utf-8", null);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
                title.setText(huDongItem.data.title);
                fenlei.setText(huDongItem.data.classify);
                laiyuan.setText(huDongItem.data.original);
                time.setText(huDongItem.data.publish_date);
                pinglun.setText(huDongItem.data.comment_count);
            }
        }
    };

    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
