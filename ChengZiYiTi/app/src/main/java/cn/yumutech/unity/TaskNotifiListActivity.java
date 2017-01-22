package cn.yumutech.unity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.yumutech.bean.TaskNotifiList;
import cn.yumutech.bean.TaskNotifiListBeen;
import cn.yumutech.netUtil.Api;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/1/18.
 */
public class TaskNotifiListActivity extends BaseActivity{
    Subscription subscription;
    private ImageView back;
    private TextView text;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_task_notifi_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        back = (ImageView) findViewById(R.id.back);
        text= (TextView) findViewById(R.id.text);
    }

    @Override
    protected void initData() {
        TaskNotifiListBeen taskNotifiItemBeen = new TaskNotifiListBeen(new TaskNotifiListBeen.UserBean(App.getContext().getLogo("logo").data.id, "1234567890"),
                new TaskNotifiListBeen.DataBean("督查通报","0","5"));
        getData1(new Gson().toJson(taskNotifiItemBeen));
    }

    //获取列表
    private void getData1(String data) {
        subscription = Api.getMangoApi1().getTaskNotifiList(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<TaskNotifiList> observer=new Observer<TaskNotifiList>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(TaskNotifiList taskNotifiList) {
            String data=new Gson().toJson(taskNotifiList);
            Log.e("tongzhi",data);
            text.setText(data);
        }
    };

    @Override
    protected void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
