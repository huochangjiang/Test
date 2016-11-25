package cn.yumutech.fragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.GroupsDatasAdapter;
import cn.yumutech.Adapter.SimpleTreeAdapter;
import cn.yumutech.bean.Bean;
import cn.yumutech.bean.FileBean;
import cn.yumutech.bean.GroupClass;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.tree.been.Node;
import cn.yumutech.tree.been.TreeListViewAdapter;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import cn.yumutech.weight.SaveData;
import de.greenrobot.event.EventBus;

/**
 * Created by Allen on 2016/11/13.
 */
public class CommuContactFragment extends BaseFragment{

    private static CommuContactFragment fragment;
    private View contactView;
    private ListView expandableListView;
    private DrawerLayout drawerlayout;
    public String mDept_id;
    public String user_id;
    private List<GroupClass> mData=new ArrayList<>();
    private GroupsDatasAdapter mAdapter;
    private SimpleTreeAdapter adapter;
    private List<Bean> mDatas = new ArrayList<Bean>();
    private List<FileBean> mDatas2 = new ArrayList<FileBean>();


    private EditText search;

    private boolean isOver1;

    public CommuContactFragment() {

    }

    public static CommuContactFragment newInstance() {
        if(fragment==null)
            fragment = new CommuContactFragment();

        return fragment;
    }
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        if (App.getContext().getLogo("logo") != null) {
            //我的ID，现在测试用最高等级
            mDept_id = App.getContext().getLogo("logo").data.dept_id;
            Log.e("info",mDept_id+"huo");
            user_id = App.getContext().getLogo("logo").data.id;
        }
        contactView = inflater.inflate(R.layout.fragment_commu_contact, container, false);
        drawerlayout= (DrawerLayout) contactView.findViewById(R.id.drawerlayout).findViewById(R.id.drawer);
        search= (EditText) contactView.findViewById(R.id.search);
        return contactView;
    }
    //查找出所有子部门的树形递归算法
    @Override
    protected void initViews(View contentView) {
        initExtra();
        expandableListView = (ListView) contentView.findViewById(R.id.expandlistview);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_layout, DralayoutFragment.newInstance()).commitAllowingStateLoss();
        initDatas1();
        try
        {
            adapter = new SimpleTreeAdapter<FileBean>(expandableListView, getActivity(), mDatas2, 10);
            adapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
            {
                @Override
                public void onClick(Node node, int position)
                {
                    if (node.isLeaf())
                    {
                        Toast.makeText(getActivity(), node.getName(),
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

    /**
     * 获取初始化数据
     */
    private void initExtra(){

    }
    @Override
    protected void initListeners() {
        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerlayout.closeDrawers();
                if(mData!=null){
                    SaveData.getInstance().Dept_Id=mData.get(i).dept_id;
                    EventBus.getDefault().post(new UserAboutPerson());
                }

            }
        });

    }

    @Override
    protected void initDatas() {
    }


    private void initDatas1() {
        if(SaveData.getInstance().taskToChildGroups!=null) {
            mData.addAll(SaveData.getInstance().taskToChildGroups);
//            mAdapter = new GroupsDatasAdapter(mActivity, mData);
//            expandableListView.setAdapter(mAdapter);
        }
//        mDatas.add(new Bean(1, 0, "根目录1"));
//        mDatas.add(new Bean(2, 0, "根目录2"));
//        mDatas.add(new Bean(3, 0, "根目录3"));
//        mDatas.add(new Bean(4, 0, "根目录4"));
//        mDatas.add(new Bean(5, 1, "子目录1-1"));
//        mDatas.add(new Bean(6, 1, "子目录1-2"));
//
//        mDatas.add(new Bean(7, 5, "子目录1-1-1"));
//        mDatas.add(new Bean(8, 2, "子目录2-1"));
//
//        mDatas.add(new Bean(9, 4, "子目录4-1"));
//        mDatas.add(new Bean(10, 4, "子目录4-2"));
//
//        mDatas.add(new Bean(11, 10, "子目录4-2-1"));
//        mDatas.add(new Bean(12, 10, "子目录4-2-3"));
//        mDatas.add(new Bean(13, 10, "子目录4-2-2"));
//        mDatas.add(new Bean(14, 9, "子目录4-1-1"));
//        mDatas.add(new Bean(15, 9, "子目录4-1-2"));
//        mDatas.add(new Bean(16, 9, "子目录4-1-3"));
//
//        mDatas2.add(new FileBean(1, 0, " 文件管理系统"));
//        mDatas2.add(new FileBean(2, 1, "游戏"));
//        mDatas2.add(new FileBean(3, 1, "文档"));
//        mDatas2.add(new FileBean(4, 1, "程序"));
//        mDatas2.add(new FileBean(5, 2, "war3"));
//        mDatas2.add(new FileBean(6, 2, "刀塔传奇"));

        for(int i=0;i<mData.size();i++){
            mDatas2.add(new FileBean(Integer.valueOf(mData.get(i).dept_id),Integer.valueOf(mData.get(i).dept_parent_id),mData.get(i).name));
        }
//        mDatas2.add(new FileBean(7, 4, "面向对象"));
//        mDatas2.add(new FileBean(8, 4, "非面向对象"));
//
//        mDatas2.add(new FileBean(9, 7, "C++"));
//        mDatas2.add(new FileBean(10, 7, "JAVA"));
//        mDatas2.add(new FileBean(11, 7, "Javascript"));
//        mDatas2.add(new FileBean(12, 8, "C"));

    }

}
