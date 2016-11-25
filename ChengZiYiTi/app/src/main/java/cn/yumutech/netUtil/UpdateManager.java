package cn.yumutech.netUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;

import cn.yumutech.bean.Update;
import cn.yumutech.unity.R;
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

	private Update mUpdate;

	public String savePath = "";
	private String apkFilePath = "";
	private static final int ANALYZEDONE = 1;
	public Context mContext;

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
	private void Download(String download, String savepath) {
		httpClient.get(download, new FileAsyncHttpResponseHandler(new File(savepath)) {
			@Override
			public void onSuccess(int arg0, Header[] arg1, File arg2) {
				// 下载成功之后提示用户升级
				showUpdateDialog();
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
				// 下载失败的情况下，清除apk文件
				FileUtils.deleteFolderFile(savePath, false);
			}
		});
	}

	/**
	 * 安装apk
	 * 
	 */
	private void installApk(String apk_path) {

		if (!new File(apk_path).exists()) {
			return;
		}

		// 安装之前先修改apk的权限，避免出现解析包错误的问题
		try {
			String command = "chmod 777 " + apk_path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apk_path), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
	public  String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}
	/**
	 * 安装提示对话框
	 */
	private void showUpdateDialog() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.welcomedilog, null);

		final Dialog dialog = new Dialog(mContext, R.style.transparentFrameWindowStyle);
		dialog.setContentView(view);


		TextView textView_version = (TextView) view.findViewById(R.id.text1);
		TextView textView_log = (TextView) view.findViewById(R.id.text2);


		textView_log.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				installApk(apkFilePath);
				dialog.dismiss();
			}
		});
		textView_version.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// 获取屏幕分辨率来控制宽度
		int width = ToosUtil.getInstance().getScreenWidth((Activity) mContext);

		Window window = dialog.getWindow();
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.width = width * 8 / 10;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);

		dialog.setCanceledOnTouchOutside(false);

		dialog.show();

	}

	/**
	 * 校验下载的apk文件的md5值
	 * 
	 * @param md5
	 *            期望的md5值
	 * @return
	 */
	private boolean MD5Check(String md5) {
		boolean b = false;

		String local_md5 = MD5Util.getFileMD5String(new File(apkFilePath));

		if (local_md5.equals(md5)) {
			b = true;
		}

		return b;
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

	public void initDatas1( String canshu,Context mContext){
		this.mContext=mContext;
		savePath = mContext.getDir("update", 0).getAbsolutePath();
		apkFilePath = savePath + File.separator   + "cz.apk";
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
				if (DeviceUtils.getappVersionCode(mContext) < Integer.valueOf(mUpdate.data.bh)) {
					Download(channels.data.url, apkFilePath);
					Toast.makeText(mContext, "升级中", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

}
