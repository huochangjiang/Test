package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

import cn.yumutech.Adapter.TaShanZhiShiAdapter;
import cn.yumutech.bean.ExchangeListBeen;
import cn.yumutech.bean.HuDongJIaoLiu;
import cn.yumutech.netUtil.Api;
import cn.yumutech.weight.StringUtils1;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 2016/11/13.
 */
public class TaShanZhiShiActivity extends BaseActivity {
    private ImageView back;
    private ListView listview;
    Subscription subscription;
    private TaShanZhiShiAdapter adapter;
    private HuDongJIaoLiu mData;
    private App app;
    private View net_connect;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_tashanzhishi;

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        app= (App) TaShanZhiShiActivity.this.getApplicationContext();
        back = (ImageView) findViewById(R.id.back);
        listview = (ListView) findViewById(R.id.listview);
        adapter=new TaShanZhiShiAdapter(TaShanZhiShiActivity.this,mData);
        listview.setAdapter(adapter);
        net_connect = findViewById(R.id.netconnect);
        initLocal();

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
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setClass(TaShanZhiShiActivity.this,TaShanDetailActivity.class);
                if(mData!=null&&mData.data!=null&&mData.data.get(i).id!=null){
                    intent.putExtra("id",mData.data.get(i).id);
                }
                startActivity(intent);
            }
        });
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.isNetworkConnected(TaShanZhiShiActivity.this)){
                    net_connect.setVisibility(View.GONE);
                    initData();
                }
            }
        });
    }

    /**
     * 获取他山之石列表数据
     */
    public void getData() {
        ExchangeListBeen exchangeItemBeen = new ExchangeListBeen(new ExchangeListBeen.UserBean("unity", "1234567890"),
                new ExchangeListBeen.DataBean("国内","0","5"));
        getData1(new Gson().toJson(exchangeItemBeen));
    }

    public void getData1(String data) {
        subscription = Api.getMangoApi1().getHuDongJiaoLiu(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<HuDongJIaoLiu> observer = new Observer<HuDongJIaoLiu>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }

        @Override
        public void onNext(HuDongJIaoLiu huDongItem) {
            if(huDongItem!=null&&huDongItem.status.code.equals("0")){
                mData=huDongItem;
                app.savaHomeJson("tslist",new Gson().toJson(huDongItem));
               loadHome(mData);
            }
        }
    };

    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    /**
     * 加载列表数据
     */
    private void loadHome(HuDongJIaoLiu data){
        adapter.dataChange(data);
        net_connect.setVisibility(View.GONE);
        listview.setVisibility(View.VISIBLE);
    }
    private void initLocal() {
        String readHomeJson = app.readHomeJson("tslist");// 首页内容
        if (!StringUtils1.isEmpty(readHomeJson)) {
            HuDongJIaoLiu data = new Gson().fromJson(readHomeJson, HuDongJIaoLiu.class);
            loadHome(data);

        }else{
            if(!app.isNetworkConnected(this)){
                net_connect.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
            }
        }
        if (app.isNetworkConnected(TaShanZhiShiActivity.this)) {
            initData();
        }
    }
}
