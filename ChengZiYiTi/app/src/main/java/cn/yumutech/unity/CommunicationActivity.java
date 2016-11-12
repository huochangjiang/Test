package cn.yumutech.unity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.FragmentAdapter;
import cn.yumutech.fragments.CommuContactFragment;
import cn.yumutech.fragments.CommuGroupFragment;
import cn.yumutech.fragments.CommuMessageFragment;

/**
 * Created by Allen on 2016/11/13.
 */
public class CommunicationActivity extends BaseActivity{
    private ViewPager viewpager;
    private ImageView back;
    private List<Fragment> fragmentlist=new ArrayList<Fragment>();
    private TextView tv_message,tv_contact,tv_group;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_communication;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        viewpager= (ViewPager) findViewById(R.id.viewpager);
        tv_message= (TextView) findViewById(R.id.tv_message);
        tv_contact= (TextView) findViewById(R.id.tv_contact);
        tv_group= (TextView) findViewById(R.id.tv_group);
        back= (ImageView) findViewById(R.id.back);
        CollectOnclickListener listener = new CollectOnclickListener();
        tv_message.setOnClickListener(listener);
        tv_contact.setOnClickListener(listener);
        tv_group.setOnClickListener(listener);
        CommuMessageFragment cmfragment=new CommuMessageFragment();
        CommuContactFragment ccfragment=new CommuContactFragment();
        CommuGroupFragment cgragment=new CommuGroupFragment();
        fragmentlist.add(cmfragment);
        fragmentlist.add(ccfragment);
        fragmentlist.add(cgragment);
        FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),fragmentlist);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(3);
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
    private class CollectOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_message:
                    viewpager.setCurrentItem(0);
                    break;
                case R.id.tv_contact:
                    viewpager.setCurrentItem(1);
                    break;
                case R.id.tv_group:
                    viewpager.setCurrentItem(2);
                    break;
            }
        }
    }
}
