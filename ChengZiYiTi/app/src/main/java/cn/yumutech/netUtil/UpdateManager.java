package cn.yumutech.netUtil;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;

import cn.yumutech.bean.Update;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 应用程序更新工具包
 * 
 * 目前是检查到更新之后下载安装包，下载完成之后再提示用户升级
 */
public class UpdateManager {
	private String packgeName="com.xgimi.zhushou";
	private String type="all";

	private static UpdateManager updateManager;

	private static AsyncHttpClient httpClient;

	private static Gson gson;

	public Update mUpdate;

	public String savePath = "";
	private String apkFilePath = "";
	private static final int ANALYZEDONE = 1;
	public Context mContext;
	public int mCurrentVesionCode;

	public static UpdateManager getUpdateManager() {

		if (updateManager == null) {

			updateManager = new UpdateManager();
			httpClient = new AsyncHttpClient();
			gson = new Gson();

		}

		return updateManager;
	}

	/**
	 * 下载资源
	 * 
	 * @param download
	 *            下载地址
	 * @param savepath
	 *            保存地址
	 */
	public void Download(String download, String savepath) {
		httpClient.get(download, new FileAsyncHttpResponseHandler(new File(savepath)) {
			@Override
			public void onSuccess(int arg0, Header[] arg1, File arg2) {
				// 下载成功之后提示用户升级
				mHandler.sendEmptyMessage(2);
				EventBus.getDefault().post(mUpdate);

			}
			@Override
			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
				// 下载失败的情况下，清除apk文件
//				FileUtils.deleteFolderFile(UserGetToken.getInstance(mContext).path, false);
				mHandler.sendEmptyMessage(3);
			}
		});
	}


	/**
	 * 获取封面更新图片
	 */
//	private void getUpdateImage() {
//		final String imagename = mUpdate.getImagename();
//		final String imagemd5 = mUpdate.getImagemd5();
//		final String imageUrl = mUpdate.getImageUrl();
//
//		final String dir_path = FileUtils.getAppCache(mContext, "xgimipage");
//		String image_path = dir_path + File.separator + imagename;
//		final File image_file = new File(image_path);
//
//		if (StringUtils.isEmpty(imagename) || StringUtils.isEmpty(imagemd5) || StringUtils.isEmpty(imageUrl)) {
//			FileUtils.deleteFolderFile(dir_path, false);
//			return;
//		}
//		if (image_file.exists() && MD5Util.getFileMD5String(image_file).equals(imagemd5)) {
//			return;
//		}
//		FileUtils.deleteFolderFile(dir_path, false);
//		httpClient.get(imageUrl, new FileAsyncHttpResponseHandler(image_file) {
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, File arg2) {
//				Log.e("httpClient", "getUpdateImage_onSuccess");
//				if (!MD5Util.getFileMD5String(image_file).equals(imagemd5)) {
//					FileUtils.deleteFolderFile(dir_path, false);
//				}
//			}
//			@Override
//			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
//				Log.e("httpClient", "getUpdateImage_onFailure");
//				FileUtils.deleteFolderFile(dir_path, false);
//			}
//		});
//
//	}
Handler mHandler;
	public void initDatas1(String canshu, Context mContext, int versionCode, Handler mhandler){
		this.mContext=mContext;
		this.mHandler=mhandler;
		this.mCurrentVesionCode=versionCode;

		subscription = Api.getMangoApi1().getSheBeiXinXi(canshu)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(observer);

	}
	Subscription subscription;
	protected void unsubscribe( Subscription subscription) {
		if (subscription != null && !subscription.isUnsubscribed()) {
			subscription.unsubscribe();
		}
	}
	Observer<Update> observer = new Observer<Update>() {
		@Override
		public void onCompleted() {
			unsubscribe(subscription);
		}
		@Override
		public void onError(Throwable e) {
			e.printStackTrace();
		}
		@Override
		public void onNext(Update channels) {
			if(channels.status.code.equals("0")){
				mUpdate=channels;
				if (mCurrentVesionCode < Integer.valueOf(mUpdate.data.bh)) {
					mUpdate=channels;
					mHandler.sendEmptyMessage(1);
//					Download(channels.data.url, App.getContext().downLoadPath);
				}else{
					mHandler.sendEmptyMessage(0);
				}
			}
		}
	};

}
