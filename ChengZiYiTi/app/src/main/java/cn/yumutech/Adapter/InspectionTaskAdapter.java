package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.yumutech.unity.R;

/**
 * Created by Administrator on 2016/11/21.
 */
public class InspectionTaskAdapter extends BaseAdapter{
    private Context context;
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) myView.getTag();
        }
        return myView;
    }
    public class ViewHolder{
        public TextView title,content,status,date;
    }
}
