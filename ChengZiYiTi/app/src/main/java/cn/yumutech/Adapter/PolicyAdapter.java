package cn.yumutech.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import cn.yumutech.Adapter.ViewHolders.AppPolicyViewHolder;
import cn.yumutech.Adapter.ViewHolders.ApplyFooterViewHolder;
import cn.yumutech.Adapter.ViewHolders.ApplyHeaderItemViewHolder;
import cn.yumutech.bean.ZhengCeFile;
import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/9.
 */
public class PolicyAdapter extends SectionedRecyclerViewAdapter<ApplyHeaderItemViewHolder,
        AppPolicyViewHolder,
        ApplyFooterViewHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ZhengCeFile mDatas;
    OnitemClick onitemClick;
    public void setLisener(OnitemClick click){
        this.onitemClick=click;
    }

    public PolicyAdapter(Context context,ZhengCeFile data){
        this.mContext=context;
        this.mDatas=data;
        mLayoutInflater=LayoutInflater.from(context);
    }
    public void dataChange(ZhengCeFile data){
        this.mDatas=data;
        notifyDataSetChanged();
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
    protected AppPolicyViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.leader_activity_adapter, parent, false);
        return new AppPolicyViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(ApplyHeaderItemViewHolder holder, int section) {

    }

    @Override
    protected void onBindSectionFooterViewHolder(ApplyFooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(AppPolicyViewHolder holder, final int section, final int position) {
        if(holder instanceof AppPolicyViewHolder){
            if(mDatas!=null&&mDatas.data!=null&&mDatas.data.size()>0){
                ((AppPolicyViewHolder)holder).laiyuan.setText(mDatas.data.get(section).original);
                ((AppPolicyViewHolder)holder).textView.setText(mDatas.data.get(section).title);
                ((AppPolicyViewHolder)holder).summary.setText(mDatas.data.get(section).summary);
                ((AppPolicyViewHolder)holder).time.setText(mDatas.data.get(section).publish_date);
                ImageLoader.getInstance().displayImage(mDatas.data.get(section).logo_path, ((AppPolicyViewHolder)holder).iv);
                ((AppPolicyViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
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
        void onitemClice(ZhengCeFile.DataBean data);
    }
}
