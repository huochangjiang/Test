package cn.yumutech.unity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.umeng.message.PushAgent;

/**
 * Created by 霍长江 on 2016/11/6.
 */
public abstract  class BaseActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        this.setContentView(this.getLayoutId());
        this.initViews(savedInstanceState);
        this.initData();
        this.initListeners();
    }



    /**
     * Fill in layout id
     *
     * @return layout id
     */
    protected abstract int getLayoutId();


    /**
     * Initialize the view in the layout
     *
     * @param savedInstanceState savedInstanceState
     */
    protected abstract void initViews(Bundle savedInstanceState);
    /**
     * Initialize the Activity data
     */
    protected abstract void initData();


    /**
     * Initialize the View of the listener
     */
    protected abstract void initListeners();


    /**
     * 遥控器的替换fragment，不添加进返回栈，不添加转场动画
     *
     * @param home
     */
    public void showFragmentWithoutBackStackAndAnim1(Fragment home) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.replace(R.id.layout, home).commit();
    }
    /**
     * 替换fragment，不添加进返回栈，不添加转场动画
     *
     * @param home
     */

    /**
     * 替换fragment，不添加进返回栈，不添加转场动画
     *
     */

    public void addFragement(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.layout, fragment).
                commitAllowingStateLoss();
        fragmentTransaction.hide(fragment);
    }
    public void showFragmentWithoutBackStackAndAnim(Fragment home, Fragment hideFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (hideFragment != null) {
            fragmentTransaction.hide(hideFragment);
        }
        fragmentTransaction.show(home).commitAllowingStateLoss();
    }

    public void controlTitle(View view) {
        ImageView iv = (ImageView) view.findViewById(R.id.back);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
