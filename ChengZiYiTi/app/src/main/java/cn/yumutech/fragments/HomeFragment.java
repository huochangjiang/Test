package cn.yumutech.fragments;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.BaiBaoAdatper;
import cn.yumutech.bean.BaiBao;
import cn.yumutech.bean.LeaderActivitys;
import cn.yumutech.bean.RequestCanShuNew;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.CommunicationActivity;
import cn.yumutech.unity.FriendsUrlActivity;
import cn.yumutech.unity.InspectionTaskNewActivity;
import cn.yumutech.unity.InspectionTaskNewOtherActivity;
import cn.yumutech.unity.LeaderActivitysActivity;
import cn.yumutech.unity.PolicyFileActivity;
import cn.yumutech.unity.ProjectMangerActivity;
import cn.yumutech.unity.R;
import cn.yumutech.unity.WorkDongTaiActivity;
import cn.yumutech.weight.ImagePagerAdapterApply;
import cn.yumutech.weight.MyGridView;
import cn.yumutech.weight.StringUtils1;
import cn.yumutech.weight.ViewFlow;
import cn.yumutech.weight.getContact;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragment extends BaseFragment {
    private View rootView;
    private static HomeFragment fragment;
    private ViewFlow mViewFlow;
    private LinearLayout ll_dian;
    private ScrollView scrollView;
    private MyGridView myGridView;
    private App app;
    private View net_connect;
    List<BaiBao> baibaos=new ArrayList<BaiBao>();
    LeaderActivitys leaderActivitys;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        if(fragment==null)
        fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    protected void initViews(View contentView) {
        app=App.getContext();
        mViewFlow = (ViewFlow) contentView.findViewById(R.id.viewpager);
        ll_dian = (LinearLayout) contentView.findViewById(R.id.ll_dian);
        scrollView = (ScrollView) contentView.findViewById(R.id.scrollview);
        myGridView = (MyGridView) contentView.findViewById(R.id.gridView);
        baibaos.add(new BaiBao("时政新闻", R.drawable.lingdaohuodong));
        baibaos.add(new BaiBao("动态消息", R.drawable.gongzuozhuangtai));
        baibaos.add(new BaiBao("重点项目", R.drawable.xiangmugongzuo));
        baibaos.add(new BaiBao("政策文件", R.drawable.zhengciwenjian));
        baibaos.add(new BaiBao("督查督办", R.drawable.duchaduban));
        baibaos.add(new BaiBao("友情链接", R.drawable.lianjie));
        BaiBaoAdatper baiBaoAdatper=new BaiBaoAdatper(getActivity(), (ArrayList<BaiBao>) baibaos);
        myGridView.setAdapter(baiBaoAdatper);
        myGridView.setFocusable(false);
        mViewFlow.setLiserner(new ViewFlow.postion() {
            @Override
            public void postion(int postin) {
                // TODO Auto-generated method stub
                Log.e("info",postin+"---");
                if(ll_dian.getChildCount()>2) {
                    for (int i = 0; i < ll_dian.getChildCount(); i++) {
                        if (postin % ll_dian.getChildCount() == i) {
                            ImageView childAt = (ImageView) ll_dian.getChildAt(i);
                            childAt.setImageResource(R.drawable.baidian);
                        } else {
                            ImageView childAt = (ImageView) ll_dian.getChildAt(i);
                            childAt.setImageResource(R.drawable.hongdian);
                        }
                    }
                }
            }
        });
        net_connect = contentView.findViewById(R.id.netconnect);
        initDatas();
//        initLocal();
       //正常登录
        getContact.getInstance().getData();
    }
    //加载缓存
    private void initLocal() {
        String readHomeJson = app.readHomeJson("LeaderActivitys");// 首页内容
        if (!StringUtils1.isEmpty(readHomeJson)) {
            LeaderActivitys data = new Gson().fromJson(readHomeJson, LeaderActivitys.class);
            loadHome(data);
        }else{
            if(!app.isNetworkConnected(getActivity())){
                scrollView.setVisibility(View.GONE);
                net_connect.setVisibility(View.VISIBLE);
            }
        }
        if (app.isNetworkConnected(getActivity())) {
            initDatas();
        }
    }
    /**
     * 加载列表数据
     */
    private void loadHome(LeaderActivitys channels){
        leaderActivitys=channels;
        initBanner(channels);
    }
    @Override
    protected void initListeners() {
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Intent intent=new Intent(getActivity(), LeaderActivitysActivity.class);
                    startActivity(intent);
                }else if(i==1){
                    Intent intent=new Intent(getActivity(), WorkDongTaiActivity.class);
                    startActivity(intent);
                }else if(i==5){
                    Intent intent=new Intent(getActivity(), FriendsUrlActivity.class);
                    startActivity(intent);
                }else if(i==6){
                    Intent intent=new Intent(getActivity(), ProjectMangerActivity.class);
                    startActivity(intent);
                }else if(i==4){
                    if(App.getContext().getLogo("logo").data.publish_task_flag.equals("1")){
                        Intent intent=new Intent(getActivity(), InspectionTaskNewActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent=new Intent(getActivity(), InspectionTaskNewOtherActivity.class);
                        startActivity(intent);
                    }
                }else if(i==7){
                    Intent intent=new Intent(getActivity(), CommunicationActivity.class);
                    startActivity(intent);
                }else if(i==8){
                    Intent intent=new Intent(getActivity(), FriendsUrlActivity.class);
                    startActivity(intent);
                }else if(i==2){
                    Intent intent=new Intent(getActivity(), ProjectMangerActivity.class);
                    startActivity(intent);
                }else if(i==3){
                    Intent intent=new Intent(getActivity(), PolicyFileActivity.class);
                    startActivity(intent);
                }
            }
        });
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.isNetworkConnected(getActivity())){
                    net_connect.setVisibility(View.GONE);
                    initDatas();
                }
            }
        });
    }
Subscription subscription;
    @Override
    protected void initDatas() {
        if(App.getContext().getLogo("logo")!=null) {
            RequestCanShuNew canshus = new RequestCanShuNew(new RequestCanShuNew.UserBean(App.getContext().getLogo("logo").data.id,App.getContext().getLogo("logo").data.nickname),
                    new RequestCanShuNew.DataBean("0", "5"));
            initDatas1(new Gson().toJson(canshus));
        }else {
            App.getContext().noLogin(getActivity());
        }
    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getHomeLunBo(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    /**
     * 动态创建
     */
    public void createImageView(int i){
        ImageView iv=new ImageView(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(layoutParams);
        layoutParams.leftMargin=10;
        if(i==0) {
            iv.setImageResource(R.drawable.baidian);
        }else{
            iv.setImageResource(R.drawable.hongdian);
        }
        ll_dian.addView(iv);
    }

    private void initBanner(LeaderActivitys film) {
            ll_dian.removeAllViews();
            for (int i = 0; i < film.data.size(); i++) {
                createImageView(i);
            }
        mViewFlow.setAdapter(new ImagePagerAdapterApply(getActivity(), film,
                scrollView).setInfiniteLoop(true));
        mViewFlow.setmSideBuffer(film.data.size()); // 实际图片张数，
        mViewFlow.setTimeSpan(3000);
        mViewFlow.setSelection(film.data.size() * 1000); // 设置初始位置
        mViewFlow.startAutoFlowTimer();
    }


    Observer<LeaderActivitys> observer = new Observer<LeaderActivitys>() {
        @Override
        public void onCompleted() {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
        @Override
        public void onNext(LeaderActivitys channels) {
            if(channels.status.code.equals("0")) {
                if (channels.data != null && channels.data.size() > 0) {
//                    app.savaHomeJson("LeaderActivitys", new Gson().toJson(channels));
                    loadHome(channels);
                }
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        baibaos.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        initDatas();
    }
}
