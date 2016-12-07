package cn.yumutech.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.yumutech.Adapter.ViewHolders.AppPolicyViewHolder;
import cn.yumutech.bean.BumenStatusList;
import cn.yumutech.unity.R;

/**
 * Created by Administrator on 2016/12/7.
 */
public class BumenStatusListAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<BumenStatusList.DataBean> mDatas;
    OnitemClick onitemClick;
    private static final int ITEM_VIEW=1;
    private static final int FOOT_VIEW=2;
    private boolean isHave;
    public void setLisener(OnitemClick click){
        this.onitemClick=click;
    }

    public BumenStatusListAdapter(Context context,List<BumenStatusList.DataBean> data){
        this.mContext=context;
        this.mDatas=data;
        mLayoutInflater=LayoutInflater.from(context);
    }
    public void dataChange(List<BumenStatusList.DataBean> data,boolean isHave){
        this.isHave=isHave;
        this.mDatas=data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW) {
            View view = mLayoutInflater.inflate(R.layout.leader_activity_adapter, parent, false);
            return new AppPolicyViewHolder(view);
        }else if (viewType == FOOT_VIEW) {
            View view = mLayoutInflater.inflate(R.layout.instance_load_more_layout, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof AppPolicyViewHolder){
            if(mDatas!=null&&mDatas!=null&&mDatas.size()>0){
                ((AppPolicyViewHolder)holder).laiyuan.setText(mDatas.get(position).original);
                ((AppPolicyViewHolder)holder).textView.setText(mDatas.get(position).title);
                ((AppPolicyViewHolder)holder).summary.setText(mDatas.get(position).summary);
                ((AppPolicyViewHolder)holder).time.setText(mDatas.get(position).publish_date);
                ImageLoader.getInstance().displayImage(mDatas.get(position).logo_path, ((AppPolicyViewHolder)holder).iv);
                ((AppPolicyViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(onitemClick!=null){
                            onitemClick.onitemClice(mDatas.get(position));
                        }
                    }
                });

            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas!=null&&mDatas!=null?mDatas.size():0;
    }

    public interface OnitemClick{
        void onitemClice(BumenStatusList.DataBean data);
    }
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()&&getItemCount()>8&&isHave) {
            return FOOT_VIEW;
        } else {
            return ITEM_VIEW;
        }
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
}
