package cn.yumutech.unity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.FragmentAdapter;
import cn.yumutech.fragments.MyFaBuDeFragment;
import cn.yumutech.fragments.MyTaskFragment;

/**
 * Created by Administrator on 2016/11/21.
 */
public class InspectionTaskActivity extends BaseActivity implements View.OnClickListener{
    private ViewPager viewpager;

    private TextView mytask,release_task;
    private List<Fragment> fragmentlist=new ArrayList<Fragment>();
    private List<TextView> tvs = new ArrayList<>();
    private ImageView hengxian_mytask,hengxian_release;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_inspection_task;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        viewpager= (ViewPager) findViewById(R.id.viewpager);
        mytask= (TextView) findViewById(R.id.mytask);
        release_task= (TextView) findViewById(R.id.release_task);
        mytask.setOnClickListener(this);
        release_task.setOnClickListener(this);
        hengxian_mytask= (ImageView) findViewById(R.id.hengxian_mytask);
        hengxian_release= (ImageView) findViewById(R.id.hengxian_mytask);
        initFragment();
    }
    private void initFragment(){
        MyTaskFragment myTaskFragment=new MyTaskFragment();
        MyFaBuDeFragment myFaBuDeFragment=new MyFaBuDeFragment();
        fragmentlist.add(myTaskFragment);
        fragmentlist.add(myFaBuDeFragment);
        FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),fragmentlist);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(2);
        tvs.add(mytask);
        tvs.add(release_task);
    }
    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                chanColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void chanColor(int postion) {
        for (int i = 0; i < tvs.size(); i++) {
            TextView tv = tvs.get(i);
            if (i == postion) {
                tv.setTextColor(Color.parseColor("#ffffff"));
            } else {
                tv.setTextColor(Color.parseColor("#000000"));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mytask:
                hengxian_mytask.setVisibility(View.VISIBLE);
                hengxian_release.setVisibility(View.GONE);
                viewpager.setCurrentItem(0);
                break;
            case R.id.release_task:
                hengxian_mytask.setVisibility(View.GONE);
                hengxian_release.setVisibility(View.VISIBLE);
                viewpager.setCurrentItem(1);
                break;
        }
    }
}
