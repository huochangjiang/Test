package cn.yumutech.unity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yumutech.bean.Poeple;
import cn.yumutech.bean.PublishTask;
import cn.yumutech.bean.PublishTaskBeen;
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
public class ReleaseTaskActivity extends BaseActivity implements View.OnClickListener{
    private EditText edit_title;
    private EditText edit_neirong;
    private RelativeLayout rl_who;
    private RelativeLayout rl_end_time;
    private RelativeLayout rl_send;
    private TextView who;
    private ImageView back;
    Subscription subscription;
    private TextView choose_time;
    private TextView who_zhu,who_xie;
    private LinearLayout rl_zhipairen;
    public Map<Integer, UserAboutPerson.DataBean> zhipaiPeople;
    private TextView zhubanren;
    private String projectwork_id;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_task;
    }
    private Calendar c = Calendar.getInstance();


    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        maps1.clear();
        maps.clear();
        edit_title= (EditText) findViewById(R.id.edit_title);
        edit_neirong= (EditText) findViewById(R.id.edit_neirong);
        rl_who= (RelativeLayout) findViewById(R.id.rl_who);
        rl_end_time= (RelativeLayout) findViewById(R.id.rl_end_time);
        rl_send= (RelativeLayout) findViewById(R.id.rl_send);
        back= (ImageView) findViewById(R.id.back);
        choose_time= (TextView) findViewById(R.id.choose_time);
        who_zhu= (TextView) findViewById(R.id.who_zhu);
        who_xie= (TextView) findViewById(R.id.who_xie);
        rl_zhipairen= (LinearLayout) findViewById(R.id.rl_zhipairen);
        rl_zhipairen.setVisibility(View.GONE);
        zhubanren= (TextView) findViewById(R.id.zhubanren);
        back.setOnClickListener(this);
        rl_who.setOnClickListener(this);
        rl_end_time.setOnClickListener(this);
        rl_send.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if(getIntent()!=null){
            projectwork_id=getIntent().getStringExtra("projectwork_id");
        }
    }

    @Override
    protected void initListeners() {
        // 为预约时间edittext绑定点击事件
        rl_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReleaseTaskActivity.this, DatePickActivity.class);
                intent.putExtra("date", choose_time.getText().toString());
                startActivityForResult(intent, 1);
            }
        });
    }
    public static Map<Integer,Poeple> maps=new HashMap<>();
    public static Map<Integer,Poeple> maps1=new HashMap<>();
    //将指派人
    public void onEventMainThread(PublishTask task){

        if( SaveData.getInstance().twoPeople!=null&&SaveData.getInstance().twoPeople.size()>0){
            zhuPoeples=SaveData.getInstance().twoPeople;
            maps.clear();
            for(int i=0;i<zhuPoeples.size();i++){
                maps.put(i,zhuPoeples.get(i));
            }

            zhubanren.setText(getMemberIds(maps));
//            who_zhu.setText(zhuPoeples.get(0).name);
//            who_xie.setText(zhuPoeples.get(1).name);
            rl_zhipairen.setVisibility(View.VISIBLE);
        }
    }
    List<String> iids = new ArrayList<>();
    List<String> iids1 = new ArrayList<>();
    //传入数据，将名字用空格隔开
    private String getMemberIds(Map<Integer,Poeple> beans) {
        StringBuffer sb = new StringBuffer();
        iids.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            Poeple val = (Poeple) entry.getValue();
//            for(int j=0;j<zhuPoeples.size();j++){
                iids.add(val.name);
//            }

        }
        for (int i = 0; i < iids.size(); i++) {

            if (i == iids.size() - 1) {
                sb.append(iids.get(i));
            } else {
                sb.append(iids.get(i) + " ");
            }
        }
        return sb.toString();
    }
    //传入数据，将名字用空格隔开
    private String getMemberIds1(Map<Integer,Poeple> beans) {
        StringBuffer sb = new StringBuffer();
        iids1.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            Poeple val = (Poeple) entry.getValue();
//            for(int j=0;j<zhuPoeples.size();j++){
                iids1.add(val.id);
//            }

        }
        for (int i = 0; i < iids1.size(); i++) {

            if (i == iids1.size() - 1) {
                sb.append(iids1.get(i));
            } else {
                sb.append(iids1.get(i) + ",");
            }
        }
        return sb.toString();
    }
    //遍历map集合，。取出其中的人名和id
    private List<Poeple> zhuPoeples=new ArrayList<>();
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_who:
                Intent intent =new Intent();
                intent.setClass(ReleaseTaskActivity.this,TaskToWhoNewActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                break;
//            case R.id.rl_end_time:
//                Intent intent1 =new Intent();
//                intent1.setClass(ReleaseTaskActivity.this,TaskEndDateActivity.class);
//                startActivity(intent1);
//                break;
            case R.id.rl_send:

                mHandler.sendEmptyMessage(1);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(edit_title.getText().toString().trim().isEmpty()){
                        Toast.makeText(ReleaseTaskActivity.this,"您还未填写标题，请完善",Toast.LENGTH_SHORT).show();
                    }else if(edit_neirong.getText().toString().trim().isEmpty()){
                        Toast.makeText(ReleaseTaskActivity.this,"您还未填写内容，请完善",Toast.LENGTH_SHORT).show();
                    }else if(choose_time.getText().toString().isEmpty()){
                        Toast.makeText(ReleaseTaskActivity.this,"您还未选取截止日期，请完善",Toast.LENGTH_SHORT).show();
                    }else if(zhuPoeples.isEmpty()||zhuPoeples.size()<1){
                        Toast.makeText(ReleaseTaskActivity.this,"您还未指派，请完善",Toast.LENGTH_SHORT).show();
                    }else {
                        maps1.clear();
                        for(int i=0;i<zhuPoeples.size();i++){
                            maps1.put(i,zhuPoeples.get(i));
                        }
                        showDilog("发布中...");
                        PublishTaskBeen been=new PublishTaskBeen(new PublishTaskBeen.UserBean(App.getContext().getLogo("logo").data.id,"")
                                ,new PublishTaskBeen.DataBean(edit_title.getText().toString().trim(),edit_neirong.getText().toString().trim(),
                                choose_time.getText().toString(),getMemberIds1(maps1),projectwork_id));
                        initSend(new Gson().toJson(been));
                    }
                    break;
            }
        }
    };
    private void initSend(String canshu){
        subscription = Api.getMangoApi1().getPublishTask(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<PublishTask> observer=new Observer<PublishTask>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(PublishTask publishTask) {
            if(publishTask!=null&&publishTask.status.code.equals("0")){
                MissDilog();
                SaveData.getInstance().isJieshou=true;
                Toast.makeText(ReleaseTaskActivity.this,publishTask.status.message,Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    //选择截止时间时要用，我也不晓得具体啥子用
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            // 选择截止时间时间的页面被关闭
            String date = data.getStringExtra("date");
            if (!choose_time.getText().toString().equals(date)) {
                String my_time=data.getStringExtra("date");
                choose_time.setText(my_time);
            } else {
                System.out.println("选择未变");
            }
        }
    }
}
