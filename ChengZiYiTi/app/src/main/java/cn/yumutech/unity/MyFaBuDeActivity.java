package cn.yumutech.unity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.FragmentAdapter;
import cn.yumutech.fragments.MyFaBuDeFragment;

/**
 * Created by Administrator on 2017/1/18.
 */
public class MyFaBuDeActivity extends BaseActivity{
    private ViewPager viewpager;
    private ImageView back;
    private List<Fragment> fragmentlist=new ArrayList<Fragment>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_fabu_de;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        viewpager= (ViewPager) findViewById(R.id.viewpager);
        back= (ImageView) findViewById(R.id.back);
        initFragment();
    }

    private void initFragment() {
        MyFaBuDeFragment myFaBuDeFragment=new MyFaBuDeFragment();
        fragmentlist.add(myFaBuDeFragment);
        FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),fragmentlist);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(1);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
