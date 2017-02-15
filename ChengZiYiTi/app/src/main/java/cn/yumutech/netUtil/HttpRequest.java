package cn.yumutech.netUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;

import cn.yumutech.bean.CompleteBean;
import cn.yumutech.bean.UpdateUserPhoto;
import cn.yumutech.unity.App;

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
        String url="http://111.9.93.229:20080/unity/webservice/ap/FinishTask";
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
    public void UpdateUserPhoto( String dizhi,final Handler mHandler) {
        String url="http://111.9.93.229:20080/unity/webservice/ap/UpdateUserPhoto";
        RequestParams pasParams = new RequestParams();
        pasParams.put("req", dizhi);
        httpclient.post(url, pasParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {
                    String res = new String(arg2);
                    Gson gson = new Gson();
                    UpdateUserPhoto movie = gson.fromJson(res, UpdateUserPhoto.class);
                    if (movie.status.code.equals("0")) {
                        App.getContext().saveLogo("upLogo",res);
                        mHandler.sendEmptyMessage(500);
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
    //下载文件
    public void downLoadFile(final Handler mHandler,String url,String save_path){

        final File localfile = FileUtils.createFile(App.getContext().ExternalImageDir,
                save_path);
        httpclient.setTimeout(1000 * 15);
        httpclient.get(url, new FileAsyncHttpResponseHandler(localfile) {
            @Override
            public void onSuccess(int arg0, Header[] arg1, File arg2) {
                Message message = mHandler.obtainMessage(101);
                message.obj = localfile.getAbsolutePath();
                mHandler.sendMessage(message);
            }
            @Override
            public void onFailure(int arg0, Header[] arg1, Throwable arg2,
                                  File arg3) {
                Message message = mHandler.obtainMessage(102);
                mHandler.sendMessage(message);
            }
        });
    }

}
