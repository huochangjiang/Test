package cn.yumutech.unity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.LookResultAdapter;
import cn.yumutech.bean.ShowTaskDetail;
import cn.yumutech.weight.SaveData;
import cn.yumutech.weight.ViewPagerDilog;

/**
 * Created by Allen on 2016/11/24.
 */
public class LookResultActivity extends BaseActivity{
    private ImageView back;
    private TextView title,name,complete_time,neirong;
    private GridView gridView;
    private ShowTaskDetail mData;
    private View fenge;
    private LookResultAdapter adapter;
    public List<String> phones=new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_look_result;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        back= (ImageView) findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title);
        name= (TextView) findViewById(R.id.name);
        fenge=findViewById(R.id.fenge);
        complete_time= (TextView) findViewById(R.id.complete_time);
        neirong= (TextView) findViewById(R.id.neirong);
        gridView= (GridView) findViewById(R.id.gridView);
        if(SaveData.getInstance().showTaskComplete!=null){
            mData=SaveData.getInstance().showTaskComplete;
            adapter=new LookResultAdapter(LookResultActivity.this,mData);
            gridView.setAdapter(adapter);

            for (int i=0;i<mData.data.task_comment.photos.size();i++){
                    phones.add(mData.data.task_comment.photos.get(i).photo_path);
            }
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    ViewPagerDilog vp=new ViewPagerDilog(LookResultActivity.this, (ArrayList<String>) phones,i);
                    vp.show();
                }
            });
        }




    }

    @Override
    protected void initData() {
        if(getIntent()!=null&& SaveData.getInstance().showTaskComplete!=null){
            name.setText(SaveData.getInstance().showTaskComplete.data.task_finish_user_name);
            complete_time.setText(SaveData.getInstance().showTaskComplete.data.task_finish_date);
            neirong.setText(SaveData.getInstance().showTaskComplete.data.task_content);
            adapter.dataChange(SaveData.getInstance().showTaskComplete);
            if(SaveData.getInstance().showTaskComplete.data.task_comment!=null&&
                    SaveData.getInstance().showTaskComplete.data.task_comment.photos!=null&&
                    SaveData.getInstance().showTaskComplete.data.task_comment.photos.size()>0){
                fenge.setVisibility(View.VISIBLE);
            }else {
                fenge.setVisibility(View.GONE);
            }
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
}
