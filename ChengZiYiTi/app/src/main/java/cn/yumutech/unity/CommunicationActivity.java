package cn.yumutech.unity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.ConversationListAdapterEx;
import cn.yumutech.Adapter.FragmentAdapter;
import cn.yumutech.fragments.CommuContactFragment;
import cn.yumutech.fragments.CommuGroupFragment;
import cn.yumutech.weight.NoViewPager;
import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Allen on 2016/11/13.
 */
public class CommunicationActivity extends BaseActivity implements View.OnClickListener{
    private NoViewPager viewpager;
    private ImageView back;
    private List<Fragment> fragmentlist=new ArrayList<Fragment>();
    private TextView tv_message,tv_contact,tv_group;
    private ImageView hengxian_message,hengxian_contact,hengxian_group;
    private List<TextView> tvs = new ArrayList<>();
    private ConversationListFragment mConversationListFragment = null;
    private Conversation.ConversationType[] mConversationsTypes = null;
    private boolean isDebug;
    private RelativeLayout rl_message,rl_contact,rl_group;

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
        hengxian_message= (ImageView) findViewById(R.id.hengxian_message);
        hengxian_contact= (ImageView) findViewById(R.id.hengxian_contact);
        hengxian_group= (ImageView) findViewById(R.id.hengxian_group);

        rl_message= (RelativeLayout) findViewById(R.id.rl_message);
        rl_contact= (RelativeLayout) findViewById(R.id.rl_contact);
        rl_group= (RelativeLayout) findViewById(R.id.rl_group);
        tvs.add(tv_message);
        tvs.add(tv_contact);
        tvs.add(tv_group);

        rl_message.setOnClickListener(this);
        rl_contact.setOnClickListener(this);
        rl_group.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_message:
                hengxian_message.setVisibility(View.VISIBLE);
                hengxian_contact.setVisibility(View.GONE);
                hengxian_group.setVisibility(View.GONE);
                chanColor(0);
                viewpager.setCurrentItem(0);
                break;
            case R.id.rl_contact:
                chanColor(1);
                hengxian_message.setVisibility(View.GONE);
                hengxian_contact.setVisibility(View.VISIBLE);
                hengxian_group.setVisibility(View.GONE);
                viewpager.setCurrentItem(1);
                break;
            case R.id.rl_group:
                chanColor(2);
                hengxian_message.setVisibility(View.GONE);
                hengxian_contact.setVisibility(View.GONE);
                hengxian_group.setVisibility(View.VISIBLE);
                viewpager.setCurrentItem(2);
                break;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConversationListFragment.onDestroy();
        mConversationListFragment=null;

    }
}
