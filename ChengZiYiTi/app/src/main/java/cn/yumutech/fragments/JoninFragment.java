package cn.yumutech.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yumutech.Adapter.MyJoinMenberAdpater;
import cn.yumutech.bean.AddPingLun;
import cn.yumutech.bean.JoinQun;
import cn.yumutech.bean.RequestParams;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import cn.yumutech.unity.SelectorJoinActivity;
import cn.yumutech.weight.SaveData;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class JoninFragment extends BaseFragment {
    private View view;
    private static JoninFragment fragment;
    private MyJoinMenberAdpater mAdapter;

    // TODO: Rename and change types of parameters
    public JoninFragment() {
        // Required empty public constructor
    }
    public static JoninFragment newInstance() {
        if(fragment==null) {
            fragment = new JoninFragment();
        }
        return fragment;
    }
    private boolean isBaoHan=false;
    private List<UserAboutPerson.DataBean> mDatas1 = new ArrayList<>();
    String mIds;
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {


        view = inflater.inflate(R.layout.fragment_jonin, container, false);

        return view;
    }
    @Override
    protected void initViews(View view) {
        EventBus.getDefault().register(this);
        for (int i = 0; i< App.getContext().mApbutPerson.size(); i++){
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

        Button button = (Button) view.findViewById(R.id.denglu);
        ListView listView = (ListView)  view.findViewById(R.id.listview);

        mAdapter = new MyJoinMenberAdpater(mDatas1, getActivity());
        listView.setAdapter(mAdapter);
        mAdapter.setLisener(new MyJoinMenberAdpater.getIds() {
            @Override
            public void getMenmberIds(Map<Integer, UserAboutPerson.DataBean> beans) {
                mIds=getMemberIds(beans);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIds!=null&&!(mIds.equals(""))){
                    RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                            new RequestParams.DataBean(mIds, ((SelectorJoinActivity) getActivity()).groupId,( (SelectorJoinActivity) getActivity()).groupName));
                    initDatas1(new Gson().toJson(canshus));
                }else{
                    Toast.makeText(getActivity(), "请添加成员", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    @Override
    protected void initListeners() {
    }
    @Override
    protected void initDatas() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    List<String> iids = new ArrayList<>();
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getJoinQun(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    private String getMemberIds(Map<Integer, UserAboutPerson.DataBean> beans) {
        StringBuffer sb = new StringBuffer();
        iids.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            String value= (String) entry.getValue();
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
    Subscription subscription;
    Subscription subscription5;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
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
               getActivity(). finish();
            }

        }
    };


    public void onEventMainThread(AddPingLun userAboutPerson){
        if(App.getContext().getLogo("logo")!=null&&App.getContext().getLogo("logo").data!=null&& SaveData.getInstance().qunMenmberId!=null) {
            RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new RequestParams.DataBean( SaveData.getInstance().qunMenmberId));
            initDatas2(new Gson().toJson(canshus));
        }else {
            Toast.makeText(getActivity(),"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }


    private void initDatas2( String canshu){
        subscription5 = Api.getMangoApi1().getUserAboutPerson(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer5);

    }
    Observer<UserAboutPerson> observer5 = new Observer<UserAboutPerson>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription5);
        }
        @Override
        public void onError(Throwable e) {
            unsubscribe(subscription5);
            e.printStackTrace();

        }
        @Override
        public void onNext(UserAboutPerson channels) {
            if(channels.status.code.equals("0")){
                mAdapter.dataChange(channels.data);
                for (int i = 0; i< channels.data.size(); i++){
                    isBaoHan=false;
                    for (int j=0;j<App.getContext().qunMember.size();j++){
                        if ((channels.data.get(i).id.equals(App.getContext().qunMember.get(j).userId))) {
                            isBaoHan = true;
                        }
                        if(j==App.getContext().qunMember.size()-1) {
                            if (!isBaoHan) {
                                mDatas1.add(channels.data.get(i));
                            }
                        }
                    }
                }
            }
        }
    };
}
