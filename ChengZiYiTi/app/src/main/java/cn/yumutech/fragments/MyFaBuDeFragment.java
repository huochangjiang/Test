package cn.yumutech.fragments;

import android.content.Intent;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yumutech.Adapter.MyFaBuDeAdapter;
import cn.yumutech.bean.ShowMyPublishedTask;
import cn.yumutech.bean.ShowMyPublishedTaskBeen;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import cn.yumutech.unity.ShowTaskDetailActivity;
import cn.yumutech.weight.MyListview;
import cn.yumutech.weight.SaveData;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 2016/11/21.
 */
public class MyFaBuDeFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ScrollView>{
    private View view;
    private MyListview listView;
    private int page=0;
    private int pageSize=5;
    Subscription subscription;
    private View myprog;
    private MyFaBuDeAdapter adapter;
    private PullToRefreshScrollView pullToRefresh;
    private boolean isShangla=false;
    private List<ShowMyPublishedTask.DataBean> mData=new ArrayList<>();
    public static Map<Integer,ShowMyPublishedTask.DataBean> maps=new HashMap<>();
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        view =inflater.inflate(R.layout.my_fa_bu_de_fragemnt,container,false);
        return view;
    }

    @Override
    protected void initViews(View contentView) {
        maps.clear();
        listView= (MyListview) view.findViewById(R.id.listview);
        myprog=view.findViewById(R.id.myprog);
        adapter=new MyFaBuDeAdapter(getActivity(),mData);
        listView.setAdapter(adapter);
        listView.setVisibility(View.GONE);
        myprog.setVisibility(View.VISIBLE);
        pullToRefresh = (PullToRefreshScrollView) view.findViewById(R.id.pull_to_refresh);
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefresh.setOnRefreshListener(this);
        SaveData.getInstance().isJieshou=false;

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
    List<String> iids = new ArrayList<>();
    //传入数据，将名字用，隔开
    private String getMemberIds(Map<Integer, ShowMyPublishedTask.DataBean> beans) {
        StringBuffer sb = new StringBuffer();
        iids.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            ShowMyPublishedTask.DataBean val = (ShowMyPublishedTask.DataBean) entry.getValue();
            for(int j=0;j<val.assignees.size();j++){
                iids.add(val.assignees.get(j).assignee_user_name);
            }
        }
        if(iids.size()==1){
            sb.append(iids.get(0));
        }else{
            sb.append(iids.get(0)+" "+"等"+iids.size()+"人");
        }
//        for (int i = 0; i < iids.size(); i++) {
//
//            if (i == iids.size() - 1) {
//                sb.append(iids.get(i));
//            } else {
//                sb.append(iids.get(i) + ",");
//            }
//        }
        return sb.toString();
    }
    @Override
    protected void initListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                maps.clear();
                maps.put(position,mData.get(position));
                SaveData.getInstance().isJieshou=false;
                Intent intent=new Intent();
                intent.setClass(getActivity(),ShowTaskDetailActivity.class);
                intent.putExtra("task_id",mData.get(position).task_id);
                intent.putExtra("task_poeple",getMemberIds(maps));
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initDatas() {
        if(App.getContext().getLogo("logo")!=null){
            ShowMyPublishedTaskBeen been=new ShowMyPublishedTaskBeen(new ShowMyPublishedTaskBeen.UserBean(App.getContext().getLogo("logo").data.id,
                   ""),new ShowMyPublishedTaskBeen.DataBean(page+"",pageSize+""));
            initDatas1(new Gson().toJson(been));
        }
    }

    private void initDatas1(String canshu) {
        subscription = Api.getMangoApi1().getXianShiFaBu(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<ShowMyPublishedTask> observer=new Observer<ShowMyPublishedTask>() {
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
        public void onNext(ShowMyPublishedTask showMyPublishedTask) {
            if(showMyPublishedTask!=null){
                if(isShangla){
                    mData.addAll(showMyPublishedTask.data);
                }else {
                    mData=showMyPublishedTask.data;
                }
                adapter.dataChange(mData);

                listView.setVisibility(View.VISIBLE);
                myprog.setVisibility(View.GONE);
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
            ShowMyPublishedTaskBeen been=new ShowMyPublishedTaskBeen(new ShowMyPublishedTaskBeen.UserBean(App.getContext().getLogo("logo").data.id,
                    ""),new ShowMyPublishedTaskBeen.DataBean(page+"",pageSize+""));
            initDatas1(new Gson().toJson(been));
        }
    }

    //上拉
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        page=mData.size();
        isShangla=true;
        if(App.getContext().getLogo("logo")!=null){
            ShowMyPublishedTaskBeen been=new ShowMyPublishedTaskBeen(new ShowMyPublishedTaskBeen.UserBean(App.getContext().getLogo("logo").data.id,
                    ""),new ShowMyPublishedTaskBeen.DataBean(page+"",pageSize+""));
            initDatas1(new Gson().toJson(been));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if( SaveData.getInstance().isJieshou){
            isShangla=false;
            page=0;
            if(App.getContext().getLogo("logo")!=null){
                ShowMyPublishedTaskBeen been=new ShowMyPublishedTaskBeen(new ShowMyPublishedTaskBeen.UserBean(App.getContext().getLogo("logo").data.id,
                        ""),new ShowMyPublishedTaskBeen.DataBean(page+"",pageSize+""));
                initDatas1(new Gson().toJson(been));
            }
        }

    }
}
