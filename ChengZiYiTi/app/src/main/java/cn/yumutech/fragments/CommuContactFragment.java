package cn.yumutech.fragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import cn.yumutech.bean.UserToken;
import cn.yumutech.tree.been.Node;
import cn.yumutech.tree.been.TreeListViewAdapter;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import cn.yumutech.weight.SaveData;
import de.greenrobot.event.EventBus;

/**
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
    private boolean isHave;
    private ImageView iv_huadong,iv_huadong1;

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
            Log.e("infomDept_id",mDept_id+"huo");
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
        DralayoutFragment.newInstance().getDrablelayout(drawerlayout);
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
        iv_huadong= (ImageView) contactView.findViewById(R.id.drawerlayout).findViewById(R.id.iv_huadong);
        iv_huadong1= (ImageView) contactView.findViewById(R.id.drawerlayout).findViewById(R.id.iv_huadong1);
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
        iv_huadong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerlayout.openDrawer(Gravity.LEFT);
                iv_huadong1.setImageResource(R.drawable.celanzhankai);
                iv_huadong.setVisibility(View.GONE);
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
                return true;
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
                isHave=false;
                if(SaveData.getInstance().shuXingData!=null){
                    SaveData.getInstance().Dept_Id=SaveData.getInstance().shuXingData.get(i).dept_id;

                    if(SaveData.getInstance().underTaskToChildGroups!=null&&SaveData.getInstance().underTaskToChildGroups.size()>0){
                        for(int j=0;j<SaveData.getInstance().underTaskToChildGroups.size();j++){
                            if(SaveData.getInstance().Dept_Id.equals(SaveData.getInstance().underTaskToChildGroups.get(j).dept_id)){
                                isHave=true;
                            }else if(!isHave){
                                isHave=false;
                            }
                        }
                        if(isHave){
                            EventBus.getDefault().post(new UserAboutPerson());
                        } else if(!isHave){
                            EventBus.getDefault().post(new UserToken());
                        }
                    }
                }
            }
        });
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

    @Override
    protected void initDatas() {
    }
    private void initDatas1() {
        if(SaveData.getInstance().taskToChildGroups!=null) {
            mData.addAll(SaveData.getInstance().taskToChildGroups);

        }
        for(int i=0;i<mData.size();i++){
            mDatas2.add(new FileBean(Integer.valueOf(mData.get(i).dept_id),Integer.valueOf(mData.get(i).dept_parent_id),mData.get(i).name));
        }
    }

}
