package cn.yumutech.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.yumutech.Adapter.ViewHolders.ProjectItemViewHolder;
import cn.yumutech.bean.ProjectManger;
import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/10.
 */
public class ProjectMangerAdpater extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<ProjectManger.DataBean> mDatas;
    OnitemClick onitemClick;
    private static final int ITEM_VIEW=1;
    private static final int FOOT_VIEW=2;
    private boolean isHave;
    public void dataChange(List<ProjectManger.DataBean> data,boolean isHave){
        this.isHave=isHave;
        this.mDatas=data;
        notifyDataSetChanged();
    }
    public ProjectMangerAdpater(Context context,List<ProjectManger.DataBean> data){
        this.mContext=context;
        this.mDatas=data;
        mLayoutInflater=LayoutInflater.from(context);
    }
    public void setLisener(OnitemClick click){
        this.onitemClick=click;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW) {
            View view = mLayoutInflater.inflate(R.layout.project_manger_item, parent, false);
            return new ProjectItemViewHolder(view);
        }else if (viewType == FOOT_VIEW) {
            View view = mLayoutInflater.inflate(R.layout.instance_load_more_layout, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ProjectItemViewHolder) {
            if (mDatas != null && mDatas != null && mDatas.size() > 0) {
                ((ProjectItemViewHolder) holder).laiyuan.setText(mDatas.get(position).original);
                ((ProjectItemViewHolder) holder).textView.setText(mDatas.get(position).title);
                ((ProjectItemViewHolder) holder).summary.setText(mDatas.get(position).summary);
                ((ProjectItemViewHolder) holder).time.setText(mDatas.get(position).publish_date);
                if(mDatas.get(position).amount.equals("")) {
                    ((ProjectItemViewHolder) holder).money.setText("项目金额:"+"0元");
                }else{
                    ((ProjectItemViewHolder) holder).money.setText("项目金额:"+mDatas.get(position).amount+"元");
                }
                ((ProjectItemViewHolder) holder).guifua.setText("类型:"+mDatas.get(position).type);
                ImageLoader.getInstance().displayImage(mDatas.get(position).logo_path, ((ProjectItemViewHolder) holder).iv);
                ((ProjectItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
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
    public int getItemCount() {
        return mDatas!=null&&mDatas!=null?mDatas.size():0;
    }

    public interface OnitemClick{
        void onitemClice(ProjectManger.DataBean data);
    }
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()&&getItemCount()>5&&isHave) {
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
