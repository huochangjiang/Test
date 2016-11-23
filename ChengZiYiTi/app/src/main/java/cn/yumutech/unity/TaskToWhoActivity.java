package cn.yumutech.unity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yumutech.Adapter.TaskToWhoAdapter;
import cn.yumutech.bean.GroupClass;
import cn.yumutech.bean.PublishTask;
import cn.yumutech.bean.RequestParams;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.SaveData;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/22.
 */
public class TaskToWhoActivity extends BaseActivity implements View.OnClickListener{
    public ImageView back;
    private TaskToWhoAdapter adapter;
    private List<GroupClass> mData1=new ArrayList<>();
    private List<GroupClass> mData=new ArrayList<>();
    private List<UserAboutPerson.DataBean> chengyuanData=new ArrayList<>();
    Subscription subscription;
    private ListView listview;
    private RelativeLayout rl_send;
    private String mName;
    private boolean isOver;

    private Map<Integer, UserAboutPerson.DataBean> mBeen;
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
        rl_send= (RelativeLayout) findViewById(R.id.rl_send);
        rl_send.setOnClickListener(this);
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
                    if(mData!=null&&mData.size()>0){
                        initData1(mData.get(i-1).dept_id);
                    }
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
        adapter.setLisener(new TaskToWhoAdapter.getIds() {
            @Override
            public void getMenmberIds(Map<Integer, UserAboutPerson.DataBean> beans) {
                mBeen=beans;
                mName = getMemberIds(mBeen);
                SaveData.getInstance().zhiPaiBeen=beans;
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
            }
        }
    };

    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_send:
                EventBus.getDefault().post(new PublishTask());
                finish();
                break;
        }
    }
//    public static List<UserAboutPerson.DataBean> removeDuplicate(List<UserAboutPerson.DataBean> list) {
//        Set set = new LinkedHashSet<UserAboutPerson.DataBean>();
//        set.addAll(list);
//        list.clear();
//        list.addAll(set);
//        return list;
//    }
List<String> iids = new ArrayList<>();
    //传入数据，将名字用，隔开
    private String getMemberIds(Map<Integer, UserAboutPerson.DataBean> beans) {
        StringBuffer sb = new StringBuffer();
        iids.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            UserAboutPerson.DataBean val = (UserAboutPerson.DataBean) entry.getValue();
            iids.add(val.nickname);
        }
        for (int i = 0; i < iids.size(); i++) {

            if (i == iids.size() - 1) {
                sb.append(iids.get(i));
            } else {
                sb.append(iids.get(i) + ",");
            }
        }
        return sb.toString();
    }
}
