package cn.yumutech.fragments;

import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.ConstancAdapter;
import cn.yumutech.bean.RequestParams;
import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.bean.UserToken;
import cn.yumutech.netUtil.Api;
import cn.yumutech.unity.App;
import cn.yumutech.unity.BaseFragment;
import cn.yumutech.unity.R;
import cn.yumutech.weight.SaveData;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DralayoutFragment extends BaseFragment {
    private View rootView;
    private List<UserAboutPerson.DataBean> mDatas=new ArrayList<>();
    private ListView listView;
    private ConstancAdapter mAdapter;
    private EditText search;
    public List<UserAboutPerson.DataBean> searchData=new ArrayList<>();
    private View myprog;

    Subscription subscription;
    public boolean isAllPerson=false;
    private static DralayoutFragment fragment;
    private View wuquanxian;
    //区分自己部门的人和所有部门的人


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
        if(fragment==null) {
            fragment = new DralayoutFragment();
        }
        return fragment;
    }

    DrawerLayout drawerLayout;
    public void getDrablelayout(DrawerLayout ddd){
        this.drawerLayout=ddd;
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_dralayout, container, false);
        return rootView;
    }

    @Override
    protected void initViews(View contentView) {
        EventBus.getDefault().register(this);
        SaveData.getInstance().isContactPermissions=true;
        wuquanxian=contentView.findViewById(R.id.wuquanxian);
        listView = (ListView) contentView.findViewById(R.id.listview);
        search= (EditText) contentView.findViewById(R.id.search);
        myprog= contentView.findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        mDatas.clear();
        mAdapter = new ConstancAdapter(mActivity,mDatas);
        listView.setAdapter(mAdapter);
        ImageView iv= (ImageView) contentView.findViewById(R.id.iv_huadong);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
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
                    myprog.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    mAdapter.dataChange(mDatas);
                    listView.setFocusable(false);
                }else {
                    getmDataSub(search.getText().toString().trim());
                }
            }
        });
    }

    @Override
    protected void initDatas() {
        if(App.getContext().getLogo("logo")!=null&&App.getContext().getLogo("logo").data!=null&&App.getContext().getLogo("logo").data.dept_id!=null) {
            RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new RequestParams.DataBean("0"));
            SaveData.getInstance().isContactPermissions=false;
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
                myprog.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                mAdapter.dataChange(channels.data);
                mDatas=channels.data;
                for (int i=0;i<channels.data.size();i++){
                    UserInfo info=new UserInfo(channels.data.get(i).id,channels.data.get(i).nickname, Uri.parse(channels.data.get(i).logo_path));
                    RongIM.getInstance().refreshUserInfoCache(info);
                }

                    for (int i=0;i<channels.data.size();i++){
                        if(channels.data.get(i).id.equals(App.getContext().getLogo("logo").data.id)){
                            channels.data.remove(i);
                        }
                }

            }
        }
    };
    //点击部门按键响应事件
    public void onEventMainThread(UserAboutPerson userAboutPerson){
        if(App.getContext().getLogo("logo")!=null&&App.getContext().getLogo("logo").data!=null&&SaveData.getInstance().Dept_Id!=null) {
            SaveData.getInstance().isContactPermissions=true;
            RequestParams canshus = new RequestParams(new RequestParams.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                    new RequestParams.DataBean(SaveData.getInstance().Dept_Id));
            initDatas1(new Gson().toJson(canshus));
            wuquanxian.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(getActivity(),"您还未登陆",Toast.LENGTH_SHORT).show();
        }
    }
    //点击部门按键响应事件
    public void onEventMainThread(UserToken userToken){
        if(App.getContext().getLogo("logo")!=null&&App.getContext().getLogo("logo").data!=null&&SaveData.getInstance().Dept_Id!=null) {
            SaveData.getInstance().isContactPermissions=false;
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
}
