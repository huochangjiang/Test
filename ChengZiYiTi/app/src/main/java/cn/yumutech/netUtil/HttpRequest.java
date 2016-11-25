package cn.yumutech.netUtil;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import cn.yumutech.bean.CompleteBean;

/**
 * Created by huo on 2016/11/24.
 */
public class HttpRequest {


    public static HttpRequest httprequest = null;
    private static AsyncHttpClient httpclient = new AsyncHttpClient();;
    private Context mContext;
    private HttpRequest(Context context) {
        this.mContext = context;
    }

    public static HttpRequest getInstance(Context context) {
        if (httprequest == null) {
            httprequest = new HttpRequest(context);
        }
        return httprequest;
    }


    public void upLoadTouXiang( String dizhi,final Handler mHandler
                              ) {
        String url="http://182.254.167.232:20080/unity/webservice/ap/FinishTask";
        RequestParams pasParams = new RequestParams();
        pasParams.put("req", dizhi);
        httpclient.post(url, pasParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {
                    String res = new String(arg2);
                    Gson gson = new Gson();
                    CompleteBean movie = gson.fromJson(res, CompleteBean.class);
                    if (movie.status.code.equals("0")) {
                        mHandler.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
            }
        });

    }
}
