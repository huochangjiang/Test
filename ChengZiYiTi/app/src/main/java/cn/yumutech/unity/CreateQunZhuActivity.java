package cn.yumutech.unity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import cn.yumutech.bean.CreateQunZu;
import cn.yumutech.bean.RequestCanShu;
import cn.yumutech.netUtil.Api;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateQunZhuActivity extends BaseActivity {


    private EditText editText;
    private Button sure;
    private String ids;
    Subscription subscription;
    protected void unsubscribe( Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_qun_zhu;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Intent intent=getIntent();
        if(intent!=null){
            ids = intent.getStringExtra("id");
        }
        editText = (EditText) findViewById(R.id.phone);
        sure = (Button) findViewById(R.id.denglu);
    }

    @Override
    protected void initData() {

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestCanShu canshus=new RequestCanShu(new RequestCanShu.UserBean(App.getContext().getLogo("logo").data.id,"1234567890"),
                        new RequestCanShu.DataBean(ids+","+App.getContext().getLogo("logo").data.id,editText.getText().toString().trim()));
                initDatas1(new Gson().toJson(canshus));
            }
        });
    }

    @Override
    protected void initListeners() {

    }
    private void initDatas1( String canshu){
        subscription = Api.getMangoApi1().getCreateQunZhu(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    Observer<CreateQunZu> observer = new Observer<CreateQunZu>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }
        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }
        @Override
        public void onNext(CreateQunZu channels) {
            if(channels.status.code.equals("0")){
                Group group=new Group(channels.data.groupId,channels.data.groupName, Uri.parse(channels.data.create_user_logo_path));
                RongIM.getInstance().refreshGroupInfoCache(group);
                RongIM.getInstance().startGroupChat(CreateQunZhuActivity.this, channels.data.groupId, "标题");
                finish();
            }

        }
    };


}
