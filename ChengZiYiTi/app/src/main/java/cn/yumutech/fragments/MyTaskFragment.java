package cn.yumutech.fragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.InspectionTaskAdapter;
import cn.yumutech.bean.ShowMyTask;
import cn.yumutech.bean.ShowMyTaskBeen;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 2016/11/21.
 */
public class MyTaskFragment extends BaseFragment{
    private View contactView;
    private ListView listView;
    private int page=0;
    private int pageSize=5;
    Subscription subscription;
    private InspectionTaskAdapter adapter;
    private List<ShowMyTask.DataBean> mData=new ArrayList<>();
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        contactView=inflater.inflate(R.layout.fragment_my_task,container,false);
        return contactView;
    }

    @Override
    protected void initViews(View contentView) {
        listView= (ListView) contactView.findViewById(R.id.listview);
        adapter=new InspectionTaskAdapter(getActivity(),mData);
        listView.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initDatas() {
        if(App.getContext().getLogo("logo")!=null){
            ShowMyTaskBeen showMyTaskBeen=new ShowMyTaskBeen(new ShowMyTaskBeen.UserBean(App.getContext().getLogo("logo").data.id,"1234567890"),new ShowMyTaskBeen.DataBean(page+"",pageSize+""));
            initDatas1(new Gson().toJson(showMyTaskBeen));
        }
    }

    private void initDatas1(String canshu) {
        Log.e("canshu",canshu);
        subscription = Api.getMangoApi1().getShowMyTask(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<ShowMyTask> observer=new Observer<ShowMyTask>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(ShowMyTask showMyTask) {
            String data=new Gson().toJson(showMyTask);
            Log.e("showMyTask",data);
            if(showMyTask!=null){
                for (int i=0;i<showMyTask.data.size();i++);{
                    mData.addAll(showMyTask.data);
                }
                adapter.dataChange(mData);
            }
        }
    };
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
 }
