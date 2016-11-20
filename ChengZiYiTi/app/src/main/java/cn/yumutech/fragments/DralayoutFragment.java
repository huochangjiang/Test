package cn.yumutech.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.ConstancAdapter;
import cn.yumutech.bean.RequestParams;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import io.rong.imkit.RongIM;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DralayoutFragment extends BaseFragment {
    private View rootView;
    private List<UserAboutPerson.DataBean> mDatas=new ArrayList<>();
    private ListView listView;
    private ConstancAdapter mAdapter;

    Subscription subscription;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    public DralayoutFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static DralayoutFragment newInstance() {
        DralayoutFragment fragment = new DralayoutFragment();
        return fragment;
    }


    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_dralayout, container, false);
        return rootView;
    }

    @Override
    protected void initViews(View contentView) {
        listView = (ListView) contentView.findViewById(R.id.listview);
        mAdapter = new ConstancAdapter(mActivity,mDatas);
        listView.setAdapter(mAdapter);
    }
    @Override
    protected void initListeners() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(RongIM.getInstance()!=null){
                    RongIM.getInstance().startPrivateChat(getActivity(), mAdapter.getItem(position).id, mAdapter.getItem(position).nickname);
                }
            }
        });
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
                mAdapter.dataChange(channels.data);
                App.getContext().mApbutPerson=channels.data;
            }

        }
    };
}
