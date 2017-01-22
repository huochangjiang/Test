package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yumutech.bean.ShowTaskDetail;
import cn.yumutech.unity.R;

/**
 * Created by 霍长江 on 2017/1/22.
 */
public class ShowTaskDetaisAdapter  extends BaseAdapter{
    private List<ShowTaskDetail.DataBean.TaskCommentBean.FilesBean> mDatas;
    private Context mContext;
    public ShowTaskDetaisAdapter(Context context,List<ShowTaskDetail.DataBean.TaskCommentBean.FilesBean> datas){
        this.mDatas=datas;
        this.mContext=context;
    }
    @Override
    public int getCount() {
        return mDatas!=null&&mDatas.size()>0?mDatas.size():0;
    }

    public void dataChange(List<ShowTaskDetail.DataBean.TaskCommentBean.FilesBean> datas)
    {
        this.mDatas=datas;
        notifyDataSetChanged();
    }
    @Override
    public ShowTaskDetail.DataBean.TaskCommentBean.FilesBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        View view=convertView;
        if(view==null){
            vh=new ViewHolder();
            view=View.inflate(mContext, R.layout.file_adapter,null);
            vh.tv= (TextView) view.findViewById(R.id.tv);
            vh.iv= (ImageView) view.findViewById(R.id.iv);
            view.setTag(vh);
        }else{
            vh= (ViewHolder) view.getTag();
        }
        vh.tv.setText(mDatas.get(position).file_name);
        vh.iv.setVisibility(View.GONE);
        return view;
    }
    public static class ViewHolder{
        public TextView tv;
        public ImageView iv;
    }

}
