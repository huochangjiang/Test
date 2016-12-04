package cn.yumutech.weight;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import cn.yumutech.Adapter.SimpleAdapter;
import cn.yumutech.unity.R;

public class ViewPagerDilog {
	Dialog mDialog;
	Context contetn;
	private ArrayList<String> sds;
	private ImageView baocun;
	
	private TextView tv;
	int mpos;
	private List<Bitmap> mybitmap=new ArrayList<>();
	public ViewPagerDilog(Context context, ArrayList<String> sds, int postion) {
		SaveData.getInstance().mybt.clear();
		LayoutInflater inflater = LayoutInflater.from(context);
		this.contetn=context;
		View view = inflater.inflate(R.layout.activity_phono_view_pager, null);
		mDialog = new Dialog(context, R.style.dialog);
		baocun= (ImageView) view.findViewById(R.id.baocun);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		this.sds=sds;
		Window window = mDialog.getWindow();
		LayoutParams windowparams = window.getAttributes();
		window.setGravity(Gravity.BOTTOM);
		windowparams.width = LayoutParams.FILL_PARENT;
		window.setAttributes(windowparams);
		Rect rect = new Rect();


		mpos=postion;
		View view1 = window.getDecorView();
		view1.getWindowVisibleDisplayFrame(rect);
		initView(view,postion);
		baocun.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(mpos);
			}
		});

	}

	private void initView(View view,int postion) {
		tv = (TextView) view.findViewById(R.id.tv);
		tv.setText((postion+1)+"/"+sds.size());
	
		PhonoViewPager viewpager = (PhonoViewPager) view.findViewById(R.id.viewpager);
		viewpager.setAdapter(new SimpleAdapter(sds,mDialog,contetn));
		viewpager.setCurrentItem(postion);
		for(int i=0;i<SaveData.getInstance().mybt.size();i++){
//			Bitmap bitmap =returnBitMap(sds.get(postion));
			mybitmap.add( SaveData.getInstance().mybt.get(i));
		}
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				tv.setText((arg0+1)+"/"+sds.size());
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	public void show() {
		mDialog.show();
	}
	public void setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
	}
	public void dismiss() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
			// animationDrawable.stop();
		}
	}
	public boolean isShowing() {
		if (mDialog.isShowing()) {
			return true;
		}
		return false;
	}


	//在一定范围内生成不重复的随机数
	//在testRandom2中生成的随机数可能会重复.
	//在此处避免该问题
	private int name;
	private void testRandom3(){
		HashSet integerHashSet=new HashSet();
		Random random=new Random();
		for (int i = 0; i <1000; i++) {
			int randomInt=random.nextInt(20);
			System.out.println("生成的randomInt="+randomInt);
			name=randomInt;
			if (!integerHashSet.contains(randomInt)) {
				integerHashSet.add(randomInt);
				System.out.println("添加进HashSet的randomInt="+randomInt);
				name=randomInt;
			}else {
				System.out.println("该数字已经被添加,不能重复添加");
			}
		}
		System.out.println("/////以上为testRandom3()的测试///////");

	}

	/** 保存方法 */
	public void saveBitmap(Bitmap bitmap, String filePath) {
//        Log.e(TAG, "保存图片");
		testRandom3();
		String fileName= "ChengZi"+name+".jpg";
		File f = new File(filePath,fileName);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Toast.makeText(contetn,"保存成功",Toast.LENGTH_SHORT).show();

//			MediaScannerConnection.scanFile(contetn, new String[]{path.toString()}, null, null);
//			contetn.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://"+path)));
			// 其次把文件插入到系统图库
			try {
				MediaStore.Images.Media.insertImage(contetn.getContentResolver(),
						f.getAbsolutePath(),fileName , null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// 最后通知图库更新
			contetn.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
//            Log.i(TAG, "已经保存");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	private String path;
	//在SD卡上创建一个文件夹
	public void createSDCardDir(){
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			// 创建一个文件夹对象，赋值为外部存储器的目录
			File sdcardDir =Environment.getExternalStorageDirectory();
			//得到一个路径，内容是sdcard的文件夹路径和名字
			path=sdcardDir.getPath()+"/ChengZi";
			File path1 = new File(path);
			if (!path1.exists()) {
				//若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();
				//setTitle("paht ok,path:"+path);
			}
		}
		else{
			//setTitle("false");
			return;

		}
	}
	// 头像设置的提示对话框方法
	@SuppressLint("NewApi")
	private void showDialog(final int myPosition) {

		View view = LayoutInflater.from(contetn).inflate(R.layout.dialog_filetransfer,
				null);

		final Dialog dialog = new Dialog(contetn,
				R.style.transparentFrameWindowStyle);
		dialog.setContentView(view);


		Button dialog_file = (Button) view.findViewById(R.id.dialog_file);
		Button btn_cancel = (Button) view
				.findViewById(R.id.dialog_avatar_cancel);


		dialog_file.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createSDCardDir();
				if(SaveData.getInstance().mybt!=null&&SaveData.getInstance().mybt.size()>0){
					saveBitmap(SaveData.getInstance().mybt.get(myPosition),path);
				}
				dialog.dismiss();
			}
		});

		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		Window window = dialog.getWindow();
		window.setDimAmount(0.2f);
		WindowManager.LayoutParams wl = window.getAttributes();

		wl.x = 0;
		wl.y = window.getWindowManager().getDefaultDisplay().getHeight();

		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);

		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);

		dialog.show();
	}
	/**
	 * string转成bitmap
	 *
	 * @param st
	 */
//	public static Bitmap convertStringToIcon(String st)
//	{
//		// OutputStream out;
//		Bitmap bitmap = null;
//		try
//		{
//			// out = new FileOutputStream("/sdcard/aa.jpg");
//			byte[] bitmapArray;
////			Base64.encodeToString(BitmapToBytesUtil.bitmapToBytes(bitmap), Base64.DEFAULT);
//			bitmapArray = Base64.decode(st, Base64.DEFAULT);
//			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
//							bitmapArray.length);
//			// bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//			return bitmap;
//		}
//		catch (Exception e)
//		{
//			return null;
//		}
//	}
//	public Bitmap returnBitMap(String url) {
//		URL myFileUrl = null;
//		Bitmap bitmap = null;
//		try {
//			myFileUrl = new URL(url);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		try {
//			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
//			conn.setDoInput(true);
//			conn.connect();
//			InputStream is = conn.getInputStream();
//			bitmap = BitmapFactory.decodeStream(is);
//			is.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return bitmap;
//	}

}
