package cn.yumutech.unity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.CompleteAdapter;
import cn.yumutech.bean.CompleteBean;
import cn.yumutech.bean.TiJiaoCanShu;
import cn.yumutech.netUtil.Api;
import cn.yumutech.netUtil.FileUtils;
import cn.yumutech.weight.MyGridView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CompleteActivity extends BaseActivity implements View.OnClickListener{
    CompleteAdapter mAdapter;
    List<Bitmap> bitmapBeen=new ArrayList<>();
    private File tempFile; // 拍照设置头像的零时file
    private final int CUTSIZE = 400; // 截取图片保存的分辨率
    TiJiaoCanShu.DataBean dataBean= new TiJiaoCanShu.DataBean();
    private final String SaveName = "photo_icon"; // 需要拍照设置头像时，文件保存名

    private ImageView touxiang;

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    public static final int MODIFYAVATARING = 99; // 修改头像请求开始
    public static final int MODIFYAVATAROK = 100; // 修改成功
    public static final int MODIFYAVATARFAIL = 101; // 修改失败
    private DisplayImageOptions options;

    private Uri uritempFile;
    private EditText editText;
    private String taskId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_complete;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Intent intent=getIntent();
        if(intent!=null){
            taskId = intent.getStringExtra("tastid");
        }
          RelativeLayout rl = (RelativeLayout) findViewById(R.id.id_toolbar).findViewById(R.id.rl);
        rl.setOnClickListener(this);
        Button mButton= (Button) findViewById(R.id.denglu);
        editText = (EditText) findViewById(R.id.edit_neirong);
        mButton.setOnClickListener(this);
        mAdapter=new CompleteAdapter(this,bitmapBeen);
        MyGridView myGridView= (MyGridView) findViewById(R.id.gridView);
        myGridView.setAdapter(mAdapter);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==bitmapBeen.size()){
                    tempFile = FileUtils.createFile(App.ExternalImageDir, SaveName);
                    /**
                     * 没有外部存储器的情况下只调用系统相册进行头像设置
                     */
                    if (FileUtils.checkSaveLocationExists() && tempFile != null) {
                        showDialog();
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    }

                }
            }
        });
    }
    // 头像设置的提示对话框方法
    @SuppressLint("NewApi")
    private void showDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_avatar,
                null);

        final Dialog dialog = new Dialog(this,
                R.style.transparentFrameWindowStyle);
        dialog.setContentView(view);

        Button btn_camera = (Button) view
                .findViewById(R.id.dialog_avatar_camera);
        Button btn_photo = (Button) view.findViewById(R.id.dialog_avatar_photo);
        Button btn_cancel = (Button) view
                .findViewById(R.id.dialog_avatar_cancel);

        btn_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

                // 调用系统的拍照功能
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定调用相机拍照后照片的储存路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);

            }
        });
        btn_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

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
        wl.y = getWindowManager().getDefaultDisplay().getHeight();

        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);

        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用

                if (tempFile != null && tempFile.exists()) {
                    startPhotoZoom(Uri.fromFile(tempFile), CUTSIZE);
                }
                break;

            case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
                // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null)
                    startPhotoZoom(data.getData(), CUTSIZE);
                break;

            case PHOTO_REQUEST_CUT:// 返回的结果
                if (data != null)
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(uritempFile));
                        if(bitmapBeen.size()>9){
                            bitmapBeen.remove(0);
                        }
                        bitmapBeen.add(bitmap);
                        mAdapter.dataChange(bitmapBeen);
                        mPhoneBeans.add(new TiJiaoCanShu.DataBean.PhotosBean("jpg",bitmaptoString(bitmap,10)));
//                        setPicToView(bitmaptoString(bitmap, 100));
//					      touxiang.setImageBitmap(bitmap);

                        ImageLoader.getInstance().clearDiskCache();
                        ImageLoader.getInstance().clearMemoryCache();
                        Log.e("info", "aaaaa");
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public String bitmaptoString(Bitmap bitmap, int bitmapQuality) {
        String string1;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bStream);
        byte[] bytes = bStream.toByteArray();
        string1 = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string1;
    }

    /**
     * 启动系统剪裁功能
     *
     * @param uri
     * @param size
     */
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);

        uritempFile = Uri.parse("file://" + "/"
                + Environment.getExternalStorageDirectory().getPath() + "/"
                + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    // 将进行剪裁后的图片显示到UI界面上,并发出更换头像的网络请求
    private void setPicToView(String dizhi) {
//        HttpRequest.getInstance(this).upLoadTouXiang(
//                App.getContext().getLoginInfo().data.uid, dizhi,
//                new CommonCallBack<HeadPhonto>() {
//
//                    @Override
//                    public void onSuccess(HeadPhonto data) {
//                        // TODO Auto-generated method stub
////							touxiang.setImageBitmap(bitmap);
//                        ImageLoader.getInstance().displayImage(data.data.avatar,touxiang,options);
//                        ToosUtil.getInstance().addEventUmeng(UserInfoActivity.this,"event_modify_header");
//                        ImageLoader.getInstance().clearDiskCache();
//                        ImageLoader.getInstance().clearMemoryCache();
//                    }
//
//                    @Override
//                    public void onStart() {
//                        // TODO Auto-generated method stub
//
//                    }
//
//                    @Override
//                    public void onFailed(String reason) {
//                        // TODO Auto-generated method stub
//                        Toast.makeText(UserInfoActivity.this, "头像修改失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    Bitmap bitmap;
    @Override
    protected void initData() {

    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getComplete(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    @Override
    protected void initListeners() {

    }

    List<TiJiaoCanShu.DataBean.PhotosBean> mPhoneBeans=new ArrayList<>();
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl:
                finish();
                break;
            case R.id.denglu:
                dataBean.setTask_comment(editText.getText().toString().trim());
                dataBean.setTask_id(taskId);
                dataBean.setPhotos(mPhoneBeans);
         initDatas1(new Gson().toJson(new TiJiaoCanShu(new TiJiaoCanShu.UserBean(App.getContext().getLogo("logo").data.id,"1234345"),
        dataBean)));
                break;
        }
    }

    Subscription subscription;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    Observer<CompleteBean> observer = new Observer<CompleteBean>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(CompleteBean channels) {
            if(channels.status.code.equals("0")){
                finish();
            }

        }
    };
}
