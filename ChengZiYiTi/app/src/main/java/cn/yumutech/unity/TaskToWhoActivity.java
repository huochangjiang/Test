package cn.yumutech.unity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yumutech.Adapter.TaskToWhoAdapter;
import cn.yumutech.bean.AssignTask;
import cn.yumutech.bean.AssignTaskBeen;
import cn.yumutech.bean.GroupClass;
import cn.yumutech.bean.Poeple;
import cn.yumutech.bean.PublishTask;
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
    private List<GroupClass> mData=new ArrayList<>();
    private List<UserAboutPerson.DataBean> chengyuanData=new ArrayList<>();
    private ListView listview;
    private RelativeLayout rl_send;
    private String mName;
    private boolean isOver;
    private String type="0";
    private String task_id;
    private TextView qianyiye;
    //保存详情页跳过来的主办人和协办人
    public List<Poeple> detailToThis;

    private Map<Integer, UserAboutPerson.DataBean> mBeen;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_task_to_who;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if(getIntent()!=null){
            type=getIntent().getStringExtra("type");
            task_id=getIntent().getStringExtra("task_id");
        }
        if(SaveData.getInstance().taskToChildGroups!=null) {

            mData.addAll(SaveData.getInstance().taskToChildGroups);
//            mData.add(App.getContext().getDepartment("department"));
        }
        if( SaveData.getInstance().twoPeople.size()>0){
            SaveData.getInstance().twoPeople.clear();
        }
        back= (ImageView) findViewById(R.id.back);
        listview= (ListView) findViewById(R.id.listview);
        rl_send= (RelativeLayout) findViewById(R.id.rl_send);
        qianyiye= (TextView) findViewById(R.id.qianyiye);
        rl_send.setOnClickListener(this);
        if(!SaveData.getInstance().allEmployees.isEmpty()&&SaveData.getInstance().allEmployees.size()>0){
            for(int i=0;i<SaveData.getInstance().allEmployees.size();i++){
                SaveData.getInstance().allEmployees.get(i).setType(0);
            }
        }
        if(type.equals("1")){
            qianyiye.setText("发布任务");
        }else if(type.equals("2")){
            qianyiye.setText("任务详情");
        }
        chengyuanData=SaveData.getInstance().allEmployees;
        adapter=new TaskToWhoAdapter(TaskToWhoActivity.this,chengyuanData);
        listview.setAdapter(adapter);
        isOver=false;
    }
    private int postion;
    private int i=1;
    @Override
    protected void initData() {
    }

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
            public void getMenmberIds(Map<Integer, UserAboutPerson.DataBean> beans,Map<Integer,UserAboutPerson.DataBean> cleanBeen,boolean isAdd) {
                if(isAdd){
                    addPeople(cleanBeen);
                    SaveData.getInstance().twoPeople.addAll(poeples);
                }else {
                    deletePeople(cleanBeen);
                    for(int i=0;i< SaveData.getInstance().twoPeople.size();i++){
                        if((deletepoeples.get(0).id).equals(SaveData.getInstance().twoPeople.get(i).id)){
                            SaveData.getInstance().twoPeople.remove(i);
                        }
                    }
                }
                shijiPoeples=beans;
                cleanBeen.clear();
                detailToThis=SaveData.getInstance().twoPeople;
            }
        });
    }
    //遍历map集合，。取出其中的人名和id放入poeples中，shijiPoeples中存放被选中的人的个数,deletepoeples存放要删除的那一个数据
    private List<Poeple> poeples=new ArrayList<>();
    private List<Poeple> deletepoeples=new ArrayList<>();
    private Map<Integer, UserAboutPerson.DataBean> shijiPoeples;
    //打钩时的正常遍历添加
    private List<Poeple> addPeople(Map<Integer, UserAboutPerson.DataBean> beans){
        poeples.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            UserAboutPerson.DataBean val = (UserAboutPerson.DataBean) entry.getValue();
            poeples.add(new Poeple(val.nickname,val.id));
        }
        return poeples;
    }
    //取消打钩的遍历，从集合中找到他并删除他个狗日的，妈卖批
    private List<Poeple> deletePeople(Map<Integer, UserAboutPerson.DataBean> beans){
        deletepoeples.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            UserAboutPerson.DataBean val = (UserAboutPerson.DataBean) entry.getValue();
            deletepoeples.add(new Poeple(val.nickname,val.id));
        }
        return deletepoeples;
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
//                if(shijiPoeples!=null&&shijiPoeples.size()>2){
//                    Toast.makeText(TaskToWhoActivity.this,"您最多可指派两人",Toast.LENGTH_SHORT).show();
//                }else
//                if(shijiPoeples!=null&&shijiPoeples.size()>0&&shijiPoeples.size()<3&&type.equals("1")) {
                    if(shijiPoeples!=null&&shijiPoeples.size()>0&&type.equals("1")) {
                    EventBus.getDefault().post(new PublishTask());
                    finish();
                }
//                    else if(shijiPoeples!=null&&shijiPoeples.size()>0&&shijiPoeples.size()<3&&type.equals("2")){
                        else if(shijiPoeples!=null&&shijiPoeples.size()>0&&type.equals("2")){
                    mGetAssignTask();
                }else {
                    finish();
                }
                break;
        }
    }

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

    /**
     * 任务详情那边跳转过来的指派人
     */
    Subscription subscription;
    private void mGetAssignTask(){
        if(App.getContext().getLogo("logo")!=null&&detailToThis.size()==2){
            Log.e("ZHU","主办人"+detailToThis.get(0).name+"协办人"+detailToThis.get(1).name);
            AssignTaskBeen assignTaskBeen = new AssignTaskBeen(new AssignTaskBeen.UserBeen(App.getContext().getLogo("logo").data.id,
                    "1234567890"),new AssignTaskBeen.DataBeen(task_id,detailToThis.get(0).id,detailToThis.get(1).id));
            mGetAssignTask1(new Gson().toJson(assignTaskBeen));
        }
    }

    private void mGetAssignTask1(String data) {
        subscription = Api.getMangoApi1().getAssignTask(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<AssignTask> observer=new Observer<AssignTask>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(AssignTask assignTask) {
            if(assignTask!=null&&assignTask.status.code.equals("0")){
                Toast.makeText(TaskToWhoActivity.this,assignTask.status.message,Toast.LENGTH_SHORT).show();;
                finish();
            }
        }
    };
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
