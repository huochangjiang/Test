package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogoActivity extends BaseActivity implements View.OnClickListener{

    private EditText phone;
    private EditText password;
    private Button denglu;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_logo;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        phone = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        denglu = (Button) findViewById(R.id.denglu);
        tv = (TextView) findViewById(R.id.zhuce);




    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {
        tv.setOnClickListener(this);
        denglu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.zhuce:
                Intent intent=new Intent(LogoActivity.this,RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.denglu:
                break;


        }
    }
}
