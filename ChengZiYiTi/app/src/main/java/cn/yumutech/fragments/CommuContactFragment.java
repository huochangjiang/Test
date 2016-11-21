package cn.yumutech.fragments;

import android.os.Handler;
import android.os.Message;
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

    Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    childGroupsDatas.addAll(threechildList);
                    threrGroups(childGroupsDatas);
                    break;
                case 1:
                    groupsDatas.addAll(childGroupsDatasNode);
                    mAdapter.dataChange(childGroupsDatasNode);
                    for (int m=0;m<childGroupsDatasNode.size();m++){
                        Log.e("info",childGroupsDatasNode.get(m).name);
                    }
                    break;


            }


        }
    };

    private EditText search;
    private List<GroupClass> groupClasses;
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
            groupClasses = childrenGroups(mDept_id);
            childGroupsDatasNode.addAll(groupClasses);
            threrGroups(groupClasses);
        }

        contactView = inflater.inflate(R.layout.fragment_commu_contact, container, false);
        drawerlayout= (DrawerLayout) contactView.findViewById(R.id.drawerlayout).findViewById(R.id.drawer);
        search= (EditText) contactView.findViewById(R.id.search);
        mHandler.sendEmptyMessageDelayed(1,1000);
        return contactView;
    }
    public List<GroupClass> allGroupsDatas=new ArrayList<>();
    public List<GroupClass> groupsDatas=new ArrayList<>();
    public List<GroupClass> childGroupsDatasNode=new ArrayList<>();
    public List<GroupClass> groupsDatasNode=new ArrayList<>();
    public List<GroupClass> childGroupsDatas=new ArrayList<>();
    private boolean isHaveChild;
    private boolean isOver=false;
    //查找出所有子部门的树形递归算法


    List<GroupClass> threechildList= new ArrayList<>();
    public void threrGroups(List<GroupClass> data ){
        threechildList.clear();
        isOver=false;

    for (int i=0;i<allGroupsDatas.size();i++){
        for (int j=0;j<data.size();j++) {
            if (data.get(j).dept_id.equals(allGroupsDatas.get(i).dept_parent_id)) {
                threechildList.add(allGroupsDatas.get(i));
                childGroupsDatasNode.add(allGroupsDatas.get(i));
            }
        }
        if(i==allGroupsDatas.size()-1){
            isOver=true;
            childGroupsDatas.clear();
        }
        if(isOver&&threechildList!=null&&threechildList.size()>0) {
            mHandler.sendEmptyMessage(0);
        }

}

    }



    public List<GroupClass> childrenGroups(String myDept_id){
         List<GroupClass> childList=new ArrayList<>();

        if(myDept_id!=null&&allGroupsDatas.size()>0){
            for (int i=0;i<allGroupsDatas.size();i++){
                if(myDept_id.equals(allGroupsDatas.get(i).dept_parent_id)){
                    childList.add(allGroupsDatas.get(i));
                }
            }

//            for(int i=0;i<mData.size();i++) {
//                //有子部门进入循环
//                if (myDept_id.equals(mData.get(i).dept_parent_id)) {
//                    childGroupsDatasNode.add(new GroupClass(mData.get(i).name, mData.get(i).dept_id, mData.get(i).dept_parent_id));
////                    groupsDatasNode.add(new GroupClass(mData.get(i).name, mData.get(i).dept_id, mData.get(i).dept_parent_id));
//                }
//                //判断是否有孩子
//                if(childGroupsDatasNode.size()>0){
//                    isHaveChild=true;
//                }else {
//                    isHaveChild=false;
//                }
//                //判断是否已经遍历完
//                if(isHaveChild) {
//                    if (i == mData.size() - 1) {
//                        isOver = true;
//                        groupsDatasNode.clear();
//                        groupsDatasNode.addAll(childGroupsDatasNode);
//                        groupsDatas.addAll(childGroupsDatasNode);
//                        allGroupsDatas.removeAll(childGroupsDatasNode);
//                        childGroupsDatasNode.clear();
//                    }
//                }
//                if(isOver) {
//                    for (int j = 0; j < groupsDatasNode.size(); j++) {
//                        isOver=false;
//                        childrenGroups(groupsDatasNode.get(j).dept_id, allGroupsDatas);
//                    }
//                }
//            }

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
        return childList;
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


    //



}
