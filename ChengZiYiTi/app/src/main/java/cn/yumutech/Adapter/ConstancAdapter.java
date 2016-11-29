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

/**
 * Created by 霍长江 on 2016/11/17.
 */
public class ConstancAdapter extends BaseAdapter{
    private Context mContext;
    private List<UserAboutPerson.DataBean> mDatas;
    private boolean isShuYu=true;
    public ConstancAdapter(Context context,List<UserAboutPerson.DataBean> data){
        this.mContext=context;
        this.mDatas=data;
    }
    public void dataChange(List<UserAboutPerson.DataBean> data){
        this.mDatas=data;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public UserAboutPerson.DataBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.constanct_item,null);
            vh.tv_phone= (TextView) convertView.findViewById(R.id.tv_phone);
            vh.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            vh.iv= (ImageView) convertView.findViewById(R.id.iv_phone);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        vh.tv_name.setText(mDatas.get(position).nickname);
        vh.tv_phone.setText(mDatas.get(position).mobile);
        ImageLoader.getInstance().displayImage(mDatas.get(position).logo_path,vh.iv);
        return convertView;
    }
    public class ViewHolder{
        public ImageView iv;
        public TextView tv_phone;
        public TextView tv_name;
    }
}
