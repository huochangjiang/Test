package cn.yumutech.fragments;

import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yumutech.Adapter.TaskToWhoAdapter;
import cn.yumutech.bean.AssignTask;
import cn.yumutech.bean.AssignTaskBeen;
import cn.yumutech.bean.Poeple;
import cn.yumutech.bean.PublishTask;
import cn.yumutech.bean.RequestParams;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.bean.UserToken;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import cn.yumutech.weight.SaveData;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 2016/11/25.
 */
public class TaskDralayoutFragment extends BaseFragment implements View.OnClickListener{
    private View rootView;
    private List<UserAboutPerson.DataBean> mDatas=new ArrayList<>();
    private ListView listView;
    private TaskToWhoAdapter mAdapter;
    private EditText search;
    public List<UserAboutPerson.DataBean> searchData=new ArrayList<>();
    private RelativeLayout rl_send;
    //保存详情页跳过来的主办人和协办人
    public List<Poeple> detailToThis;
    private View myprog;
    private View wuquanxian;
    private static TaskDralayoutFragment fragment;
    private ImageView iv;
    public static Map<Integer,Poeple> maps1=new HashMap<>();
//    private boolean isOpen;


    Subscription subscription;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    public TaskDralayoutFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static TaskDralayoutFragment newInstance() {
        if(fragment==null) {
            fragment = new TaskDralayoutFragment();
        }
        return fragment;
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_task_dralayout, container, false);
        return rootView;
    }

    @Override
    protected void initViews(View contentView) {
        EventBus.getDefault().register(this);
        maps1.clear();
        SaveData.getInstance().isPermissions=true;
        rl_send= (RelativeLayout) contentView.findViewById(R.id.rl_send);
        myprog=contentView.findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
        rl_send.setOnClickListener(this);
        wuquanxian=contentView.findViewById(R.id.wuquanxian);
        if( SaveData.getInstance().twoPeople.size()>0){
            SaveData.getInstance().twoPeople.clear();
        }
        listView = (ListView) contentView.findViewById(R.id.listview);
        search= (EditText) contentView.findViewById(R.id.search);
        mAdapter = new TaskToWhoAdapter(mActivity,mDatas);
        listView.setAdapter(mAdapter);
        iv= (ImageView) contentView.findViewById(R.id.iv_huadong);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                  //  iv.setImageResource(R.drawable.celanzhankai);
            }
        });
    }
    DrawerLayout drawerLayout;
    public void getDrablelayout(DrawerLayout ddd){
        this.drawerLayout=ddd;
    }
    @Override
    protected void initListeners() {

        mAdapter.setLisener(new TaskToWhoAdapter.getIds() {
            @Override
            public void getMenmberIds(Map<Integer, UserAboutPerson.DataBean> beans, Map<Integer,UserAboutPerson.DataBean> cleanBeen, boolean isAdd) {
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
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH||actionId==KeyEvent.KEYCODE_ENTER||actionId == EditorInfo.IME_ACTION_DONE) {
                    searchData.clear();
                    String str=search.getText().toString().trim();
                    getmDataSub(str);
                    listView.setFocusable(false);
                }
                return false;
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(search.getText().toString().trim().length()==0){
                    mAdapter.dataChange(mDatas);
                    listView.setFocusable(false);
                }else {
                    getmDataSub(search.getText().toString().trim());
                }
            }
        });
//        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                iv.setImageResource(R.drawable.celanzhankai);
//
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                iv.setImageResource(R.drawable.celanshouqi);
//
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//                super.onDrawerStateChanged(newState);
//            }
//        });
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
    }
    private void initDatas1( String canshu){
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
                mDatas=channels.data;
                mAdapter.dataChange(mDatas);
                myprog.setVisibility(View.GONE);
//                App.getContext().mApbutPerson=channels.data;
//                for (int i=0;i<channels.data.size();i++){
//                    UserInfo info=new UserInfo(channels.data.get(i).id,channels.data.get(i).nickname, Uri.parse(channels.data.get(i).logo_path));
////                    RongIM.getInstance().refreshUserInfoCache(info);
//                }
            }
        }
    };
    //点击部门按键响应事件
    public void onEventMainThread(UserAboutPerson userAboutPerson){
        if(App.getContext().getLogo("logo")!=null&&App.getContext().getLogo("logo").data!=null&& SaveData.getInstance().Dept_Id!=null) {
            SaveData.getInstance().isPermissions=true;
            RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new RequestParams.DataBean(SaveData.getInstance().Dept_Id));
            initDatas1(new Gson().toJson(canshus));
            wuquanxian.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(getActivity(),"您还未登陆",Toast.LENGTH_SHORT).show();
        }

    }
    //点击部门按键响应事件无权限时
    public void onEventMainThread(UserToken userToken){
        if(App.getContext().getLogo("logo")!=null&&App.getContext().getLogo("logo").data!=null&& SaveData.getInstance().Dept_Id!=null) {
            //这里本来是没得权限得勒，为false就不能指派比自己部门高的人，就没得圆圈可以选择，下属可以指派领导做事，我是想不通的。但测试非要说不管有没得权限全部都要显示
            //所有就改为true，出了问题我不管哟
            SaveData.getInstance().isPermissions=false;
//            SaveData.getInstance().isPermissions=true;
            RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new RequestParams.DataBean(SaveData.getInstance().Dept_Id));
            initDatas1(new Gson().toJson(canshus));
            wuquanxian.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(getActivity(),"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 联系人查查找
     * @param data
     */
    private void getmDataSub(String data){
        if(mDatas!=null) {
            for (int i = 0; i < mDatas.size(); i++) {
                if (mDatas.get(i).nickname.contains(data) || mDatas.get(i).mobile.contains(data)) {
                    searchData.add(mDatas.get(i));
                }
            }
            mAdapter.dataChange(searchData);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_send:
//                if(shijiPoeples!=null&&shijiPoeples.size()>2){
//                    Toast.makeText(getActivity(),"您最多可指派两人",Toast.LENGTH_SHORT).show();
//                }else
//                if(shijiPoeples!=null&&shijiPoeples.size()>0&&shijiPoeples.size()<3&&SaveData.getInstance().type.equals("1")) {
                    if(shijiPoeples!=null&&shijiPoeples.size()>0&&SaveData.getInstance().type.equals("1")) {
                    EventBus.getDefault().post(new PublishTask());
                    EventBus.getDefault().post(new AssignTask());
                }
//                    else if(shijiPoeples!=null&&shijiPoeples.size()>0&&shijiPoeples.size()<3&&SaveData.getInstance().type.equals("2")){
                    else if(shijiPoeples!=null&&shijiPoeples.size()>0&&SaveData.getInstance().type.equals("2")){
                    mGetAssignTask();
                    EventBus.getDefault().post(new AssignTask());
                }else {
                    EventBus.getDefault().post(new AssignTask());
                }
                break;
        }
    }
    List<String> iids1 = new ArrayList<>();
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
    /**
     * 任务详情那边跳转过来的指派人
     */
    Subscription subscription1;
    private void mGetAssignTask(){
        if(App.getContext().getLogo("logo")!=null&&detailToThis.size()>0){
            showDilog("指派中...");
            maps1.clear();
            for(int i=0;i<detailToThis.size();i++){
                maps1.put(i,detailToThis.get(i));
            }
//            Log.e("ZHU","主办人"+detailToThis.get(0).name+"协办人"+detailToThis.get(1).name);
            AssignTaskBeen assignTaskBeen = new AssignTaskBeen(new AssignTaskBeen.UserBeen(App.getContext().getLogo("logo").data.id,
                    "1234567890"),new AssignTaskBeen.DataBeen(SaveData.getInstance().task_id,getMemberIds1(maps1)));
            mGetAssignTask1(new Gson().toJson(assignTaskBeen));
        }
    }

    private void mGetAssignTask1(String data) {
        subscription = Api.getMangoApi1().getAssignTask(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }
    Observer<AssignTask> observer1=new Observer<AssignTask>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription1);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(AssignTask assignTask) {
            myprog.setVisibility(View.GONE);
            if(assignTask!=null&&assignTask.status.code.equals("0")){
                Toast.makeText(getActivity(),assignTask.status.message,Toast.LENGTH_SHORT).show();;
                EventBus.getDefault().post(new AssignTask());
                MissDilog();
            }
        }
    };
}
