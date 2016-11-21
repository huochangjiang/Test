package cn.yumutech.unity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import cn.yumutech.Adapter.InspectionTaskAdapter;

/**
 * Created by Administrator on 2016/11/21.
 */
public class InspectionTaskActivity extends BaseActivity{
    private ViewPager viewPager;
    private InspectionTaskAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_inspection_task;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        viewPager= (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {

    }
}
