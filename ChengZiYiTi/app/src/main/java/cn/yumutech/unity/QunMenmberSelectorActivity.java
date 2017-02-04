package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yumutech.Adapter.SimpleTreeAdapter;
import cn.yumutech.bean.BaiBao;
import cn.yumutech.bean.FileBean;
import cn.yumutech.bean.GroupClass;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.fragments.CreatQunZhuFragment;
import cn.yumutech.tree.been.Node;
import cn.yumutech.tree.been.TreeListViewAdapter;
import de.greenrobot.event.EventBus;

public class QunMenmberSelectorActivity extends BaseActivity {
    private ListView expandableListView;
    private DrawerLayout drawerlayout;
    private SimpleTreeAdapter adapter;
    public String type;
    public String groupId;
    public String groupName;
    private ImageView iv_huadong,iv_huadong1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qun_menmber_selector;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        controlTitle(findViewById(R.id.back));
        Intent intent=getIntent();
        type = intent.getStringExtra("type");
        TextView tv= (TextView) findViewById(R.id.tv_home);

        if(type.equals("join")) {
            groupId = intent.getStringExtra("groupId");
            tv.setText("加入讨论组");
            groupName = intent.getStringExtra("groupName");
        }else{
            tv.setText("新建讨论组");
        }
        drawerlayout= (DrawerLayout) findViewById(R.id.drawerlayout).findViewById(R.id.drawer);
        expandableListView = (ListView) findViewById(R.id.expandlistview);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_layout, CreatQunZhuFragment.newInstance()).commitAllowingStateLoss();
        initDatas1();
        CreatQunZhuFragment.newInstance().getDrablelayout(drawerlayout);
        try
        {
            adapter = new SimpleTreeAdapter<FileBean>(expandableListView, QunMenmberSelectorActivity.this, mDatas2, 10);
            adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
            {
                @Override
                public void onClick(Node node, int position)
                {
                    if (node.isLeaf())
                    {
                        Toast.makeText(QunMenmberSelectorActivity.this, node.getName(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

            });

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        expandableListView.setAdapter(adapter);
        iv_huadong= (ImageView) findViewById(R.id.drawerlayout).findViewById(R.id.iv_huadong);
        iv_huadong1= (ImageView) findViewById(R.id.drawerlayout).findViewById(R.id.iv_huadong1);
        iv_huadong.setVisibility(View.VISIBLE);
        iv_huadong1.setVisibility(View.GONE);
        iv_huadong.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                drawerlayout.openDrawer(Gravity.LEFT);
                iv_huadong1.setImageResource(R.drawable.celanzhankai);
                iv_huadong.setVisibility(View.GONE);
                return false;
            }
        });
//        iv_huadong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerlayout.openDrawer(Gravity.LEFT);
//                iv_huadong1.setImageResource(R.drawable.celanzhankai);
//                iv_huadong.setVisibility(View.GONE);
//            }
//        });
        iv_huadong1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                drawerlayout.closeDrawer(Gravity.LEFT);
                iv_huadong.setImageResource(R.drawable.celanshouqi);
                iv_huadong1.setVisibility(View.GONE);
                return false;
            }
        });
//        iv_huadong1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerlayout.closeDrawer(Gravity.LEFT);
//                iv_huadong.setImageResource(R.drawable.celanshouqi);
//                iv_huadong1.setVisibility(View.GONE);
//            }
//        });
    }
    boolean isHave;
    @Override
    protected void initData() {
        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerlayout.closeDrawers();
                if(mData!=null){



                    cn.yumutech.weight.SaveData.getInstance().createDetpt_id= cn.yumutech.weight.SaveData.getInstance().shuXingData.get(i).dept_id;
                    isHave=false;
                    String abc=cn.yumutech.weight.SaveData.getInstance().createDetpt_id;
                    for (int j=0;j<cn.yumutech.weight.SaveData.getInstance().underTaskToChildGroups.size();j++){
                        if( cn.yumutech.weight.SaveData.getInstance().createDetpt_id.equals(cn.yumutech.weight.SaveData.getInstance().underTaskToChildGroups.get(j).dept_id))
                        {
                            isHave=true;
                        }
                    }
                    if(isHave) {
                        EventBus.getDefault().post(new BaiBao("cc", 0));
                    }else{
                        EventBus.getDefault().post(new BaiBao("cc", 1));
                    }
                }

            }
        });

    }

    @Override
    protected void initListeners() {
        drawerlayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                iv_huadong1.setImageResource(R.drawable.celanzhankai);
                iv_huadong1.setVisibility(View.VISIBLE);
                iv_huadong.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                iv_huadong.setImageResource(R.drawable.celanshouqi);
                iv_huadong.setVisibility(View.VISIBLE);
                iv_huadong1.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        });
    }
    private List<FileBean> mDatas2 = new ArrayList<FileBean>();
    private List<GroupClass> mData=new ArrayList<>();

    private void initDatas1() {
        if(cn.yumutech.weight.SaveData.getInstance().taskToChildGroups!=null) {
            mData.addAll(cn.yumutech.weight.SaveData.getInstance().taskToChildGroups);

        }
        for(int i=0;i<mData.size();i++){
            mDatas2.add(new FileBean(Integer.valueOf(mData.get(i).dept_id),Integer.valueOf(mData.get(i).dept_parent_id),mData.get(i).name));
        }
    }
    List<String> iids = new ArrayList<>();

    private String getMemberIds(Map<Integer, UserAboutPerson.DataBean> beans) {
        StringBuffer sb = new StringBuffer();
        iids.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            UserAboutPerson.DataBean val = (UserAboutPerson.DataBean) entry.getValue();
            iids.add(val.id);
        }
        for (int i = 0; i < iids.size(); i++) {

            if (i == iids.size() - 1) {
                sb.append(iids.get(i));
            } else {
                sb.append(iids.get(i) + ",");
            }
        }
        return sb.toString();
    }
}
