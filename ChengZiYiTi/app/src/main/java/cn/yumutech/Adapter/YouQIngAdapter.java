package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yumutech.bean.YouQIngLianJie;
import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/10.
 */
public class YouQIngAdapter extends BaseAdapter{
    private Context mContext;
    private YouQIngLianJie mDatas;
    public YouQIngAdapter(Context context,YouQIngLianJie data){
        this.mContext=context;
        this.mDatas=data;
    }
    public void dataChange(YouQIngLianJie da){
        this.mDatas=da;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas!=null&&mDatas.data!=null?mDatas.data.size():0;
    }

    @Override
    public YouQIngLianJie.DataBean getItem(int i) {
        return mDatas.data.get(i);
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
        vh.tv.setText(mDatas.data.get(i).title);
        return view;
    }

    public class ViewHolder{
        public ImageView iv;
        public TextView tv;
    }
}
