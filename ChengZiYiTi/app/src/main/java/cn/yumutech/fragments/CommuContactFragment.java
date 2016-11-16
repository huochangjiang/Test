package cn.yumutech.fragments;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.ExpanderAdapter;
import cn.yumutech.bean.ChindClass;
import cn.yumutech.bean.DepartList;
import cn.yumutech.bean.GroupClass;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 2016/11/13.
 */
public class CommuContactFragment extends BaseFragment{
    Subscription subscription;
    private static CommuContactFragment fragment;
    private View contactView;
    private ExpandableListView expandableListView;
    private List<GroupClass> groupsDatas=new ArrayList<>();
    private List<List<ChindClass>> chindDatas=new ArrayList<>();
    private List<ChindClass> chindClasses=new ArrayList<>();
    private List<ChindClass> chindClasses1=new ArrayList<>();


    public CommuContactFragment() {

    }
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    public static CommuContactFragment newInstance() {
        if(fragment==null)
            fragment = new CommuContactFragment();

        return fragment;
    }
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        contactView = inflater.inflate(R.layout.fragment_commu_contact, container, false);

        return contactView;
    }

    @Override
    protected void initViews(View contentView) {
        expandableListView = (ExpandableListView) contentView.findViewById(R.id.expandlistview);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_layout, DralayoutFragment.newInstance()).commitAllowingStateLoss();

    }

    @Override
    protected void initListeners() {
    }
    @Override
    protected void initDatas() {


        RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean("unity","1234567890"),
                new RequestCanShu.DataBean(null));
        initDatas1(new Gson().toJson(canshus));

        groupsDatas.add(new GroupClass("资阳市干部","20"));
        groupsDatas.add(new GroupClass("资阳市干部","20"));
        ChindClass chanData= new ChindClass("cc",R.drawable.next);
        ChindClass chanData12= new ChindClass("cc1",R.drawable.next);
        ChindClass chanData3= new ChindClass("cc2",R.drawable.next);
        ChindClass chanData4= new ChindClass("cc3",R.drawable.next);
        ChindClass chanData5= new ChindClass("cc4",R.drawable.next);
        ChindClass chanData6= new ChindClass("cc5",R.drawable.next);
        chindClasses.add(chanData);
        chindClasses.add(chanData12);
        chindClasses.add(chanData3);
        chindClasses.add(chanData4);
        chindClasses.add(chanData5);
        chindClasses.add(chanData6);
        chindClasses1.add(chanData);
        chindClasses1.add(chanData12);
        chindClasses1.add(chanData6);
        chindDatas.add(chindClasses);
        chindDatas.add(chindClasses1);
        ExpanderAdapter mAdapter=new ExpanderAdapter(mActivity,groupsDatas,chindDatas);
        expandableListView.setAdapter(mAdapter);
    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getBumenList(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    private void initDatas2(String canshu){

    }

    Observer<DepartList> observer = new Observer<DepartList>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(DepartList channels) {
            if(channels.status.code.equals("0")){
                Log.e("info",channels.data.dept_name+"aaa");
            }
        }
    };
}
