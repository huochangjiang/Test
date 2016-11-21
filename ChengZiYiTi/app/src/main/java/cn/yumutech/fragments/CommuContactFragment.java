package cn.yumutech.fragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.GroupsDatasAdapter;
import cn.yumutech.bean.DepartListNew;
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
    private DepartListNew channels;
    private GroupsDatasAdapter mAdapter;



    private EditText search;
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
//            mDept_id = App.getContext().getLogo("logo").data.dept_id;
            mDept_id = "0";
            user_id = App.getContext().getLogo("logo").data.id;
        }
        channels=App.getContext().getContactGroup("Contact");
        if(channels!=null&&channels.data.size()>0){
            for(int i=0;i<channels.data.size();i++){
                if(channels.data.get(i).dept_id.equals(mDept_id)&&mDept_id!=null){
                    groupsDatas.add(new GroupClass(channels.data.get(i).dept_name,channels.data.get(i).dept_id,channels.data.get(i).dept_parent_id));
                }
                allGroupsDatas.add(new GroupClass(channels.data.get(i).dept_name,channels.data.get(i).dept_id,channels.data.get(i).dept_parent_id));
                //通过自己的dept_id和部门父ID比较，递归找出下面的子部门
//                if(mDept_id.equals(channels.data.get(i).dept_parent_id)){
//                    groupsDatasNode.add(new GroupClass(channels.data.get(i).dept_name,channels.data.get(i).dept_id,channels.data.get(i).dept_parent_id));
//                }
//                if(Integer.valueOf(channels.data.get(i).dept_id)>Integer.valueOf(mDept_id)||Integer.valueOf(channels.data.get(i).dept_id)==Integer.valueOf(mDept_id)){
//                    groupsDatas.add(new GroupClass(channels.data.get(i).dept_name,channels.data.get(i).dept_id));
////                        SaveData.getInstance().groupsDatas=groupsDatas;
//                }
            }
          //  groupsDatas.addAll(groupsDatasNode);
        }
        if(mDept_id!=null){
            childrenGroups(mDept_id,allGroupsDatas);
        }

        contactView = inflater.inflate(R.layout.fragment_commu_contact, container, false);
        drawerlayout= (DrawerLayout) contactView.findViewById(R.id.drawerlayout).findViewById(R.id.drawer);
        search= (EditText) contactView.findViewById(R.id.search);
        return contactView;
    }
    public List<GroupClass> allGroupsDatas=new ArrayList<>();
    public List<GroupClass> groupsDatas=new ArrayList<>();
    public List<GroupClass> childGroupsDatasNode=new ArrayList<>();
    public List<GroupClass> groupsDatasNode=new ArrayList<>();
    public List<GroupClass> childGroupsDatas=new ArrayList<>();
    private boolean isHaveChild;
    private boolean isOver;
    //查找出所有子部门的树形递归算法
    public void childrenGroups(String myDept_id,List<GroupClass> mData){
        if(myDept_id!=null&&mData.size()>0){
            for(int i=0;i<mData.size();i++) {
                //有子部门进入循环
                if (myDept_id.equals(mData.get(i).dept_parent_id)) {
                    childGroupsDatasNode.add(new GroupClass(mData.get(i).name, mData.get(i).dept_id, mData.get(i).dept_parent_id));
//                    groupsDatasNode.add(new GroupClass(mData.get(i).name, mData.get(i).dept_id, mData.get(i).dept_parent_id));
                }
                //判断是否有孩子
                if(childGroupsDatasNode.size()>0){
                    isHaveChild=true;
                }else {
                    isHaveChild=false;
                }
                //判断是否已经遍历完
                if(isHaveChild) {
                    if (i == mData.size() - 1) {
                        isOver = true;
                        groupsDatasNode.clear();
                        groupsDatasNode.addAll(childGroupsDatasNode);
                        groupsDatas.addAll(childGroupsDatasNode);
                        allGroupsDatas.removeAll(childGroupsDatasNode);
                        childGroupsDatasNode.clear();
                    }
                }
                if(isOver) {
                    for (int j = 0; j < groupsDatasNode.size(); j++) {
                        isOver=false;
                        childrenGroups(groupsDatasNode.get(j).dept_id, allGroupsDatas);
                    }
                }
            }

//                if(groupsDatasNode.size()==0){
//                    isHaveChild=false;
//                }
//                if(i==mData.size()-1&&isHaveChild){
//                    groupsDatas.addAll(groupsDatasNode);
//                }
//                if(isOver){
//                    childGroupsDatasNode.addAll(groupsDatasNode);
//                }
//                if(childGroupsDatasNode.size()>0){
//                    for(int j=0;j<childGroupsDatasNode.size();j++){
//                        if(j==groupsDatas.size()-1){
//                            isOver=true;
////                    groupsDatasNode.clear();
//                        }else {
//                            isOver=false;
//                        }
//                        childrenGroups(childGroupsDatasNode.get(j).dept_id,allGroupsDatas);
//
//                    }
//                }
//            }
        }
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
