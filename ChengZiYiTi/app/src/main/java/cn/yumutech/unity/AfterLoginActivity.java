package cn.yumutech.unity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/11/14.
 */
public class AfterLoginActivity extends BaseActivity implements View.OnClickListener{
    private String name,logo;
    private ImageView touxiang,back;
    private TextView userName;
    private App app;
    private Button unlogo;
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
        back.setOnClickListener(this);
        unlogo= (Button) findViewById(R.id.unlogo);

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

        }
    }
}
