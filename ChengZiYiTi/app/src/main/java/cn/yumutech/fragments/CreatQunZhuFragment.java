package cn.yumutech.fragments;

import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yumutech.Adapter.MyMenmberAdapter;
import cn.yumutech.bean.BaiBao;
import cn.yumutech.bean.CreateQunZu;
import cn.yumutech.bean.JoinQun;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.RequestParams;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.QunMenmberSelectorActivity;
import cn.yumutech.unity.R;
import cn.yumutech.weight.MyEditText;
import cn.yumutech.weight.SaveData;
import cn.yumutech.weight.SignOutDilog1;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CreatQunZhuFragment extends BaseFragment {
    private boolean isBaoHan=false;
    private ListView listView;
    private MyMenmberAdapter mAdapter;
    private String type;
    Subscription subscription;
    Subscription subscription4;
    Subscription subscription5;
    private String groupId;
    private String groupName;
    private List<String> ids=new ArrayList<>();
    private EditText editText;
    private Button button;
    private static CreatQunZhuFragment fragment;
    private View view;
    private QunMenmberSelectorActivity qunMenmberSelectorActivity;
    private boolean isFisrst=true;
    private ImageView iv_huadong;
    private List<UserAboutPerson.DataBean> saveUser=new ArrayList<>();
    private List<UserAboutPerson.DataBean> currentList=new ArrayList<>();
    public CreatQunZhuFragment() {
        // Required empty public constructor
    }


    public static CreatQunZhuFragment newInstance() {
        if (fragment == null){
            fragment = new CreatQunZhuFragment();
    }
        return fragment;
    }
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    String mIds;
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_creat_qun_zhu, container, false);

        return view;
    }
    public List<UserAboutPerson.DataBean> searchData=new ArrayList<>();

    DrawerLayout drawerLayout;
    public void getDrablelayout(DrawerLayout ddd){
        this.drawerLayout=ddd;
    }
    @Override
    protected void initViews(View contentView) {
        saveUser.clear();
        EventBus.getDefault().register(this);
        for (int k=0;k<App.getContext().mApbutPerson.size();k++){
            UserAboutPerson.DataBean bean=App.getContext().mApbutPerson.get(k);
            bean.type= UserAboutPerson.DataBean.TYPE_NOCHECKED;
        }
        qunMenmberSelectorActivity = (QunMenmberSelectorActivity) getActivity();

        editText = (MyEditText) contentView.findViewById(R.id.et);
        button = (Button) contentView.findViewById(R.id.denglu);
        listView = (ListView) contentView.findViewById(R.id.listview);
        mAdapter = new MyMenmberAdapter(saveUser, getActivity(),isXianshi);
        listView.setAdapter(mAdapter);
        iv_huadong= (ImageView) contentView.findViewById(R.id.iv_huadong);
        iv_huadong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        if(qunMenmberSelectorActivity.type.equals("create")){
        button.setText("新建讨论组");
        }else{
            button.setText("添加成员");
        }
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH||actionId==KeyEvent.KEYCODE_ENTER||actionId == EditorInfo.IME_ACTION_DONE) {
                    searchData.clear();
                    String str=editText.getText().toString().trim();
                    getmDataSub(str);
                    listView.setFocusable(false);
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(editText.getText().toString().trim().length()==0){
                    mAdapter.dataChange(currentList,isXianshi);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            private StringBuffer sb;

            @Override
            public void onClick(View view) {
                sb = new StringBuffer();
                for (int i = 0; i < saveUser.size(); i++) {
                    if (saveUser.get(i).getType() == 1) {
                        if (i == saveUser.size() - 1) {
                            sb.append(saveUser.get(i).id);
                        } else {
                            sb.append(saveUser.get(i).id + ",");
                        }
                    }
                }
                        if(qunMenmberSelectorActivity.type.equals("create")) {

                            SignOutDilog1 mDlogOutDilog1=new SignOutDilog1(getActivity(),"讨论组名字");
                            mDlogOutDilog1.show();
                            mDlogOutDilog1.setOnLisener(new SignOutDilog1.onListern1() {
                                @Override
                                public void send(String name) {
                                    RequestCanShu canshus = new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                                            new RequestCanShu.DataBean(App.getContext().getLogo("logo").data.id + "," + sb.toString(), name));
                                    showDilog("创建中...");
                                    initDatas4(new Gson().toJson(canshus));
                                }
                            });

                        }else if(qunMenmberSelectorActivity.type.equals("join")){
                            showDilog("邀请中..");
                            RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                                    new RequestParams.DataBean(sb.toString(), ((QunMenmberSelectorActivity) getActivity()).groupId,( (QunMenmberSelectorActivity) getActivity()).groupName));
                            initDatas10(new Gson().toJson(canshus));
                        }
                }
        });

    }
    /**
     * 联系人查查找
     * @param data
     */
    private boolean isXianshi=true;
    private void getmDataSub(String data){
        if(currentList!=null) {
            for (int i = 0; i < currentList.size(); i++) {
                if (currentList.get(i).nickname.contains(data) || currentList.get(i).mobile.contains(data)) {
                    searchData.add(currentList.get(i));
                }
            }
            mAdapter.dataChange(searchData,isXianshi);
        }
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
    @Override
    protected void initListeners() {

    }

    @Override
    protected void initDatas() {

        if(App.getContext().getLogo("logo")!=null&&App.getContext().getLogo("logo").data!=null&&App.getContext().getLogo("logo").data.dept_id!=null) {
            RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new RequestParams.DataBean(App.getContext().getLogo("logo").data.dept_id));
            initDatas1(new Gson().toJson(canshus));
        }else {
            Toast.makeText(getActivity(),"您还未登陆",Toast.LENGTH_SHORT).show();
        }

        mAdapter.setLisener(new MyMenmberAdapter.getIds() {
            @Override
            public void getMenmberIds(Map<Integer, UserAboutPerson.DataBean> beans) {
                mIds = getMemberIds(beans);
            }
        });
    }



    private void initDatas4( String canshu){
        subscription = Api.getMangoApi1().getCreateQunZhu(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer4);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private List<UserAboutPerson.DataBean> data;
//创建临时的需要刷新的集合
    public List<UserAboutPerson.DataBean> linshiPersons=new ArrayList<>();
    public void onEventMainThread(BaiBao userAboutPerson){
        linshiPersons.clear();
        //SaveData.getInstance().createDetpt_id;
        if(userAboutPerson.getId()==0) {
            isXianshi=true;
        }else{
            isXianshi=false;
        }

        RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                new RequestParams.DataBean(SaveData.getInstance().createDetpt_id));
        initDatas1(new Gson().toJson(canshus));
    }
    //判断是增加还是移除
    public void onEventMainThread(UserAboutPerson.DataBean userAboutPerson){
        if(userAboutPerson.type==0){
        for (int i = 0; i< saveUser.size(); i++){
            if(userAboutPerson.id.equals(saveUser.get(i).id)){
                saveUser.remove(i);
                }
            }
        }else{
            saveUser.add(userAboutPerson);
        }

    }


    Observer<CreateQunZu> observer4 = new Observer<CreateQunZu>() {
        @Override
        public void onCompleted() {
            MissDilog();
            unsubscribe(subscription4);
        }
        @Override
        public void onError(Throwable e) {
            MissDilog();
            e.printStackTrace();

        }
        @Override
        public void onNext(CreateQunZu channels) {
            MissDilog();
            if(channels.status.code.equals("0")){
                Group group=new Group(channels.data.groupId,channels.data.groupName, Uri.parse(channels.data.create_user_logo_path));

                RongIM.getInstance().refreshGroupInfoCache(group);
                RongIM.getInstance().startGroupChat(getActivity(), channels.data.groupId, channels.data.groupName);
                getActivity().finish();
            }

        }
    };



    private void initDatas1( String canshu){
        //获取用户相关列表
        subscription5 = Api.getMangoApi1().getUserAboutPerson(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer5);

    }
    Observer<UserAboutPerson> observer5 = new Observer<UserAboutPerson>() {
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
                linshiPersons.clear();
                if(qunMenmberSelectorActivity.type!=null&&qunMenmberSelectorActivity.type.equals("join")) {
                    for (int m = 0; m < channels.data.size(); m++) {
                        for (int n = 0; n < App.getContext().qunMember.size(); n++) {
                            if (channels.data.get(m).id.equals(App.getContext().qunMember.get(n).userId)) {
                                channels.data.remove(m);
                            }
                        }
                    }
                }
                if(isFisrst) {
                    mAdapter.dataChange(channels.data, true);
                    isFisrst=false;
                }else{
                    for (int i=0;i<saveUser.size();i++){
                        for (int j=0;j<channels.data.size();j++){
                            if(saveUser.get(i).id.equals(channels.data.get(j).id)){
                                channels.data.get(j).type=1;
                            }
                        }
                    }
                    mAdapter.dataChange(channels.data,isXianshi);

                }
                currentList=channels.data;
            }
        }
    };


    Subscription subscription10;
    private void initDatas10( String canshu){
        subscription10 = Api.getMangoApi1().getJoinQun(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer10);

    }
    //加入群组
    Observer<JoinQun> observer10 = new Observer<JoinQun>() {
        @Override
        public void onCompleted() {
            MissDilog();
            unsubscribe(subscription10);
        }
        @Override
        public void onError(Throwable e) {
            MissDilog();
            e.printStackTrace();

        }
        @Override
        public void onNext(JoinQun channels) {
            MissDilog();
            if(channels.status.code.equals("0")){
                getActivity(). finish();
            }

        }
    };

}
