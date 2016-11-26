package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.SimpleTreeAdapter;
import cn.yumutech.bean.AddPingLun;
import cn.yumutech.bean.FileBean;
import cn.yumutech.bean.GroupClass;
import cn.yumutech.fragments.JoninFragment;
import cn.yumutech.tree.been.Node;
import cn.yumutech.tree.been.TreeListViewAdapter;
import de.greenrobot.event.EventBus;

public class SelectorJoinActivity extends BaseActivity {
    public  String groupId;
    public  String groupName;
    private ListView expandableListView;
    private DrawerLayout drawerlayout;
    private SimpleTreeAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_selector_join;
    }
    @Override
    protected void initViews(Bundle savedInstanceState) {
        Intent intent=getIntent();
        groupId = intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");
        controlTitle(findViewById(R.id.back));
        drawerlayout= (DrawerLayout) findViewById(R.id.drawerlayout).findViewById(R.id.drawer);
        expandableListView = (ListView) findViewById(R.id.expandlistview);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_layout, JoninFragment.newInstance()).commitAllowingStateLoss();
        initDatas1();
        try
        {
            adapter = new SimpleTreeAdapter<FileBean>(expandableListView, SelectorJoinActivity.this, mDatas2, 10);
            adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
            {
                @Override
                public void onClick(Node node, int position)
                {
                    if (node.isLeaf())
                    {
                        Toast.makeText(SelectorJoinActivity.this, node.getName(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

            });

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        expandableListView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerlayout.closeDrawers();
                if(mData!=null){
                    cn.yumutech.weight.SaveData.getInstance().qunMenmberId= cn.yumutech.weight.SaveData.getInstance().shuXingData.get(i).dept_id;
                    EventBus.getDefault().post(new AddPingLun());
                }

            }
        });
    }

    @Override
    protected void initListeners() {

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
}
