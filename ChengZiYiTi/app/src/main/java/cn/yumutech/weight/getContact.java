package cn.yumutech.weight;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.bean.ChindClass;
import cn.yumutech.bean.DepartList;
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
    public List<GroupClass> groupsDatas=new ArrayList<>();
    public List<List<ChindClass>> chindDatas=new ArrayList<>();
    public List<ChindClass> chindClasses=new ArrayList<>();
    public getContact(){

    }
    public static getContact getInstance() {
        if (instance == null) {
            instance = new getContact();
        }
        return instance;
    }
    private void getData() {
        if (App.getContext().getLogo("logo") != null) {
            mDept_id = App.getContext().getLogo("logo").data.dept_id;
            user_id = App.getContext().getLogo("logo").data.id;
        }
        if (user_id != null && !user_id.equals("")) {
            RequestCanShu canshus = new RequestCanShu(new RequestCanShu.UserBean(user_id, "1234567890"),
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
    Observer<DepartList> observer = new Observer<DepartList>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(DepartList channels) {
            if(channels.status.code.equals("0")){
                String data=new Gson().toJson(channels);
                Log.e("DepartList",data);
                for(int i=0;i<channels.data.size();i++){
                    if(Integer.valueOf(channels.data.get(i).dept_id)>Integer.valueOf(mDept_id)||Integer.valueOf(channels.data.get(i).dept_id)==Integer.valueOf(mDept_id)){
                        groupsDatas.add(new GroupClass(channels.data.get(i).dept_name));
                        getMembersData(user_id,channels.data.get(i).dept_id);
                    }
                }

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
                for(int i=0;i<userAboutPerson.data.size();i++){
//                    chindClasses.add(userAboutPerson.data.get(i).nickname)
                    ChindClass chanData= new ChindClass(userAboutPerson.data.get(i).nickname, R.drawable.next);
                    chindClasses.add(chanData);
                }
                chindDatas.add(chindClasses);
//                chindClasses.clear();
//                mAdapter.dataChange(groupsDatas,chindDatas);
//                mAdapter=new ExpanderAdapter(mActivity,groupsDatas,chindDatas);
//                expandableListView.setAdapter(mAdapter);
            }
        }
    };
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
