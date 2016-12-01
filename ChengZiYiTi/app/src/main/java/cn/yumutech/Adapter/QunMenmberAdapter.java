package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.yumutech.bean.ChaXunQunMenmber;
import cn.yumutech.unity.R;

/**
 * Created by 霍长江 on 2016/11/17.
 */
public class QunMenmberAdapter extends BaseAdapter{

    private Context mContext;
    private List<ChaXunQunMenmber.DataBean.UsersBean> mDatas;
    public QunMenmberAdapter(Context context,List<ChaXunQunMenmber.DataBean.UsersBean> data){
        this.mContext=context;
        this.mDatas=data;
    }
    onItemClickLisener lisener;
    public void setLisener(onItemClickLisener lisener){
        this.lisener=lisener;
    }
    public void dataChange(List<ChaXunQunMenmber.DataBean.UsersBean> data){
        this.mDatas=data;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas!=null&&mDatas.size()>0?mDatas.size()+1:0;
    }

    @Override
    public ChaXunQunMenmber.DataBean.UsersBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.qunmember_item, null);
            vh.iv = (ImageView) convertView.findViewById(R.id.iv);
            vh.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (position == mDatas.size()) {
            vh.tv.setText("添加讨论组成员");
            vh.iv.setImageResource(R.drawable.tianjiaqunchengyuan);
        } else {
            vh.tv.setVisibility(View.VISIBLE);
        }
        if (!(position == mDatas.size())){
            ImageLoader.getInstance().displayImage(mDatas.get(position).user_logo_path, vh.iv);
            vh.tv.setText(mDatas.get(position).user_name);
    }
        vh.iv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            if(lisener!=null){
                if(position==mDatas.size()){
                    lisener.getUserBean(null,position);
                }else {
                    lisener.getUserBean(mDatas.get(position), position);
                }
            }
           }
       });


        return convertView;
    }
    public class ViewHolder{
        public ImageView iv;
        public TextView tv;
    }
    public interface  onItemClickLisener{
        void getUserBean(ChaXunQunMenmber.DataBean.UsersBean beans,int postion);
    }
}
