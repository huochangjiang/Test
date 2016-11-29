package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.yumutech.bean.ShowMyTask;
import cn.yumutech.unity.R;
import cn.yumutech.weight.SaveData;

/**
 * Created by Administrator on 2016/11/21.
 */
public class InspectionTaskAdapter extends BaseAdapter{
    private Context context;
    private List<ShowMyTask.DataBean> mData;
    public InspectionTaskAdapter(Context context,List<ShowMyTask.DataBean> mData){
        this.context=context;
        this.mData=mData;
    }
    @Override
    public int getCount() {
        return mData.size();
    }
    public void dataChange(List<ShowMyTask.DataBean> mData){
        this.mData=mData;
        notifyDataSetChanged();
    }
    @Override
    public List<ShowMyTask.DataBean> getItem(int position) {
        return mData;
    }

    @Override
    public long getItemId(int position) {
       return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView = convertView;
        ViewHolder vh;
        if(myView==null){
            vh=new ViewHolder();
            myView=View.inflate(context, R.layout.inspection_task_item,null);
            vh.title= (TextView) myView.findViewById(R.id.title);
            vh.content= (TextView) myView.findViewById(R.id.content);
            vh.status= (TextView) myView.findViewById(R.id.status);
            vh.date= (TextView) myView.findViewById(R.id.date);
            vh.deadline= (TextView) myView.findViewById(R.id.deadline);
            vh.tv_faburen= (TextView) myView.findViewById(R.id.tv_faburen);
            vh.tv_fabushijian= (TextView) myView.findViewById(R.id.tv_fabushijian);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) myView.getTag();
        }
        if(mData.get(position).task_status_name.equals("待接受")){
            vh.deadline.setText("截止时间:");
            String time=SaveData.getInstance().getStringDateShort(mData.get(position).task_end_date);
//            vh.date.setText(mData.get(position).task_end_date);
            vh.date.setText(time);
        }else if(mData.get(position).task_status_name.equals("已接受")){
            vh.deadline.setText("接受时间:");
            String time=SaveData.getInstance().getStringDateShort(mData.get(position).task_accept_date);
            vh.date.setText(time);
        }else if(mData.get(position).task_status_name.equals("已完成")){
            vh.deadline.setText("完成时间:");
            String time=SaveData.getInstance().getStringDateShort(mData.get(position).task_finish_date);
            vh.date.setText(time);
        }

        String time1=SaveData.getInstance().getStringDateShort(mData.get(position).task_publish_date);
        vh.title.setText(mData.get(position).task_title);
        vh.content.setText(mData.get(position).task_content);
        vh.status.setText(mData.get(position).task_status_name);
//        vh.date.setText(mData.get(position).task_end_date);
        vh.tv_faburen.setText(mData.get(position).task_publish_user_name);
        vh.tv_fabushijian.setText(time1);
        return myView;
    }
    public class ViewHolder{
        public TextView title,content,status,date;
        private TextView tv_faburen,tv_fabushijian,deadline;
    }
}
