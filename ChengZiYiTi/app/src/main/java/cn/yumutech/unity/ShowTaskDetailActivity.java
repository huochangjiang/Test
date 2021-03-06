package cn.yumutech.unity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.yumutech.Adapter.ShowTaskDetaisAdapter;
import cn.yumutech.Adapter.XiangqingZhipaiAdaper;
import cn.yumutech.LookResultAdapter;
import cn.yumutech.bean.AcceptTask;
import cn.yumutech.bean.AcceptTaskBeen;
import cn.yumutech.bean.ShowTaskDetail;
import cn.yumutech.bean.ShowTaskDetailBeen;
import cn.yumutech.netUtil.Api;
import cn.yumutech.netUtil.FileUtils;
import cn.yumutech.netUtil.HttpRequest;
import cn.yumutech.weight.MyListview;
import cn.yumutech.weight.SaveData;
import cn.yumutech.weight.SignOutDilog;
import cn.yumutech.weight.ViewPagerDilog;
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
    private Button bt_accept;
    private RelativeLayout complete;
    private RelativeLayout wanchen;
    private ImageView back;
    private View myprog;
    private RelativeLayout all;
    private ShowTaskDetail mData;
    private Button assign;
    private MyListview listview;
    private TextView tv_faburen,tv_fabushijian;
    private TextView zhuangtaishijian;
    private XiangqingZhipaiAdaper adaper;
    private TextView wancheng_title,name,complete_time,wancheng_neirong;
    private GridView gridView;
    private RelativeLayout rl_wancheng;
    private LookResultAdapter lookAdapter;
    private View fenge,fengexian;
    private TextView zhuangtaishijian1;
    private TextView wanchenzhe;
    public List<String> phones=new ArrayList<>();
    private boolean isDownLoad=false;
    private RelativeLayout rl_zong;
    private List<ShowTaskDetail.DataBean.TaskCommentBean.FilesBean> mDatas=new ArrayList<>();

    //
//    private RelativeLayout rl_zhipai,rl_zhipai2;
    private View xian;
    private ListView mFilelistView;
    private ShowTaskDetaisAdapter showAdapter;
    private List<File> mFileList;
    private String tv_poeple;
    private String type="0";
    private TextView faburen;
    private TextView tv_beijing;
    private String project_id,fenlei;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_task_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initExtra();
        wanchenzhe= (TextView) findViewById(R.id.wanchenzhe);
        title= (TextView) findViewById(R.id.title);
        status= (TextView) findViewById(R.id.status);
        date= (TextView) findViewById(R.id.date);
        neirong= (TextView) findViewById(R.id.neirong);
        accept= (RelativeLayout) findViewById(R.id.accept);
        complete= (RelativeLayout) findViewById(R.id.complete);
        wanchen= (RelativeLayout) findViewById(R.id.wanchen);
        bt_accept= (Button) findViewById(R.id.bt_accept);
        back= (ImageView) findViewById(R.id.back);
        zhuangtaishijian= (TextView) findViewById(R.id.zhuangtaishijian);
        myprog=findViewById(R.id.myprog);
        listview= (MyListview) findViewById(R.id.listview);
        adaper=new XiangqingZhipaiAdaper(mData,this);
        listview.setAdapter(adaper);
        xian=findViewById(R.id.xian);
        tv_faburen= (TextView) findViewById(R.id.tv_faburen);
        tv_fabushijian= (TextView) findViewById(R.id.tv_fabushijian);
        assign= (Button) findViewById(R.id.assign);
        if(App.getContext().getLogo("logo").data.publish_task_flag.equals("1")){
            assign.setVisibility(View.VISIBLE);
        }else {
            assign.setVisibility(View.GONE);
        }
        all= (RelativeLayout) findViewById(R.id.all);
        myprog.setVisibility(View.VISIBLE);
        all.setVisibility(View.GONE);
        fengexian=findViewById(R.id.fengexian);
        zhuangtaishijian1= (TextView) findViewById(R.id.zhuangtaishijian1);
        //查看界面的相关布局初始化
        wancheng_title= (TextView) findViewById(R.id.wancheng_title);
        name= (TextView) findViewById(R.id.name);
        fenge=findViewById(R.id.fenge);
        complete_time= (TextView) findViewById(R.id.complete_time);
        wancheng_neirong= (TextView) findViewById(R.id.wancheng_neirong);
        gridView= (GridView) findViewById(R.id.gridView);
        rl_wancheng= (RelativeLayout) findViewById(R.id.rl_wancheng);
        gridView= (GridView) findViewById(R.id.gridView);
        mFilelistView = (ListView) findViewById(R.id.listView);
        showAdapter = new ShowTaskDetaisAdapter(this,mDatas);
        mFilelistView.setAdapter(showAdapter);
        rl_zong= (RelativeLayout) findViewById(R.id.rl_zong);
        faburen= (TextView) findViewById(R.id.faburen);
        tv_beijing= (TextView) findViewById(R.id.tv_beijing);
        if(type.equals("1")){
            faburen.setText("指派给:");
            listview.setVisibility(View.VISIBLE);
            xian.setVisibility(View.VISIBLE);
        }else {
            faburen.setText("发布人:");
            listview.setVisibility(View.GONE);
            xian.setVisibility(View.GONE);
        }
//        if(SaveData.getInstance().showTaskComplete!=null){
//            mData=SaveData.getInstance().showTaskComplete;
            lookAdapter=new LookResultAdapter(ShowTaskDetailActivity.this,mData);
            gridView.setAdapter(lookAdapter);
           mFilelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isDownLoad=false;
                    final  ShowTaskDetail.DataBean.TaskCommentBean.FilesBean item=showAdapter.getItem(position);
                List<File> mfies=FileUtils.listPathFiles(Environment.getExternalStorageDirectory() + File.separator + App.getContext().ExternalImageDir);
                File loadFile;
                for (int i=0;i<mfies.size();i++){
                   if(item.file_name.equals(mfies.get(i).getName())){
                       isDownLoad=true;
                      openFile(mfies.get(i).getAbsolutePath());
                   }
                }
                if(isDownLoad){
//                    Toast.makeText(ShowTaskDetailActivity.this, "文件已经下载了", Toast.LENGTH_SHORT).show();
                }
                if(!isDownLoad) {
                    SignOutDilog msinDilog = new SignOutDilog(ShowTaskDetailActivity.this, "确认下载？");
                    msinDilog.show();
                    msinDilog.setOnLisener(new SignOutDilog.onListern() {
                        @Override
                        public void send() {
                            showDilog("下载中...");
                            HttpRequest.getInstance(ShowTaskDetailActivity.this).downLoadFile(mHandler, item.file_path, item.file_name);
                        }
                    });
                }
            }
        });
//        }
    }

    private void initExtra() {
        if(getIntent()!=null){
            task_id=getIntent().getStringExtra("task_id");
            tv_poeple=getIntent().getStringExtra("task_poeple");
            type=getIntent().getStringExtra("type");
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
        tv_beijing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ShowTaskDetailActivity.this,ProjectDetaisActivity.class);
                intent.putExtra("id",project_id);
                intent.putExtra("fenlei","");
                intent.putExtra("type","3");
                startActivity(intent);
            }
        });
        bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mData!=null&&mData.data!=null&&mData.data.task_id!=null){
                    showDilog("接受中...");
                    initAcceptTask(mData.data.task_id);
                }
            }
        });
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShowTaskDetailActivity.this,CompleteActivity.class);
                intent.putExtra("tastid",mData.data.task_id);
                startActivity(intent);
            }
        });
        wanchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ShowTaskDetailActivity.this,LookResultActivity.class);
                startActivity(intent);
            }
        });
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(ShowTaskDetailActivity.this,TaskToWhoNewActivity.class);
                intent.putExtra("type","2");
                intent.putExtra("task_id",mData.data.task_id);
                startActivity(intent);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ViewPagerDilog vp=new ViewPagerDilog(ShowTaskDetailActivity.this, (ArrayList<String>) phones,i);
                vp.show();
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
                MissDilog();
                SaveData.getInstance().isJieshou=true;
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
            myprog.setVisibility(View.GONE);
        }

        @Override
        public void onNext(ShowTaskDetail showTaskDetail) {
            String data=new Gson().toJson(showTaskDetail);
            Log.e("getShowTaskDetail",data);
            if(showTaskDetail.data.task_projectwork_id.equals("")||showTaskDetail.data.task_projectwork_id.equals("0")){
                tv_beijing.setVisibility(View.GONE);
            }else {
                project_id=showTaskDetail.data.task_projectwork_id;
//                fenlei=showTaskDetail.data.task_projectwork_title;
                tv_beijing.setVisibility(View.VISIBLE);
            }
            if(showTaskDetail.status.code.equals("0")&&showTaskDetail!=null){
                mData=showTaskDetail;
                if(showTaskDetail.data.task_status_name.equals("待接受")){
                    zhuangtaishijian.setText("截止时间:");
                    String time1=SaveData.getInstance().getStringDateShort(showTaskDetail.data.task_end_date);
//                    String time1=showTaskDetail.data.task_end_date;
                    date.setText(time1);
                    fengexian.setVisibility(View.GONE);
                    accept.setVisibility(View.VISIBLE);
                    complete.setVisibility(View.GONE);
                    rl_zong.setVisibility(View.VISIBLE);
                    rl_wancheng.setVisibility(View.GONE);
                }else if(showTaskDetail.data.task_status_name.equals("已接受")){
//                    zhuangtaishijian.setText("接受时间:");
//                    String time1=SaveData.getInstance().getStringDateShort(showTaskDetail.data.task_accept_date);
//                    date.setText(time1);
                    accept.setVisibility(View.GONE);
                    rl_zong.setVisibility(View.VISIBLE);
                    fengexian.setVisibility(View.VISIBLE);
                    complete.setVisibility(View.VISIBLE);
                    rl_wancheng.setVisibility(View.VISIBLE);
                    jieshou(mData);
                }else if(showTaskDetail.data.task_status_name.equals("已完成")){
//                    zhuangtaishijian.setText("完成时间:");
//                    String time1=SaveData.getInstance().getStringDateShort(showTaskDetail.data.task_finish_date);
//                    date.setText(time1);
                    rl_zong.setVisibility(View.GONE);
                    fengexian.setVisibility(View.VISIBLE);
                    accept.setVisibility(View.GONE);
                    complete.setVisibility(View.GONE);
                    rl_wancheng.setVisibility(View.VISIBLE);
                    if(showTaskDetail.data.task_comment.getFiles()!=null&&showTaskDetail.data.task_comment.getFiles().size()>0){
                        showAdapter.dataChange(showTaskDetail.data.task_comment.getFiles());
                    }
                    lookResult(mData);
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

      neirong.setText(data.data.task_content);
      String time=data.data.task_publish_date;
//      String time=SaveData.getInstance().getStringDateShort(data.data.task_publish_date);
      tv_fabushijian.setText(time);
      if(type.equals("1")){
          tv_faburen.setText(tv_poeple);
      }else if(type.equals("2")){
          tv_faburen.setText(data.data.task_publish_user_name);
      }
      if(data.data.assign!=null&&data.data.assign.size()>0){
          adaper.dataChange(data);
          if(type.equals("1")){
              xian.setVisibility(View.VISIBLE);
          }else if(type.equals("2")){
              xian.setVisibility(View.GONE);
          }
      }else {
          xian.setVisibility(View.GONE);
      }
  }
    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(App.getContext().getLogo("logo")!=null&&task_id!=null){
            ShowTaskDetailBeen been=new ShowTaskDetailBeen(new ShowTaskDetailBeen.UserBean(App.getContext().getLogo("logo").data.id,""),
                    new ShowTaskDetailBeen.DataBean(task_id));
            initData1(new Gson().toJson(been));
        }
    }
    //查看详情
    private void lookResult(ShowTaskDetail data){
        if(data!=null&&data.data!=null&&data.data.task_comment!=null&&data.data.task_comment.photos!=null){
            lookAdapter.dataChange(data);
            for (int i=0;i<mData.data.task_comment.photos.size();i++){
                phones.add(mData.data.task_comment.photos.get(i).photo_path);
            }
        }
        wancheng_title.setText("任务已完成");
        zhuangtaishijian1.setText("完成时间:");
        wanchenzhe.setText("完成者:");

        if( data!=null){
            name.setText(data.data.task_finish_user_name);
//            String time1=SaveData.getInstance().getStringDateShort(data.data.task_finish_date);
            String time1=data.data.task_finish_date;
            wancheng_neirong.setVisibility(View.VISIBLE);
            complete_time.setText(time1);
            wancheng_neirong.setText(data.data.task_comment.taskcomment_content);
            lookAdapter.dataChange(data);
            if(data.data.task_comment!=null&&
                    data.data.task_comment.photos!=null&&
                    data.data.task_comment.photos.size()>0){
                fenge.setVisibility(View.VISIBLE);
            }else {
                fenge.setVisibility(View.GONE);
            }
        }
    }
    //任务已经jieshou
    private  void  jieshou(ShowTaskDetail data){
        if( data!=null){
            zhuangtaishijian1.setText("接受时间:");
            wancheng_title.setText("任务已接受");
            wanchenzhe.setText("接受者:");
            name.setText(data.data.task_accept_user_name);
            String time1=data.data.task_accept_date;
            complete_time.setText(time1);
            wancheng_neirong.setVisibility(View.GONE);
//            wancheng_neirong.setText(data.data.task_comment.taskcomment_content);
//            lookAdapter.dataChange(data);
//            if(data.data.task_comment!=null&&
//                    data.data.task_comment.photos!=null&&
//                    data.data.task_comment.photos.size()>0){
//                fenge.setVisibility(View.VISIBLE);
//            }else {
                fenge.setVisibility(View.GONE);
//            }
        }
    }
    //下载文件
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 101:
                    MissDilog();
                    String path= (String) msg.obj;
                    Toast.makeText(ShowTaskDetailActivity.this, "保存在"+path+"目录中", Toast.LENGTH_SHORT).show();
                    openFile(path);
                    break;
                case 102:
                    MissDilog();
                    Toast.makeText(ShowTaskDetailActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private void openFile(String path) {
        File apkfile = new File(path);
        if (!apkfile.exists()) {
            return;
        }

        try {
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(apkfile), getMIMEType(apkfile));
            startActivity(intent);
        } catch (Exception e) {
// TODO: handle exception
            Toast.makeText(ShowTaskDetailActivity.this, "请安装相关插件", Toast.LENGTH_LONG).show();
        }

    }

    private static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            type = "image/*";
        } else if (end.equals("doc") || end.equals("docx")) {
            type = "application/msword";
        } else if (end.equals("ppt") || end.equals("pot") || end.equals("pps")) {
            type = "application/vnd.ms-powerpoint";
        } else if (end.equals("xla") || end.equals("xlc") || end.equals("xlm") || end.equals("xls") || end.equals("xlt") || end.equals("xlw") || end.equalsIgnoreCase("xlsx")) {
            type = "application/vnd.ms-excel";
        } else if (end.equals("xll")) {
            type = "application/x-excel";
        } else if (end.equals("pdf")) {
            type = "application/pdf";
        } else if (end.equals("zip")) {
            type = "application/zip";
        } else if (end.equals("rar")) {
            type = "application/x-rar-compressed";
        } else if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            type = "/*";
        }
        return type;
    }


}
