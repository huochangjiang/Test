package cn.yumutech.unity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

import cn.yumutech.Adapter.TaShanCommentAdapter;
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
    private TaShanCommentAdapter adapter;
    private ExchangeCommenList mData;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_tashan_comments;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        back= (ImageView) findViewById(R.id.back);
        button= (Button) findViewById(R.id.button);
        comments_list= (ListView) findViewById(R.id.comments_list);
        adapter=new TaShanCommentAdapter(TaShanCommentsActivity.this,mData);
        comments_list.setAdapter(adapter);
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
    }
    /**
     * 获取他山之石评论列表
     */
    private void getData(){
        ExchangeCommenListBeen exchangeCommenList=new ExchangeCommenListBeen(new ExchangeCommenListBeen.UserBean("1","1234567890"),
                new ExchangeCommenListBeen.DataBean("1","0","5"));
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
}
