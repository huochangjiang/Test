package cn.yumutech.unity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.yumutech.bean.ShengJiRequest;
import cn.yumutech.bean.Update;
import cn.yumutech.bean.UpdateUserPhoto;
import cn.yumutech.bean.UpdateUserPhotoBeen;
import cn.yumutech.netUtil.Api;
import cn.yumutech.netUtil.DeviceUtils;
import cn.yumutech.netUtil.FileUtils;
import cn.yumutech.netUtil.HttpRequest;
import cn.yumutech.netUtil.ToosUtil;
import cn.yumutech.netUtil.UpdateManager;
import cn.yumutech.weight.DataCleanManager;
import cn.yumutech.weight.FileUtils1;
import cn.yumutech.weight.SaveData;
import cn.yumutech.weight.TiShiDilog;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/14.
 */
public class AfterLoginActivity extends BaseActivity implements View.OnClickListener{
    private final int CUTSIZE = 400; // 截取图片保存的分辨率
    private File tempFile; // 拍照设置头像的零时file
    private final String SaveName = "photo_icon"; // 需要拍照设置头像时，文件保存名
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private String name,logo,size;
    private ImageView touxiang,back;
    private TextView userName;
    private App app;
    private Button unlogo;
    private TextView tv_one,tv_two,tv_size;
    private RelativeLayout clean;
    private Uri uritempFile;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_after_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app= (App) AfterLoginActivity.this.getApplicationContext();
        initExtra();
        EventBus.getDefault().register(this);
        touxiang= (ImageView) findViewById(R.id.touxiang);
        touxiang.setOnClickListener(this);
        userName= (TextView) findViewById(R.id.name);
        back= (ImageView) findViewById(R.id.back);
        clean= (RelativeLayout) findViewById(R.id.one);
        clean.setOnClickListener(this);
        tv_size= (TextView) findViewById(R.id.one).findViewById(R.id.tv_name);
        tv_size.setVisibility(View.VISIBLE);
        tv_one= (TextView) findViewById(R.id.one).findViewById(R.id.tv);
        tv_one.setText("清除缓存");
        tv_two= (TextView) findViewById(R.id.two).findViewById(R.id.tv);
        tv_two.setText("版本更新");
        View view=findViewById(R.id.two);
        view.setOnClickListener(this);
        back.setOnClickListener(this);
        unlogo= (Button) findViewById(R.id.unlogo);
        if(clearImageLoaderCache()/1024/1024>50){
            ImageLoader.getInstance().clearDiskCache();
            ImageLoader.getInstance().clearMemoryCache();
            tv_size.setText("0"+"M");
        }
        try {
            size= DataCleanManager.getFormatSize(clearImageLoaderCache());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_size.setText(size);
        if(size==null){
            tv_size.setText("0"+"M");
        }
      //  tv_size.setText(clearImageLoaderCache() / 1024 / 1024 + "M");
        if(name!=null){
            userName.setText(name);
        }
        if(logo!=null){
            ImageLoader.getInstance().displayImage(logo,touxiang);
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initListeners() {
        unlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.cleanLogoInformation();
                SaveData.getInstance().taskToChildGroups.clear();
//                if(SaveData.getInstance().chindDatas!=null){
//                    SaveData.getInstance().chindDatas.clear();
//                }
                if(App.getContext().getContactGroup("Contact")!=null){
                    App.getContext().cleanContactGroup();
                }
                Intent intent=new Intent(AfterLoginActivity.this,LogoActivity.class);
                startActivity(intent);

                finish();
//                if(RongIM.getInstance()!=null){
//                    RongIM.getInstance().startPrivateChat(AfterLoginActivity.this, "4", "title");
//
//                }
//                app.cleanLogoInformation();
            }
        });
    }
    private void initExtra(){
        if(getIntent()!=null){
            name=getIntent().getStringExtra("name");
            logo=getIntent().getStringExtra("logo");
        }
        if(App.getContext().getLogo("logo")!=null){
            logo=App.getContext().getLogo("logo").data.logo_path;
        }
        if(App.getContext().getUpdateUserPhoto("upLogo")!=null){
            logo=App.getContext().getUpdateUserPhoto("upLogo").data.logo_path;
        }
    }

    Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    MissDilog();
                    TiShiDilog tiShiDilog=new TiShiDilog(AfterLoginActivity.this);
                    tiShiDilog.show();
                    break;
                case 1:
                    MissDilog();
                    showDilog("升级中，请稍后...");
                    break;
                case 2:
                    MissDilog();
                    break;
                case 3:
                    Toast.makeText(AfterLoginActivity.this, "网络出错，请清新下载", Toast.LENGTH_SHORT).show();
                    break;
                case 500:
                    if(App.getContext().getUpdateUserPhoto("upLogo")!=null){
                        Toast.makeText(AfterLoginActivity.this, App.getContext().getUpdateUserPhoto("upLogo").status.message,Toast.LENGTH_SHORT).show();
                        if(App.getContext().getUpdateUserPhoto("upLogo")!=null) {
                            ImageLoader.getInstance().displayImage(App.getContext().getUpdateUserPhoto("upLogo").data.logo_path, touxiang);
                        }
                    }
                    break;
            }
        }
    };

    public void onEventMainThread(Update userAboutPerson){
        Log.e("info","gengxingle ");
        showUpdateDialog(userAboutPerson);
    }
    /**
     * 安装提示对话框
     */
    private void showUpdateDialog(Update mUpdate) {
        View view = LayoutInflater.from(this).inflate(R.layout.welcomedilog, null);

        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view);


        TextView textView_version = (TextView) view.findViewById(R.id.bh);
        TextView shaohou = (TextView) view.findViewById(R.id.text1);
        TextView newNow = (TextView) view.findViewById(R.id.text2);
        TextView textView_log = (TextView) view.findViewById(R.id.tv);
        textView_log.setText(mUpdate.data.getRemarks());
        newNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                installApk( App.getContext().downLoadPath);
                dialog.dismiss();
            }
        });
        shaohou.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 获取屏幕分辨率来控制宽度
        int width = ToosUtil.getInstance().getScreenWidth(this);

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
        startActivity(i);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.one:
                ImageLoader.getInstance().clearDiskCache();
                ImageLoader.getInstance().clearMemoryCache();
                tv_size.setText(clearImageLoaderCache() / 1024 / 1024 + "M");
                break;
            case R.id.two:
                showDilog("检查更新...");
                ShengJiRequest sheng=new ShengJiRequest(new ShengJiRequest.UserBean(App.getContext().getLogo("logo").data.id,"1233454"),new ShengJiRequest.DataBean("Android"));
                UpdateManager.getUpdateManager().initDatas1(new Gson().toJson(sheng),this, DeviceUtils.getAPPVersionCodeFromAPP(this),mHandler);
                break;
            case R.id.touxiang:
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

                break;
        }
    }
    /**
     * 更新头像操作
     */
    Subscription subscription;
    private void setUpdateUserPhoto(Bitmap bitmap){
        if(App.getContext().getLogo("logo")!=null){
            UpdateUserPhotoBeen updateUserPhotoBeen=new UpdateUserPhotoBeen(new UpdateUserPhotoBeen.UserBean(App.getContext().getLogo("logo").data.id,""),
                    new UpdateUserPhotoBeen.DataBean("jpg",bitmaptoString(bitmap,10)));
            HttpRequest.getInstance(this).UpdateUserPhoto(new Gson().toJson(updateUserPhotoBeen),mHandler);
//            setUpdateUserPhoto1(new Gson().toJson(updateUserPhotoBeen));
        }
    }
    private void setUpdateUserPhoto1(String canshu){
        subscription = Api.getMangoApi1().getUpdateUserPhoto(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<UpdateUserPhoto> observer=new Observer<UpdateUserPhoto>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(UpdateUserPhoto updateUserPhoto) {
            if(updateUserPhoto!=null&&updateUserPhoto.status.code.equals("0")){
                Toast.makeText(AfterLoginActivity.this,updateUserPhoto.status.message,Toast.LENGTH_SHORT).show();
                ImageLoader.getInstance().displayImage(updateUserPhoto.data.logo_path,touxiang);
            }
        }
    };
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    /**
     * 检查缓存大小，超过50M就删除
     */
    public Long clearImageLoaderCache() {
        long size = FileUtils1.getDirSize(getDir(App.CachePath, 0)
                .getAbsoluteFile());
        return size;
        // if (size > (1024 * 1024 * 50)) {
        // ImageLoader.getInstance().clearDiskCache();
        // ImageLoader.getInstance().clearMemoryCache();
        // }
    }

    // 头像设置的提示对话框方法
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
                        if(uritempFile!=null){
                            bitmap = BitmapFactory.decodeStream(getContentResolver()
                                    .openInputStream(uritempFile));
                        }
                        setPicToView(bitmaptoString(bitmap, 100));
                        setUpdateUserPhoto(bitmap);

                        ImageLoader.getInstance().clearDiskCache();
                        ImageLoader.getInstance().clearMemoryCache();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    Bitmap bitmap;

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
//                myApp.getLoginInfo().data.uid,
//                dizhi,
//                new CommonCallBack<HeadPhonto>() {
//
//                    @Override
//                    public void onSuccess(HeadPhonto data) {
//                        // TODO Auto-generated method stub
//                        touxiang.setImageBitmap(bitmap);
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
//                        Toast.makeText(UserInfoActivity.this, "头像修改失败", 0).show();
//                    }
//                });
    }
    public String bitmaptoString(Bitmap bitmap, int bitmapQuality) {
        String string1;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bStream);
        byte[] bytes = bStream.toByteArray();
        string1 = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string1;
    }
//    public String bitmaptoString(Bitmap bitmap, int bitmapQuality) {
//        String string1;
//        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream);
//        byte[] bytes = bStream.toByteArray();
//        string1 = Base64.encodeToString(bytes, Base64.DEFAULT);
//        return string1;
//    }
}
