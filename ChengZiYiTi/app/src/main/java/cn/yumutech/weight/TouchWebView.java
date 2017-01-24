package cn.yumutech.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by 霍长江 on 2017/1/24.
 */
public class TouchWebView extends WebView {
    public TouchWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TouchWebView(Context context) {
        super(context);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        requestDisallowInterceptTouchEvent(true);
        return true;
    }

}
