package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yumutech.bean.YouQIngLianJie;
import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/10.
 */
public class YouQIngAdapter extends BaseAdapter{
    private Context mContext;
     private List<YouQIngLianJie.DataBean> mDatas;
    public YouQIngAdapter(Context context,List<YouQIngLianJie.DataBean> data){
        this.mContext=context;
        this.mDatas=data;
    }
    public void dataChange(List<YouQIngLianJie.DataBean> da){
        this.mDatas=da;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas!=null&&mDatas!=null?mDatas.size():0;
    }

    @Override
    public YouQIngLianJie.DataBean getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if(view==null){
            view=View.inflate(mContext, R.layout.frieds_item,null);
            vh=new ViewHolder();
            vh.iv= (ImageView) view.findViewById(R.id.iv);
            vh.tv= (TextView) view.findViewById(R.id.tv);
            view.setTag(vh);
        }else{
            vh= (ViewHolder) view.getTag();
        }
        vh.tv.setText(mDatas.get(i).title);
        return view;
    }

    public class ViewHolder{
        public ImageView iv;
        public TextView tv;
    }
}
