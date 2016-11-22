package cn.yumutech.unity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.yumutech.Adapter.TaskToWhoAdapter;
import cn.yumutech.bean.GroupClass;
import cn.yumutech.bean.RequestParams;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.SaveData;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/22.
 */
public class TaskToWhoActivity extends BaseActivity{
    public ImageView back;
    private TaskToWhoAdapter adapter;
    private List<GroupClass> mData1=new ArrayList<>();
    private List<GroupClass> mData=new ArrayList<>();
    private List<UserAboutPerson.DataBean> chengyuanData=new ArrayList<>();
    Subscription subscription;
    private ListView listview;
    private boolean isOver;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_task_to_who;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if(SaveData.getInstance().taskToChildGroups!=null) {

            mData.addAll(SaveData.getInstance().taskToChildGroups);
//            mData.add(App.getContext().getDepartment("department"));
        }
        back= (ImageView) findViewById(R.id.back);
        listview= (ListView) findViewById(R.id.listview);
        adapter=new TaskToWhoAdapter(TaskToWhoActivity.this,chengyuanData);
        listview.setAdapter(adapter);
        isOver=false;
    }

    private int postion;
    private int i=1;
    @Override
    protected void initData() {
            if(mData!=null&&mData.size()>0){
                postion=mData.size();
            }
            mHandler.sendEmptyMessage(1);

    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    initData1(mData.get(i-1).dept_id);
                    break;
            }
        }
    };
    @Override
    protected void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initData1(String dept_id){
        if(App.getContext().getLogo("logo")!=null&&App.getContext().getLogo("logo").data!=null) {
            RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new RequestParams.DataBean(dept_id));
            initDatas2(new Gson().toJson(canshus));
        }else {
            Toast.makeText(TaskToWhoActivity.this,"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }
    private void initDatas2( String canshu){
        subscription = Api.getMangoApi1().getUserAboutPerson(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    Observer<UserAboutPerson> observer = new Observer<UserAboutPerson>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
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
                    i++;
                    mHandler.sendEmptyMessage(1);
                }else if(i==postion){
                    isOver=true;
                }
                if(isOver){
                    adapter.dataChange(removeDuplicate(chengyuanData));
                }

//                App.getContext().mApbutPerson=channels.data;
            }
        }
    };

    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    //去掉重复的数据
    public static List<UserAboutPerson.DataBean> removeDuplicate(List<UserAboutPerson.DataBean> list) {
        Set set = new LinkedHashSet<UserAboutPerson.DataBean>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }
}
