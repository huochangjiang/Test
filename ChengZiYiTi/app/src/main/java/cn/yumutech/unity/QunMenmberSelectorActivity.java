package cn.yumutech.unity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yumutech.Adapter.MyMenmberAdapter;
import cn.yumutech.bean.CreateQunZu;
import cn.yumutech.bean.JoinQun;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.RequestParams;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.netUtil.Api;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class QunMenmberSelectorActivity extends BaseActivity {


    private TextView tv_quer;
    private boolean isBaoHan=false;
    private ListView listView;
    private List<UserAboutPerson.DataBean> mDatas = new ArrayList<>();
    private List<UserAboutPerson.DataBean> mDatas1 = new ArrayList<>();
    private MyMenmberAdapter mAdapter;
    private String type;
    Subscription subscription;
    Subscription subscription4;
    private String groupId;
    private String groupName;
    private List<String> ids=new ArrayList<>();
    private EditText editText;
    private Button button;

    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_qun_menmber_selector;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Intent intent=getIntent();
        if(intent!=null){
            type = intent.getStringExtra("type");
            groupId = intent.getStringExtra("groupId");
            groupName = intent.getStringExtra("groupName");
            if(type.equals("join")){
                for (int i=0;i<App.getContext().mApbutPerson.size();i++){
                    isBaoHan=false;
                    for (int j=0;j<App.getContext().qunMember.size();j++){
                            if ((App.getContext().mApbutPerson.get(i).id.equals(App.getContext().qunMember.get(j).userId))) {
                                isBaoHan = true;
                            }
                    if(j==App.getContext().qunMember.size()-1) {
                        if (!isBaoHan) {
                            mDatas1.add(App.getContext().mApbutPerson.get(i));
                        }
                    }
                        }

                }
                for (int k=0;k<mDatas1.size();k++){
                    UserAboutPerson.DataBean bean=mDatas1.get(k);
                    bean.type= UserAboutPerson.DataBean.TYPE_NOCHECKED;
                }
            }else if(type.equals("create")){
                mDatas1=App.getContext().mApbutPerson;

            }
        }
        tv_quer = (TextView) findViewById(R.id.tv_qure);
        editText = (EditText) findViewById(R.id.et);
        button = (Button) findViewById(R.id.denglu);
        listView = (ListView) findViewById(R.id.listview);

        mAdapter = new MyMenmberAdapter(mDatas1, this);
        listView.setAdapter(mAdapter);
        controlTitle(findViewById(R.id.back));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().trim()!=null&&!editText.getText().toString().trim().equals("")) {
                    RequestCanShu canshus = new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                            new RequestCanShu.DataBean(App.getContext().getLogo("logo").data.id + "," + mIds, editText.getText().toString().trim()));
                    initDatas4(new Gson().toJson(canshus));
                }else{
                    Toast.makeText(QunMenmberSelectorActivity.this, "請輸入討論組名字", Toast.LENGTH_SHORT).show();
                }
                if(mIds==null||mIds.equals("")){
                    Toast.makeText(QunMenmberSelectorActivity.this, "請添加群成員", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    String mIds;

    @Override
    protected void initData() {
        mAdapter.setLisener(new MyMenmberAdapter.getIds() {
            @Override
            public void getMenmberIds(Map<Integer, UserAboutPerson.DataBean> beans) {
                    mIds = getMemberIds(beans);
            }
        });
        tv_quer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("create")) {
                    if(mIds!=null&&!mIds.equals("")) {

                        for (int i=0;i<App.getContext().mApbutPerson.size();i++){
                            App.getContext().mApbutPerson.get(i).type=0;
                        }
                        Intent intent = new Intent(QunMenmberSelectorActivity.this, CreateQunZhuActivity.class);
                        intent.putExtra("id", mIds);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(QunMenmberSelectorActivity.this, "请选择成员", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(mIds!=null&&!mIds.equals("")) {
//                        Intent intent = new Intent(QunMenmberSelectorActivity.this, CreateQunZhuActivity.class);
//                        intent.putExtra("id", mIds);
//                        startActivity(intent);
//                        finish();

                        if(App.getContext().getLogo("logo")!=null) {


                            RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                                    new RequestParams.DataBean(mIds,groupId,groupName));
                            initDatas1(new Gson().toJson(canshus));
                        }

                    }else{
                        Toast.makeText(QunMenmberSelectorActivity.this, "请选择成员", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getJoinQun(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    @Override
    protected void initListeners() {
    }

    List<String> iids = new ArrayList<>();

    private String getMemberIds(Map<Integer, UserAboutPerson.DataBean> beans) {
        StringBuffer sb = new StringBuffer();
        iids.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            UserAboutPerson.DataBean val = (UserAboutPerson.DataBean) entry.getValue();
            iids.add(val.id);
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


    //加入群组
    Observer<JoinQun> observer = new Observer<JoinQun>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(JoinQun channels) {
            if(channels.status.code.equals("0")){
                for (int i=0;i<App.getContext().mApbutPerson.size();i++){
                    App.getContext().mApbutPerson.get(i).type=0;
                }
                finish();
            }

        }
    };


    //創建qunzhu


    private void initDatas4( String canshu){
        subscription = Api.getMangoApi1().getCreateQunZhu(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer4);

    }
    Observer<CreateQunZu> observer4 = new Observer<CreateQunZu>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription4);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(CreateQunZu channels) {
            if(channels.status.code.equals("0")){
                Group group=new Group(channels.data.groupId,channels.data.groupName, Uri.parse(channels.data.create_user_logo_path));
                RongIM.getInstance().refreshGroupInfoCache(group);
                RongIM.getInstance().startGroupChat(QunMenmberSelectorActivity.this, channels.data.groupId, channels.data.groupName);
                finish();
            }

        }
    };
}
