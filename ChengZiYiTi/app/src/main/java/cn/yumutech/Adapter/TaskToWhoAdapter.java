package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.unity.R;
import cn.yumutech.weight.Yuanxing;

/**
 * Created by Administrator on 2016/11/22.
 */
public class TaskToWhoAdapter extends BaseAdapter{
    private Context context;
    private List<UserAboutPerson.DataBean> mData;
    public TaskToWhoAdapter(Context context,List<UserAboutPerson.DataBean> mData){
        this.context=context;
        this.mData=mData;
    }
    public void dataChange(List<UserAboutPerson.DataBean> mData){
        this.mData=mData;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mData!=null&&mData.size()>0?mData.size():0;
    }

    @Override
    public List<UserAboutPerson.DataBean> getItem(int position) {
        return mData;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView =convertView;
        ViewHolder vh;
        if(myView==null) {
            vh = new ViewHolder();
            myView=View.inflate(context, R.layout.task_to_who_item,null);
            vh.touxiang= (Yuanxing) myView.findViewById(R.id.touxiang);
            vh.employees= (TextView) myView.findViewById(R.id.employees);
            vh.selecte= (ImageView) myView.findViewById(R.id.selecte);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) myView.getTag();
        }
        ImageLoader.getInstance().displayImage(mData.get(position).logo_path,vh.touxiang);
        vh.employees.setText(mData.get(position).nickname);
        return myView;
    }

    public class ViewHolder{
        public Yuanxing touxiang;
        public TextView employees;
        public ImageView selecte;
    }
}
