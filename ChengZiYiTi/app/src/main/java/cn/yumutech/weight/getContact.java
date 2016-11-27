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
            mDept_id = App.getContext().getLogo("logo").data.dept_id;
            Log.e("infomDept_id",mDept_id+"huo");
            user_id = App.getContext().getLogo("logo").data.id;
            nickname=App.getContext().getLogo("logo").data.nickname;
        }
        if(SaveData.getInstance().taskToChildGroups!=null){
            SaveData.getInstance().taskToChildGroups.clear();
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
                if (channels!=null&&channels.data!=null&&channels.data.size()>0){
                    SaveData.getInstance().taskToChildGroups.clear();
                    for(int i=0;i<channels.data.size();i++){
                        SaveData.getInstance().taskToChildGroups.add(new GroupClass(channels.data.get(i).dept_name,channels.data.get(i).dept_id,
                                channels.data.get(i).dept_parent_id));
                    }
                }

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
                    SaveData.getInstance().underTaskToChildGroups=childGroupsDatasNode;
                    mHandler.sendEmptyMessageDelayed(50,500);
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
                case 50:
                    getAllMenbers();
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
        }
        return childList;
    }


    /**
     * 以下为指派页面获取所有成员的数据
     */
    private List<UserAboutPerson.DataBean> chengyuanData=new ArrayList<>();
    Subscription subscription2;
    private List<GroupClass> mData=new ArrayList<>();
    private int postion;
    private int i=1;
    private void getAllMenbers(){
         new Thread(new Runnable() {
             @Override
             public void run() {
                 if(SaveData.getInstance().taskToChildGroups!=null) {
                     mData.addAll(SaveData.getInstance().taskToChildGroups);
                 }
                 if(mData!=null&&mData.size()>0){
                     postion=mData.size();
                 }
                 otherHandler.sendEmptyMessageDelayed(10,1000);
//                 otherHandler.sendEmptyMessage(10);
             }
         }).start();

    }
    Handler otherHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 10:
                    if(mData!=null&&mData.size()>0){
                        otherData1(mData.get(i-1).dept_id);
                    }
                    break;
            }
        }
    };
    private void otherData1(String dept_id){
        if(App.getContext().getLogo("logo")!=null&&App.getContext().getLogo("logo").data!=null) {
            RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new RequestParams.DataBean(dept_id));
            otherDatas2(new Gson().toJson(canshus));
        }else {
//            Toast.makeText(TaskToWhoActivity.this,"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }
    private void otherDatas2( String canshu){
        subscription2 = Api.getMangoApi1().getUserAboutPerson(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);

    }
    Observer<UserAboutPerson> observer2 = new Observer<UserAboutPerson>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription2);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
        @Override
        public void onNext(UserAboutPerson channels) {
            if(channels.status.code.equals("0")){
                chengyuanData.addAll(channels.data);
                if(i<postion){
                    isOver=false;
                    Log.e("cishu","来的第"+i+""+"次"+"部门个数"+mData.size()+"");
                    i++;
                    otherHandler.sendEmptyMessageDelayed(10,50);
                }else if(i==postion){
                    isOver=true;
                    Log.e("wanle","走完了"+i+""+"次");
                }
                if(isOver){
                    Log.e("jiashuju",chengyuanData.size()+"");
                    SaveData.getInstance().allEmployees.addAll(removeDuplicate(chengyuanData));
                    for (int i=0;i<SaveData.getInstance().allEmployees.size();i++){
                        Log.e("chengyuan","第"+i+"个员工"+SaveData.getInstance().allEmployees.get(i).nickname);
                    }

//                    adapter.dataChange(removeDuplicate(chengyuanData));
                }
            }
        }
    };
    //去掉重复的数据
    private static List<UserAboutPerson.DataBean> removeDuplicate(List<UserAboutPerson.DataBean> data){
        for (int i=0;i<data.size();i++){
            for(int j=data.size()-1;j>i;j--){
                if(data.get(j).id.equals(data.get(i).id)){
                    data.remove(j);
                }
            }
        }
        return data;
    }
}
