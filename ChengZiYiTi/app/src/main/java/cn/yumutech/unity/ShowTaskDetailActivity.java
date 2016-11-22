package cn.yumutech.unity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.yumutech.bean.AcceptTask;
import cn.yumutech.bean.AcceptTaskBeen;
import cn.yumutech.bean.ShowTaskDetail;
import cn.yumutech.bean.ShowTaskDetailBeen;
import cn.yumutech.netUtil.Api;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/22.
 */
public class ShowTaskDetailActivity extends BaseActivity{
    private String task_id;
    private TextView status,date,neirong;
    Subscription subscription;
    Subscription subscription1;
    private TextView title;
    private RelativeLayout accept;
    private RelativeLayout complete;
    private RelativeLayout wanchen;
    private ImageView back;
    private View myprog;
    private RelativeLayout all;
    private ShowTaskDetail mData;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_task_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initExtra();
        title= (TextView) findViewById(R.id.title);
        status= (TextView) findViewById(R.id.status);
        date= (TextView) findViewById(R.id.date);
        neirong= (TextView) findViewById(R.id.neirong);
        accept= (RelativeLayout) findViewById(R.id.accept);
        complete= (RelativeLayout) findViewById(R.id.complete);
        wanchen= (RelativeLayout) findViewById(R.id.wanchen);
        back= (ImageView) findViewById(R.id.back);
        myprog=findViewById(R.id.myprog);
        all= (RelativeLayout) findViewById(R.id.all);
        myprog.setVisibility(View.VISIBLE);
        all.setVisibility(View.GONE);
    }

    private void initExtra() {
        if(getIntent()!=null){
            task_id=getIntent().getStringExtra("task_id");
        }
    }

    @Override
    protected void initData() {
        if(App.getContext().getLogo("logo")!=null&&task_id!=null){
            ShowTaskDetailBeen been=new ShowTaskDetailBeen(new ShowTaskDetailBeen.UserBean(App.getContext().getLogo("logo").data.id,""),
                    new ShowTaskDetailBeen.DataBean(task_id));
            initData1(new Gson().toJson(been));
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
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mData!=null&&mData.data!=null&&mData.data.task_id!=null){
                    initAcceptTask(mData.data.task_id);
                }

            }
        });
    }
    //接受任务方法
    private void initAcceptTask(String id){
        if(App.getContext().getLogo("logo")!=null){
            AcceptTaskBeen been=new AcceptTaskBeen(new AcceptTaskBeen.UserBean(App.getContext().getLogo("logo").data.id,""),
                    new AcceptTaskBeen.DataBean(id));
            initAcceptTask1(new Gson().toJson(been));
        }
    }
    private void initAcceptTask1(String canshu){
        subscription1 = Api.getMangoApi1().getAcceptTask(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }
    Observer<AcceptTask> observer1=new Observer<AcceptTask>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(AcceptTask acceptTask) {
            if(acceptTask.status.code.equals("0")&&acceptTask.data!=null){
                Toast.makeText(ShowTaskDetailActivity.this,"您已接受任务",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    //获取任务详情信息
    private void initData1(String canshu) {
        subscription = Api.getMangoApi1().getShowTaskDetail(canshu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<ShowTaskDetail> observer=new Observer<ShowTaskDetail>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(ShowTaskDetail showTaskDetail) {
            String data=new Gson().toJson(showTaskDetail);
            Log.e("getShowTaskDetail",data);
            if(showTaskDetail.status.code.equals("0")&&showTaskDetail!=null){
                mData=showTaskDetail;
                if(showTaskDetail.data.task_status_name.equals("待接受")){
                    accept.setVisibility(View.VISIBLE);
                    complete.setVisibility(View.GONE);
                    wanchen.setVisibility(View.GONE);
                }else if(showTaskDetail.data.task_status_name.equals("已接受")){
                    accept.setVisibility(View.GONE);
                    complete.setVisibility(View.VISIBLE);
                    wanchen.setVisibility(View.GONE);
                }else if(showTaskDetail.data.task_status_name.equals("已完成")){
                    accept.setVisibility(View.GONE);
                    complete.setVisibility(View.GONE);
                    wanchen.setVisibility(View.VISIBLE);
                }
                myprog.setVisibility(View.GONE);
                all.setVisibility(View.VISIBLE);
                setData(showTaskDetail);
            }


        }
    };
  private void setData(ShowTaskDetail data){
      title.setText(data.data.task_title);
      status.setText(data.data.task_status_name);
      date.setText(data.data.task_end_date);
      neirong.setText(data.data.task_content);
  }
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
