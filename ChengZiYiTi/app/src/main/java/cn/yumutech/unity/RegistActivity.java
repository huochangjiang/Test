package cn.yumutech.unity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistActivity extends BaseActivity implements View.OnClickListener{
    private EditText phone;
    private EditText password;
    private Button denglu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        phone = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        denglu = (Button) findViewById(R.id.denglu);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.zhuce:

                break;
        }
    }
}
