package cn.yumutech.unity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Allen on 2016/11/13.
 */
public class TaShanCommentsActivity extends BaseActivity{
    private ImageView back;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_tashan_comments;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        back= (ImageView) findViewById(R.id.back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
