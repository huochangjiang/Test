package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yumutech.bean.ShowMyPublishedTask;
import cn.yumutech.unity.R;
import cn.yumutech.weight.SaveData;

/**
 * Created by Administrator on 2016/11/22.
 */
public class MyFaBuDeAdapter extends BaseAdapter{
    private Context context;
    private List<ShowMyPublishedTask.DataBean> mData;
    public static Map<Integer,ShowMyPublishedTask.DataBean> maps=new HashMap<>();
    public MyFaBuDeAdapter(Context context,List<ShowMyPublishedTask.DataBean> mData){
        this.context=context;
        this.mData=mData;
    }
    public void dataChange(List<ShowMyPublishedTask.DataBean> mData){
        this.mData=mData;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mData!=null&&mData.size()>0?mData.size():0;
    }

    @Override
    public List<ShowMyPublishedTask.DataBean> getItem(int position) {
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
            myView=View.inflate(context, R.layout.my_fabude_item,null);
            vh.title= (TextView) myView.findViewById(R.id.title);
            vh.content= (TextView) myView.findViewById(R.id.content);
            vh.status= (TextView) myView.findViewById(R.id.status);
            vh.date= (TextView) myView.findViewById(R.id.date);
            vh.deadline= (TextView) myView.findViewById(R.id.deadline);
            vh.tv_faburen= (TextView) myView.findViewById(R.id.tv_faburen);
            vh.tv_fabushijian= (TextView) myView.findViewById(R.id.tv_fabushijian);
            vh.fenge=myView.findViewById(R.id.fenge);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) myView.getTag();
        }
        maps.clear();
        //注释的这一大段是根据任务的状态来改变显示为什么时间，但测试那傻逼说改成这样的统一为截止时间
//        if(mData.get(position).task_status_name.equals("待接受")){
            vh.deadline.setText("截止时间:");
            String time= SaveData.getInstance().getStringDateShort(mData.get(position).task_end_date);
            vh.date.setText(time);
            if(time.isEmpty()){
                vh.deadline.setVisibility(View.GONE);
            }else {
                vh.deadline.setVisibility(View.VISIBLE);
            }
//        }else if(mData.get(position).task_status_name.equals("已接受")){
//            vh.deadline.setText("接受时间:");
//            String time= SaveData.getInstance().getStringDateShort(mData.get(position).task_accept_date);
//            vh.date.setText(time);
//            if(time.isEmpty()){
//                vh.deadline.setVisibility(View.GONE);
//            }else {
//                vh.deadline.setVisibility(View.VISIBLE);
//            }
//        }else if(mData.get(position).task_status_name.equals("已完成")){
//            vh.deadline.setText("完成时间:");
//            String time= SaveData.getInstance().getStringDateShort(mData.get(position).task_finish_date);
//            vh.date.setText(time);
//            if(time.isEmpty()){
//                vh.deadline.setVisibility(View.GONE);
//            }else {
//                vh.deadline.setVisibility(View.VISIBLE);
//            }
//        }
        if(position==mData.size()-1){
            vh.fenge.setVisibility(View.GONE);
        }else {
            vh.fenge.setVisibility(View.VISIBLE);
        }
//        注释的这句是去掉时分秒操作
//        String time1= SaveData.getInstance().getStringDateShort(mData.get(position).task_publish_date);
        String time1= mData.get(position).task_publish_date;
        vh.title.setText(mData.get(position).task_title);
        vh.content.setText(mData.get(position).task_content);
        vh.status.setText(mData.get(position).task_status_name);
//        vh.date.setText(mData.get(position).task_end_date);
        maps.put(position,mData.get(position));
        vh.tv_faburen.setText( getMemberIds(maps));
        vh.tv_fabushijian.setText(time1);
        if(time1.isEmpty()){
            vh.tv_fabushijian.setVisibility(View.GONE);
        }else {
            vh.tv_fabushijian.setVisibility(View.VISIBLE);
        }
        return myView;
    }
    public class ViewHolder{
        public TextView title,content,status,date;
        private TextView tv_faburen,tv_fabushijian,deadline;
        private View fenge;
    }
    List<String> iids = new ArrayList<>();
    //传入数据，将名字用，隔开
    private String getMemberIds(Map<Integer, ShowMyPublishedTask.DataBean> beans) {
        StringBuffer sb = new StringBuffer();
        iids.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            ShowMyPublishedTask.DataBean val = (ShowMyPublishedTask.DataBean) entry.getValue();
            for(int j=0;j<val.assignees.size();j++){
                iids.add(val.assignees.get(j).assignee_user_name);
            }

        }
        for (int i = 0; i < iids.size(); i++) {

            if (i == iids.size() - 1) {
                sb.append(iids.get(i));
            } else {
                sb.append(iids.get(i) + ",");
            }
        }
        return sb.toString();
    }
}
