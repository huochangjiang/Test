package cn.yumutech.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import cn.yumutech.Adapter.ViewHolders.ApplyFooterViewHolder;
import cn.yumutech.Adapter.ViewHolders.ApplyHeaderItemViewHolder;
import cn.yumutech.Adapter.ViewHolders.ApplyItemViewHolder;
import cn.yumutech.bean.LeaderActivitys;
import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/8.
 */
public class LeaderActivityAdapter extends SectionedRecyclerViewAdapter<ApplyHeaderItemViewHolder,
        ApplyItemViewHolder,
        ApplyFooterViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private LeaderActivitys mDatas;
    OnitemClick onitemClick;
    public void dataChange(LeaderActivitys data){
        this.mDatas=data;
        notifyDataSetChanged();
    }
    public LeaderActivityAdapter(Context context,LeaderActivitys data){
        this.mContext=context;
        this.mDatas=data;
        mLayoutInflater=LayoutInflater.from(context);
    }
    public void setLisener(OnitemClick click){
        this.onitemClick=click;
    }
    @Override
    protected int getSectionCount() {
        return mDatas!=null&&mDatas.data!=null?mDatas.data.size():0;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return 1;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected boolean hasHeaderInSection(int section) {
        return false;
    }

    @Override
    protected ApplyHeaderItemViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected ApplyFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected ApplyItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.leader_activity_adapter, parent, false);
        return new ApplyItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(ApplyHeaderItemViewHolder holder, int section) {

    }

    @Override
    protected void onBindSectionFooterViewHolder(ApplyFooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(ApplyItemViewHolder holder,final int section, final int position) {
        if(holder instanceof ApplyItemViewHolder){
            if(mDatas!=null&&mDatas.data!=null&&mDatas.data.size()>0){
                ((ApplyItemViewHolder)holder).laiyuan.setText(mDatas.data.get(section).original);
                ((ApplyItemViewHolder)holder).textView.setText(mDatas.data.get(section).title);
                ((ApplyItemViewHolder)holder).summary.setText(mDatas.data.get(section).summary);
                ((ApplyItemViewHolder)holder).time.setText(mDatas.data.get(section).publish_date);
                ImageLoader.getInstance().displayImage(mDatas.data.get(section).logo_path, ((ApplyItemViewHolder)holder).iv);
                ((ApplyItemViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(onitemClick!=null){
                            onitemClick.onitemClice(mDatas.data.get(section));
                        }
                    }
                });

            }
        }

    }


    public interface OnitemClick{
        void onitemClice(LeaderActivitys.DataBean data);
    }
}
