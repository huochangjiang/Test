package cn.yumutech.unity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import cn.yumutech.Adapter.ReplyToCommentAdapter;
import cn.yumutech.bean.ExchangeCommenList;
import cn.yumutech.bean.ExchangeCommenListBeen;
import cn.yumutech.netUtil.Api;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 2016/11/14.
 */
public class ReplyToCommentActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ListView>{
    private ImageView back;
    private ReplyToCommentAdapter adapter;
    private ListView listView;
    private List<ExchangeCommenList.data> mData;
    private int page=0;
    Subscription subscription;
    private PullToRefreshListView pullToRefresh;
    private String commentId;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_reply_to_comment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initExtra();
        back= (ImageView) findViewById(R.id.back);
        listView= (ListView) findViewById(R.id.reply);
        adapter=new ReplyToCommentAdapter(ReplyToCommentActivity.this,mData);
        listView.setAdapter(adapter);
        pullToRefresh = (PullToRefreshListView) findViewById(R.id.pull_to_refresh);
        pullToRefresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        if(commentId!=null&&commentId.length()!=0){
            getData();
        }

    }

    @Override
    protected void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //获取回复列表
    private ExchangeCommenListBeen exchangeCommenList;
    private void getData(){
        exchangeCommenList=new ExchangeCommenListBeen(new ExchangeCommenListBeen.UserBean("1","1234567890"),
                new ExchangeCommenListBeen.DataBean(commentId,"0","10"));
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
            pullToRefresh.onRefreshComplete();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            pullToRefresh.onRefreshComplete();
        }

        @Override
        public void onNext(ExchangeCommenList exchangeCommenList) {
            if(exchangeCommenList!=null&&exchangeCommenList.status.code.equals("0")){
                if(isShangla){
                    mData.addAll(exchangeCommenList.data);
                }else {
                    mData = exchangeCommenList.data;
                }
                adapter.dataChange(mData);
            }
            pullToRefresh.onRefreshComplete();
        }
    };
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    private boolean isShangla=false;
    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        isShangla=false;
        mData.clear();
        exchangeCommenList=new ExchangeCommenListBeen(new ExchangeCommenListBeen.UserBean("1","1234567890"),
                new ExchangeCommenListBeen.DataBean(commentId,"0","10"));
        getData1(new Gson().toJson(exchangeCommenList));
    }
    //上拉加载更多
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        page++;
        isShangla=true;
        exchangeCommenList=new ExchangeCommenListBeen(new ExchangeCommenListBeen.UserBean("1","1234567890"),
                new ExchangeCommenListBeen.DataBean(commentId,page+"","10"));
        getData1(new Gson().toJson(exchangeCommenList));
    }
    //获取传入的数据
    private void initExtra(){
        if (getIntent()!=null){
            commentId=getIntent().getStringExtra("commentId");
        }
    }
}
