package cn.yumutech.fragments;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yumutech.Adapter.MyMenmberAdapter;
import cn.yumutech.bean.BaiBao;
import cn.yumutech.bean.CreateQunZu;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.bean.RequestParams;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import cn.yumutech.weight.SaveData;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CreatQunZhuFragment extends BaseFragment {
    private boolean isBaoHan=false;
    private ListView listView;
    private List<UserAboutPerson.DataBean> mDatas = new ArrayList<>();
    private List<UserAboutPerson.DataBean> mDatas1 = new ArrayList<>();
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

    @Override
    protected void initViews(View contentView) {
        EventBus.getDefault().register(this);
        mDatas1= App.getContext().mApbutPerson;
        for (int k=0;k<mDatas1.size();k++){
            UserAboutPerson.DataBean bean=mDatas1.get(k);
            bean.type= UserAboutPerson.DataBean.TYPE_NOCHECKED;
        }

        editText = (EditText) contentView.findViewById(R.id.et);
        button = (Button) contentView.findViewById(R.id.denglu);
        listView = (ListView) contentView.findViewById(R.id.listview);

        mAdapter = new MyMenmberAdapter(mDatas1, getActivity());
        listView.setAdapter(mAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mIds!=null&&!(mIds.equals(""))){
                    if(editText.getText().toString().trim()!=null&&!editText.getText().toString().trim().equals("")) {
                        RequestCanShu canshus = new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                                new RequestCanShu.DataBean(App.getContext().getLogo("logo").data.id + "," + mIds, editText.getText().toString().trim()));
                        initDatas4(new Gson().toJson(canshus));
                    }else{
                        Toast.makeText(getActivity(), "请输入讨论组名字", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getActivity(), "请添加群成员", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    @Override
    protected void initListeners() {

    }

    @Override
    protected void initDatas() {
        mAdapter.setLisener(new MyMenmberAdapter.getIds() {
            @Override
            public void getMenmberIds(Map<Integer, UserAboutPerson.DataBean> beans) {
                mIds = getMemberIds(beans);
            }
        });
    }
    //創建qunzhu


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

    //评论回复按键响应事件
    public void onEventMainThread(BaiBao userAboutPerson){
        if(App.getContext().getLogo("logo")!=null&&App.getContext().getLogo("logo").data!=null&& SaveData.getInstance().createDetpt_id!=null) {
            RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new RequestParams.DataBean(SaveData.getInstance().createDetpt_id));
            initDatas1(new Gson().toJson(canshus));
        }else {
            Toast.makeText(getActivity(),"您还未登陆",Toast.LENGTH_SHORT).show();
        }
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
                RongIM.getInstance().startGroupChat(getActivity(), channels.data.groupId, channels.data.groupName);
                getActivity().finish();
            }

        }
    };



    private void initDatas1( String canshu){
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
                Log.e("info","a");
                mAdapter.dataChange(channels.data);
                mDatas=channels.data;
                App.getContext().mApbutPerson=channels.data;
                for (int i=0;i<channels.data.size();i++){
                    UserInfo info=new UserInfo(channels.data.get(i).id,channels.data.get(i).nickname, Uri.parse(channels.data.get(i).logo_path));
                    RongIM.getInstance().refreshUserInfoCache(info);
                }
            }
        }
    };
}
