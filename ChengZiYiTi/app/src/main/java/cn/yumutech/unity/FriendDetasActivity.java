package cn.yumutech.unity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class FriendDetasActivity extends BaseActivity {


    private WebView webView;
    private TextView title;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_friend_detas;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        title = (TextView) findViewById(R.id.tv_home);
        webView = (WebView) findViewById(R.id.webview);

        controlTitle(findViewById(R.id.back));
    }

    @Override
    protected void initData() {
String url=getIntent().getStringExtra("url");
String intTYpe=getIntent().getStringExtra("title");
        title.setText(intTYpe);
        webView.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

    }

    @Override
    protected void initListeners() {

    }
}
