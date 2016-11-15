package cn.yumutech.unity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.yumutech.Adapter.TaShanCommentAdapter;
import cn.yumutech.bean.AddPingLun;
import cn.yumutech.bean.AddPingLunBeen;
import cn.yumutech.bean.ExchangeCommenList;
import cn.yumutech.bean.ExchangeCommenListBeen;
import cn.yumutech.netUtil.Api;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 2016/11/13.
 */
public class TaShanCommentsActivity extends BaseActivity{
    private ImageView back;
    private Button button;
    private ListView comments_list;
    Subscription subscription;
    Subscription subscription1;
    private TaShanCommentAdapter adapter;
    private ExchangeCommenList mData;
    private RelativeLayout shurukuang;
    private EditText edit;
    private TextView send;
    private App app;
    private String mId,userId;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_tashan_comments;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app = (App) TaShanCommentsActivity.this.getApplicationContext();
        getExtra();
        back= (ImageView) findViewById(R.id.back);
        button= (Button) findViewById(R.id.button);
        shurukuang= (RelativeLayout) findViewById(R.id.shurukuang);
        comments_list= (ListView) findViewById(R.id.comments_list);
        edit= (EditText) findViewById(R.id.edit);
        send= (TextView) findViewById(R.id.send);
        adapter=new TaShanCommentAdapter(TaShanCommentsActivity.this,mData);
        comments_list.setAdapter(adapter);
        button.setFocusable(true);
    }

    @Override
    protected void initData() {
        getData();
    }

    @Override
    protected void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.requestFocus();
                edit.setFocusable(true);
                button.setVisibility(View.GONE);
                shurukuang.setVisibility(View.VISIBLE);
                InputMethodManager inputManager =
                        (InputMethodManager)button.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit.getText().toString()!=null){
                    addPinglun();
                }
            }
        });
    }
    /**
     * 获取他山之石评论列表
     */
    private void getData(){
        ExchangeCommenListBeen exchangeCommenList=new ExchangeCommenListBeen(new ExchangeCommenListBeen.UserBean("1","1234567890"),
                new ExchangeCommenListBeen.DataBean(mId,"0","5"));
        getData1(new Gson().toJson(exchangeCommenList));
    }
    private void getData1(String list){
        subscription = Api.getMangoApi1().getExchangeCommenList(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<ExchangeCommenList> observer=new Observer<ExchangeCommenList>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(ExchangeCommenList exchangeCommenList) {
            if(exchangeCommenList!=null&&exchangeCommenList.status.code.equals("0")){
                mData=exchangeCommenList;
                adapter.dataChange(exchangeCommenList);
            }
        }
    };
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    /**
     * 添加他山之石评论
     */
    private void addPinglun(){
        if(mId!=null&&userId!=null){
            AddPingLunBeen addPingLunBeen=new AddPingLunBeen(new AddPingLunBeen.userBeen("1","1234567890"),
                    new AddPingLunBeen.dataBeen(mId,edit.getText().toString(),userId,"2"));
            addPinglun1(new Gson().toJson(addPingLunBeen));
        }

    }
    private void addPinglun1(String data){
        subscription1 = Api.getMangoApi1().getAddPingLun(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }
    Observer<AddPingLun> observer1=new Observer<AddPingLun>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(AddPingLun addPingLun) {
            if(addPingLun.status.code.equals("0")){
//                mData.data.addAll(addPingLun.data);
//                adapter.dataChange(mData);
                getData();
                button.setVisibility(View.VISIBLE);
                shurukuang.setVisibility(View.GONE);
            }else if(addPingLun.status.code.equals("-9")){
                Toast.makeText(TaShanCommentsActivity.this,addPingLun.status.message,Toast.LENGTH_SHORT).show();
            }
        }
    };
    /**
     * 获取传入的数据
     */
    private void getExtra(){
        if(getIntent()!=null){
            mId=getIntent().getStringExtra("id");
        }
        if(app.getLogo("logo")!=null){
            userId=app.getLogo("logo").data.id;
        }
    }
}
