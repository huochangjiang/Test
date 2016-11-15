package cn.yumutech.unity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.yumutech.weight.DataCleanManager;
import cn.yumutech.weight.FileUtils1;

/**
 * Created by Administrator on 2016/11/14.
 */
public class AfterLoginActivity extends BaseActivity implements View.OnClickListener{
    private String name,logo,size;
    private ImageView touxiang,back;
    private TextView userName;
    private App app;
    private Button unlogo;
    private TextView tv_one,tv_two,tv_size;
    private RelativeLayout clean;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_after_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app= (App) AfterLoginActivity.this.getApplicationContext();
        initExtra();
        touxiang= (ImageView) findViewById(R.id.touxiang);
        userName= (TextView) findViewById(R.id.name);
        back= (ImageView) findViewById(R.id.back);
        clean= (RelativeLayout) findViewById(R.id.one);
        clean.setOnClickListener(this);
        tv_size= (TextView) findViewById(R.id.one).findViewById(R.id.tv_name);
        tv_size.setVisibility(View.VISIBLE);
        tv_one= (TextView) findViewById(R.id.one).findViewById(R.id.tv);
        tv_one.setText("清除缓存");
        tv_two= (TextView) findViewById(R.id.two).findViewById(R.id.tv);
        tv_two.setText("关于我们");
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
    protected void initListeners() {
        unlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.cleanLogoInformation();
                finish();
            }
        });
    }
    private void initExtra(){
        if(getIntent()!=null){
            name=getIntent().getStringExtra("name");
            logo=getIntent().getStringExtra("logo");
        }
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
}
