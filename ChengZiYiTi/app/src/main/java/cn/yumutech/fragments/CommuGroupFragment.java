package cn.yumutech.fragments;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.GroupListAdapter;
import cn.yumutech.bean.RequestCanShu1;
import cn.yumutech.bean.UserXiangGuanQun;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.QunMenmberSelectorActivity;
import cn.yumutech.unity.R;
import cn.yumutech.unity.TaShanZhiShiActivity;
import cn.yumutech.weight.MyListview;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 2016/11/13.
 */
public class CommuGroupFragment extends BaseFragment implements View.OnClickListener{
    private static CommuGroupFragment fragment;
    private View groupView;
    private MyListview listview;
    private GroupListAdapter adapter;
    private RelativeLayout two;
    private List<UserXiangGuanQun.DataBean> data=new ArrayList<>();
    Subscription subscription;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public CommuGroupFragment() {
        // Required empty public constructor
    }
    public static CommuGroupFragment newInstance() {
        if(fragment==null)
            fragment = new CommuGroupFragment();

        return fragment;
    }
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        groupView = inflater.inflate(R.layout.fragment_commu_group, container, false);
        return groupView;
    }

    @Override
    protected void initViews(View contentView) {

        listview= (MyListview) contentView.findViewById(R.id.listview);
        listview.setFocusable(false);
        adapter=new GroupListAdapter(getActivity(),data);
        listview.setAdapter(adapter);
        two= (RelativeLayout) contentView.findViewById(R.id.two);
        two.setOnClickListener(this);
        RelativeLayout  one= (RelativeLayout) contentView.findViewById(R.id.one);
        one.setOnClickListener(this);
        listview.setFocusable(false);
    }

    @Override
    protected void initListeners() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RongIM.getInstance().startGroupChat(mActivity, adapter.getItem(i).groupId, adapter.getItem(i).groupName);

            }
        });
    }

    @Override
    protected void initDatas() {

    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getUserXiangGuanQun(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.two:
                Intent intent=new Intent();
                intent.setClass(getActivity(), TaShanZhiShiActivity.class);
                startActivity(intent);
                break;
            case R.id.one:
                Intent intent1=new Intent();
                intent1.putExtra("type","create");
                intent1.setClass(getActivity(), QunMenmberSelectorActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(App.getContext().getLogo("logo")!=null) {
            RequestCanShu1 canshus = new RequestCanShu1(new RequestCanShu1.UserBean(App.getContext().getLogo("logo").data.nickname, App.getContext().getLogo("logo").data.id),
                    new RequestCanShu1.DataBean(App.getContext().getLogo("logo").data.id));
            initDatas1(new Gson().toJson(canshus));
        }
    }

    Observer<UserXiangGuanQun> observer = new Observer<UserXiangGuanQun>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(UserXiangGuanQun channels) {
            if(channels.status.code.equals("0")){
                adapter.dataChange(channels.data);
                for (int i=0;i<channels.data.size();i++) {
                    Group group = new Group(channels.data.get(i).groupId, channels.data.get(i).groupName, Uri.parse(channels.data.get(i).create_user_logo_path));
                    RongIM.getInstance().refreshGroupInfoCache(group);
                }

            }

        }
    };


}
