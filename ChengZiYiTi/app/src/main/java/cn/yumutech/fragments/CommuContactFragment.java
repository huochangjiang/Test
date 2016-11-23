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

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.GroupsDatasAdapter;
import cn.yumutech.bean.GroupClass;
import cn.yumutech.bean.UserAboutPerson;
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
        if(SaveData.getInstance().taskToChildGroups!=null) {
            mData.addAll(SaveData.getInstance().taskToChildGroups);
            mAdapter = new GroupsDatasAdapter(mActivity, mData);
            expandableListView.setAdapter(mAdapter);
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


    //



}
