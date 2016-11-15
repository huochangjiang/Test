package cn.yumutech.unity;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.ConversationListAdapterEx;
import cn.yumutech.Adapter.FragmentAdapter;
import cn.yumutech.fragments.CommuContactFragment;
import cn.yumutech.fragments.CommuGroupFragment;
import cn.yumutech.weight.NetAbout;
import cn.yumutech.weight.NoViewPager;
import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Allen on 2016/11/13.
 */
public class CommunicationActivity extends BaseActivity{
    private NoViewPager viewpager;
    private ImageView back;
    private List<Fragment> fragmentlist=new ArrayList<Fragment>();
    private TextView tv_message,tv_contact,tv_group;
    private ImageView xian;
    private List<TextView> tvs = new ArrayList<>();
    private ConversationListFragment mConversationListFragment = null;
    private Conversation.ConversationType[] mConversationsTypes = null;
    private boolean isDebug;

    private int currIndex;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_communication;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        viewpager= (NoViewPager) findViewById(R.id.viewpager);
        tv_message= (TextView) findViewById(R.id.tv_message);
        tv_contact= (TextView) findViewById(R.id.tv_contact);
        tv_group= (TextView) findViewById(R.id.tv_group);
        back= (ImageView) findViewById(R.id.back);
        xian= (ImageView) findViewById(R.id.hengxian);
        tvs.add(tv_message);
        tvs.add(tv_contact);
        tvs.add(tv_group);
        CollectOnclickListener listener = new CollectOnclickListener();
        tv_message.setOnClickListener(listener);
        tv_contact.setOnClickListener(listener);
        tv_group.setOnClickListener(listener);
        isDebug = getSharedPreferences("config", MODE_PRIVATE).getBoolean("isDebug", false);

        Fragment conversationList = initConversationList();
        CommuContactFragment ccfragment=new CommuContactFragment();
        CommuGroupFragment cgragment=new CommuGroupFragment();
        fragmentlist.add(conversationList);
        fragmentlist.add(ccfragment);
        fragmentlist.add(cgragment);
        FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),fragmentlist);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(3);
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
    }
    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            if (isDebug) {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                        .build();
                mConversationsTypes = new Conversation.ConversationType[] {Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM,
                        Conversation.ConversationType.DISCUSSION
                };

            } else {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .build();
                mConversationsTypes = new Conversation.ConversationType[] {Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM
                };
            }
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
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
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            ObjectAnimator.ofFloat(xian, "x", NetAbout.getInstance().getScreenWidth(CommunicationActivity.this)/5*(position+positionOffset)+ NetAbout.getInstance().getScreenWidth(CommunicationActivity.this)/11+70).setDuration(0).start();
        }

        @Override
        public void onPageSelected(int position) {
//            Animation animation = new TranslateAnimation(currIndex*one,position*one,0,0);//平移动画
            currIndex = position;

//            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
//            animation.setDuration(400);//动画持续时间0.4秒
//            xian.startAnimation(animation);//是用ImageView来显示动画的
            chanColor(position);

        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
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
}
