package cn.yumutech.unity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/1/18.
 */
public class InspectionTaskNewActivity extends BaseActivity implements View.OnClickListener{
    private ImageView back;
    private RelativeLayout rl_item_one,rl_item_two,rl_item_three,rl_item_four,rl_item_five,rl_item_six,rl_item_seven,rl_item_eitht;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_inspection_task_new;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        back= (ImageView) findViewById(R.id.back);
        rl_item_one= (RelativeLayout) findViewById(R.id.rl_item_one);
        rl_item_one.setOnClickListener(this);
        rl_item_two= (RelativeLayout) findViewById(R.id.rl_item_two);
        rl_item_two.setOnClickListener(this);
        rl_item_three= (RelativeLayout) findViewById(R.id.rl_item_three);
        rl_item_three.setOnClickListener(this);
        rl_item_four= (RelativeLayout) findViewById(R.id.rl_item_four);
        rl_item_four.setOnClickListener(this);
        rl_item_five= (RelativeLayout) findViewById(R.id.rl_item_five);
        rl_item_five.setOnClickListener(this);
        rl_item_six= (RelativeLayout) findViewById(R.id.rl_item_six);
        rl_item_six.setOnClickListener(this);
        rl_item_seven= (RelativeLayout) findViewById(R.id.rl_item_seven);
        rl_item_seven.setOnClickListener(this);
        rl_item_eitht= (RelativeLayout) findViewById(R.id.rl_item_eitht);
        rl_item_eitht.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_item_one:
                Intent intent1=new Intent();
                intent1.setClass(InspectionTaskNewActivity.this,ReleaseTaskActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_item_two:
                Intent intent2=new Intent();
                intent2.setClass(InspectionTaskNewActivity.this,MyFaBuDeActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_item_three:
                Intent intent3=new Intent();
                intent3.setClass(InspectionTaskNewActivity.this,MyTaskActivity.class);
                startActivity(intent3);
                break;
            case R.id.rl_item_four:
                Intent intent4=new Intent();
                intent4.setClass(InspectionTaskNewActivity.this,TaskNotifiListActivity.class);
                startActivity(intent4);
                break;
            case R.id.rl_item_five:
                Intent intent5=new Intent();
                intent5.setClass(InspectionTaskNewActivity.this,CommunicationNewActivity.class);
                startActivity(intent5);
                break;
            case R.id.rl_item_six:
                Intent intent6=new Intent();
                intent6.setClass(InspectionTaskNewActivity.this,CommuContactActivity.class);
                startActivity(intent6);
                break;
            case R.id.rl_item_seven:
                Intent intent7=new Intent();
                intent7.setClass(InspectionTaskNewActivity.this,CommuGroupActivity.class);
                startActivity(intent7);
                break;
            case R.id.rl_item_eitht:
//                Intent intent8=new Intent();
//                intent8.setClass(InspectionTaskNewActivity.this,TaShanZhiShiActivity.class);
//                startActivity(intent8);
                break;
            default:
                break;
        }
    }
}
