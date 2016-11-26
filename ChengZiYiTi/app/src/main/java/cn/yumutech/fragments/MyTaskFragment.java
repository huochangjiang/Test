package cn.yumutech.fragments;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.InspectionTaskAdapter;
import cn.yumutech.bean.ShowMyTask;
import cn.yumutech.bean.ShowMyTaskBeen;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import cn.yumutech.unity.ShowTaskDetailActivity;
import cn.yumutech.weight.MyListview;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 2016/11/21.
 */
public class MyTaskFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ScrollView>{
    private View contactView;
    private MyListview listView;
    private int page=0;
    private int pageSize=5;
    Subscription subscription;
    private View myprog;
    private InspectionTaskAdapter adapter;
    private PullToRefreshScrollView pullToRefresh;
    private boolean isShangla=false;

    private List<ShowMyTask.DataBean> mData=new ArrayList<>();
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        contactView=inflater.inflate(R.layout.fragment_my_task,container,false);
        return contactView;
    }

    @Override
    protected void initViews(View contentView) {
        listView= (MyListview) contactView.findViewById(R.id.listview);
        adapter=new InspectionTaskAdapter(getActivity(),mData);
        listView.setAdapter(adapter);
        myprog=contentView.findViewById(R.id.myprog);

        myprog.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        pullToRefresh = (PullToRefreshScrollView) contactView.findViewById(R.id.pull_to_refresh);
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefresh.setOnRefreshListener(this);

        //下拉刷新设置
        ILoadingLayout startLabels = pullToRefresh
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        //上拉加载更多设置
        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
    }

    @Override
    protected void initListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),ShowTaskDetailActivity.class);
                intent.putExtra("task_id",mData.get(position).task_id);
                startActivity(intent);
            }
        });
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
            pullToRefresh.onRefreshComplete();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            pullToRefresh.onRefreshComplete();
        }

        @Override
        public void onNext(ShowMyTask showMyTask) {
            String data=new Gson().toJson(showMyTask);
            Log.e("showMyTask",data);
            if(showMyTask!=null){
                for (int i=0;i<showMyTask.data.size();i++);{
                    if(isShangla){
                        mData.addAll(showMyTask.data);
                    }else {
                        mData=showMyTask.data;
                    }
                }
                adapter.dataChange(mData);
                myprog.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                pullToRefresh.onRefreshComplete();
            }
        }
    };
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    //下拉
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        isShangla=false;
        page=0;
        if(App.getContext().getLogo("logo")!=null){
            ShowMyTaskBeen showMyTaskBeen=new ShowMyTaskBeen(new ShowMyTaskBeen.UserBean(App.getContext().getLogo("logo").data.id,"1234567890"),new ShowMyTaskBeen.DataBean(page+"",pageSize+""));
            initDatas1(new Gson().toJson(showMyTaskBeen));
        }
    }

    //上拉
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        page=mData.size();
        isShangla=true;
        if(App.getContext().getLogo("logo")!=null){
            ShowMyTaskBeen showMyTaskBeen=new ShowMyTaskBeen(new ShowMyTaskBeen.UserBean(App.getContext().getLogo("logo").data.id,"1234567890"),new ShowMyTaskBeen.DataBean(page+"",pageSize+""));
            initDatas1(new Gson().toJson(showMyTaskBeen));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isShangla=false;
        page=0;
        if(App.getContext().getLogo("logo")!=null){
            ShowMyTaskBeen showMyTaskBeen=new ShowMyTaskBeen(new ShowMyTaskBeen.UserBean(App.getContext().getLogo("logo").data.id,"1234567890"),new ShowMyTaskBeen.DataBean(page+"",pageSize+""));
            initDatas1(new Gson().toJson(showMyTaskBeen));
        }
    }
}
