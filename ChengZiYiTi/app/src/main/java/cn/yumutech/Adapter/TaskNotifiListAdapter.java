package cn.yumutech.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yumutech.bean.TaskNotifiList;
import cn.yumutech.unity.R;

/**
 * Created by Administrator on 2017/1/23.
 */
public class TaskNotifiListAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<TaskNotifiList.DataBean> mDatas;
    private static final int ITEM_VIEW=1;
    private static final int FOOT_VIEW=2;
    OnitemClick onitemClick;
    private boolean isHava;
    public void dataChange(List<TaskNotifiList.DataBean> data, boolean isHava){
        this.mDatas=data;
        this.isHava=isHava;
        notifyDataSetChanged();
    }
    public TaskNotifiListAdapter(Context context,List<TaskNotifiList.DataBean> data){
        this.mContext=context;
        this.mDatas=data;
        mLayoutInflater=LayoutInflater.from(context);
    }
    public void setLisener(OnitemClick click){
        this.onitemClick=click;
    }
    public interface OnitemClick{
        void onitemClice(TaskNotifiList.DataBean data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW) {
            View view = mLayoutInflater.inflate(R.layout.project_manger_item, parent, false);
            return new ItemViewHolder(view);
        }else if (viewType == FOOT_VIEW) {
            View view = mLayoutInflater.inflate(R.layout.instance_load_more_layout, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemViewHolder){
            if(mDatas!=null&&mDatas!=null&&mDatas.size()>0) {
                ((ItemViewHolder) holder).laiyuan.setText(mDatas.get(position).original);
                ((ItemViewHolder) holder).textView.setText(mDatas.get(position).title);
                ((ItemViewHolder) holder).summary.setText(mDatas.get(position).summary);
                ((ItemViewHolder) holder).time.setText(mDatas.get(position).publish_date);
                ((ItemViewHolder)holder).iv.setVisibility(View.GONE);
//                if(mDatas.get(position).logo_path==null||mDatas.get(position).logo_path.equals("")){
//                    ((AppWorkDongTaiViewHoldewr)holder).iv.setVisibility(View.GONE);
//                }else {
//                    ((AppWorkDongTaiViewHoldewr)holder).iv.setVisibility(View.VISIBLE);
//
//                    ImageLoader.getInstance().displayImage(mDatas.get(position).logo_path,((AppWorkDongTaiViewHoldewr)holder).iv);
//                }
//                ImageLoader.getInstance().displayImage(mDatas.get(position).logo_path, ((ItemViewHolder) holder).iv);
                ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onitemClick != null) {
                            onitemClick.onitemClice(mDatas.get(position));
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()&&isHava&&getItemCount()>8) {
            return FOOT_VIEW;
        } else {
            return ITEM_VIEW;
        }
    }
    @Override
    public int getItemCount() {
        return  mDatas!=null?mDatas.size():0;
    }
    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends  RecyclerView.ViewHolder{
        private TextView foot_view_item_tv;
        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv=(TextView)view.findViewById(R.id.foot_view_item_tv);
        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{

        public  TextView textView;
        public  TextView summary;
        public  TextView laiyuan;
        public  TextView time;
        public ImageView iv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv1);
            summary = (TextView) itemView.findViewById(R.id.sumary);
            laiyuan = (TextView) itemView.findViewById(R.id.laiyuan);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
}
