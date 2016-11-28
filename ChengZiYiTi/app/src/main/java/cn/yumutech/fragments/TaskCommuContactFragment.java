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
import cn.yumutech.bean.UserToken;
import cn.yumutech.tree.been.Node;
import cn.yumutech.tree.been.TreeListViewAdapter;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import cn.yumutech.weight.SaveData;
import de.greenrobot.event.EventBus;

/**
 * Created by Allen on 2016/11/25.
 */
public class TaskCommuContactFragment extends BaseFragment {
    private static TaskCommuContactFragment fragment;
    private View contactView;
    private ListView expandableListView;
    private DrawerLayout drawerlayout;
    public String mDept_id;
    public String user_id;
    private boolean isHave;

    private List<GroupClass> mData=new ArrayList<>();
    private GroupsDatasAdapter mAdapter;
    private SimpleTreeAdapter adapter;
    private List<Bean> mDatas = new ArrayList<Bean>();
    private List<FileBean> mDatas2 = new ArrayList<FileBean>();


    private EditText search;

    private boolean isOver1;

    public TaskCommuContactFragment() {

    }

    public static TaskCommuContactFragment newInstance() {
        if(fragment==null)
            fragment = new TaskCommuContactFragment();

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
        fragmentManager.beginTransaction().replace(R.id.fragment_layout, TaskDralayoutFragment.newInstance()).commitAllowingStateLoss();
        initDatas1();
        TaskDralayoutFragment.newInstance().getDrablelayout(drawerlayout);
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
        for(int a=0;a<SaveData.getInstance().shuXingData.size();a++){
            Log.e("shu",SaveData.getInstance().shuXingData.get(a).name+SaveData.getInstance().shuXingData.get(a).dept_id);
        }

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
                        }                  }
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
        }
        for(int i=0;i<mData.size();i++){
            mDatas2.add(new FileBean(Integer.valueOf(mData.get(i).dept_id),Integer.valueOf(mData.get(i).dept_parent_id),mData.get(i).name));
        }


    }
}
