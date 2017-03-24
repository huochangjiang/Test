package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.yumutech.bean.ShowTaskDetail;
import cn.yumutech.unity.R;
import cn.yumutech.weight.SaveData;

/**
 */
public class XiangqingZhipaiAdaper extends BaseAdapter{
    private ShowTaskDetail data;
    private Context context;
    public XiangqingZhipaiAdaper(ShowTaskDetail mData,Context context){
        this.data=mData;
        this.context=context;
    }
    public void dataChange(ShowTaskDetail data){
        this.data=data;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return data!=null&&data.data!=null&&data.data.assign!=null&&data.data.assign.size()>0?data.data.assign.size():0;
    }

    @Override
    public ShowTaskDetail getItem(int i) {
        return data;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView =view;
        ViewHolder vh;
        if(myView==null) {
            vh = new ViewHolder();
            myView=View.inflate(context, R.layout.xiangqing_zhipai_item,null);
            vh.zhu_zhipaishijian= (TextView) myView.findViewById(R.id.zhu_zhipaishijian);
            vh.tv_zhipairen1= (TextView) myView.findViewById(R.id.tv_zhipairen1);
            vh.zhu_banren= (TextView) myView.findViewById(R.id.zhu_banren);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) view.getTag();
        }
        if(data.data.assign!=null&&data.data.assign.size()>0){
            String time1= SaveData.getInstance().getStringDateShort(data.data.assign.get(i).assign_date);
//            String time1= data.data.assign.get(i).assign_date;
            vh.zhu_zhipaishijian.setText(time1);
            vh.tv_zhipairen1.setText(data.data.assign.get(i).assigner_user_name);
            vh.zhu_banren.setText(data.data.assign.get(i).assignee_user_name);
        }
        return myView;
    }
    public class ViewHolder{
        private TextView zhu_zhipaishijian,tv_zhipairen1,zhu_banren;
    }
}
