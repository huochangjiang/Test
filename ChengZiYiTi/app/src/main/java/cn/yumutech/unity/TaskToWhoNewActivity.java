package cn.yumutech.unity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.FragmentAdapter;
import cn.yumutech.bean.AssignTask;
import cn.yumutech.fragments.TaskCommuContactFragment;
import cn.yumutech.weight.NoViewPager;
import cn.yumutech.weight.SaveData;
import de.greenrobot.event.EventBus;

/**
 */
public class TaskToWhoNewActivity extends BaseActivity implements View.OnClickListener{
    public ImageView back;
    private NoViewPager viewpager;
    private List<Fragment> fragmentlist=new ArrayList<Fragment>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_task_to_who_new;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        if(getIntent()!=null){
            SaveData.getInstance().type=getIntent().getStringExtra("type");
            SaveData.getInstance().task_id=getIntent().getStringExtra("task_id");
        }
        back= (ImageView) findViewById(R.id.back);
        viewpager= (NoViewPager) findViewById(R.id.viewpager);
        TaskCommuContactFragment taskCommuContactFragment=new TaskCommuContactFragment();
        fragmentlist.add(taskCommuContactFragment);
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
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
    //将指派人
    public void onEventMainThread(AssignTask assignTask){
      finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
