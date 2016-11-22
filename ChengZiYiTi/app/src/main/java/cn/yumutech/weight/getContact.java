package cn.yumutech.weight;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.bean.ChindClass;
import cn.yumutech.bean.DepartListNew;
import cn.yumutech.bean.GroupClass;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.RequestParams;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.App;
import cn.yumutech.unity.R;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/19.
 */
public class getContact {
    Subscription subscription;
    Subscription subscription1;
    public static getContact instance;
    public String mDept_id;
    public String user_id;
    public String nickname;
    private DepartListNew channels;
    public List<GroupClass> groupsDatas1=new ArrayList<>();
    public List<List<ChindClass>> chindDatas=new ArrayList<>();
    public List<ChindClass> chindClasses=new ArrayList<>();



    public List<GroupClass> allGroupsDatas=new ArrayList<>();
    public List<GroupClass> groupsDatas=new ArrayList<>();
    public List<GroupClass> childGroupsDatasNode=new ArrayList<>();
    public List<GroupClass> groupsDatasNode=new ArrayList<>();
    public List<GroupClass> childGroupsDatas=new ArrayList<>();
    List<GroupClass> threechildList= new ArrayList<>();
    private List<GroupClass> groupClasses;
    private boolean isHaveChild;
    private boolean isOver=false;
    public getContact(){

    }
    public static getContact getInstance() {
        if (instance == null) {
            instance = new getContact();
        }
        return instance;
    }
    public void getData() {
        if (App.getContext().getLogo("logo") != null) {
//            mDept_id = App.getContext().getLogo("logo").data.dept_id;
            mDept_id ="3";
            user_id = App.getContext().getLogo("logo").data.id;
            nickname=App.getContext().getLogo("logo").data.nickname;
        }
        if (user_id != null && !user_id.equals("")&&nickname!=null) {
            RequestCanShu canshus = new RequestCanShu(new RequestCanShu.UserBean(nickname, user_id),
                    new RequestCanShu.DataBean(null));
            initDatas1(new Gson().toJson(canshus));
        }
    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getBumenList(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    private void getMembersData(String user_id,String id){
        if(id!=null&&!id.equals("")){
            RequestParams canshus = new RequestParams(new RequestParams.UserBean(user_id, "1234567890"),
                    new RequestParams.DataBean(id));
            initDatas2(new Gson().toJson(canshus));
        } else {
//            Toast.makeText(getActivity(),"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }
    Observer<DepartListNew> observer = new Observer<DepartListNew>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(DepartListNew channels) {
            if(channels.status.code.equals("0")){
                String data=new Gson().toJson(channels);
                Log.e("DepartList",data);
                groupsDatas1.clear();
                App.getContext().savaHomeJson("Contact",data);
                mHandler.sendEmptyMessage(2);
            }
        }
    };

    private void  initDatas2(String canshu){
        subscription1 = Api.getMangoApi1().getUserAboutPerson(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }
    Observer<UserAboutPerson> observer1=new Observer<UserAboutPerson>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription1);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(UserAboutPerson userAboutPerson) {
            if(userAboutPerson.status.code.equals("0")){
                chindClasses.clear();
                for(int i=0;i<userAboutPerson.data.size();i++){
//                    chindClasses.add(userAboutPerson.data.get(i).nickname)
                    ChindClass chanData= new ChindClass(userAboutPerson.data.get(i).nickname, R.drawable.next);
                    chindClasses.add(chanData);
                }
                chindDatas.add(chindClasses);

//                SaveData.getInstance().chindDatas=chindDatas;
            }
        }
    };
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
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
//                    childGroupsDatasNode.add(0,groupsDatas.get(0));
                    groupsDatas.addAll(childGroupsDatasNode);
                    SaveData.getInstance().taskToChildGroups=childGroupsDatasNode;
                    for (int m=0;m<childGroupsDatasNode.size();m++){
                        Log.e("info",childGroupsDatasNode.get(m).name);
                    }
                    break;
                case 2:
                    channels=App.getContext().getContactGroup("Contact");
                    String data=new Gson().toJson(channels);
                    if(channels!=null&&channels.data.size()>0){
                        for(int i=0;i<channels.data.size();i++){
                            if(channels.data.get(i).dept_id.equals(mDept_id)&&mDept_id!=null){
                                groupsDatas.add(new GroupClass(channels.data.get(i).dept_name,channels.data.get(i).dept_id,channels.data.get(i).dept_parent_id));
                                childGroupsDatasNode.add(new GroupClass(channels.data.get(i).dept_name,channels.data.get(i).dept_id,channels.data.get(i).dept_parent_id));
                            }
                            allGroupsDatas.add(new GroupClass(channels.data.get(i).dept_name,channels.data.get(i).dept_id,channels.data.get(i).dept_parent_id));
                        }
                    }
                    if(mDept_id!=null){
                        groupClasses = childrenGroups(mDept_id);
                        childGroupsDatasNode.addAll(groupClasses);
                        threrGroups(groupClasses);
                    }
                    mHandler.sendEmptyMessageDelayed(1,1000);
                    break;
            }
        }
    };
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
}
