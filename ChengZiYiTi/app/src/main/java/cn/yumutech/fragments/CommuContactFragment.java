package cn.yumutech.fragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.GroupsDatasAdapter;
import cn.yumutech.bean.DepartList;
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
    private DepartList channels;
    private GroupsDatasAdapter mAdapter;
    public List<GroupClass> groupsDatas=new ArrayList<>();

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
            mDept_id = App.getContext().getLogo("logo").data.dept_id;
            user_id = App.getContext().getLogo("logo").data.id;
        }
        channels=App.getContext().getContactGroup("Contact");
        if(channels!=null&&channels.data.size()>0){
            for(int i=0;i<channels.data.size();i++){
                if(Integer.valueOf(channels.data.get(i).dept_id)>Integer.valueOf(mDept_id)||Integer.valueOf(channels.data.get(i).dept_id)==Integer.valueOf(mDept_id)){
                    groupsDatas.add(new GroupClass(channels.data.get(i).dept_name,channels.data.get(i).dept_id));
//                        SaveData.getInstance().groupsDatas=groupsDatas;
                }
            }
        }

        contactView = inflater.inflate(R.layout.fragment_commu_contact, container, false);
        drawerlayout= (DrawerLayout) contactView.findViewById(R.id.drawerlayout).findViewById(R.id.drawer);
        return contactView;
    }

    @Override
    protected void initViews(View contentView) {

        initExtra();
        expandableListView = (ListView) contentView.findViewById(R.id.expandlistview);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_layout, DralayoutFragment.newInstance()).commitAllowingStateLoss();
        if(groupsDatas!=null) {
            mAdapter = new GroupsDatasAdapter(mActivity, groupsDatas);
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
                SaveData.getInstance().Dept_Id=groupsDatas.get(i).dept_id;
                EventBus.getDefault().post(new UserAboutPerson());
            }
        });

    }
    @Override
    protected void initDatas() {


    }




}
