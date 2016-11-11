package cn.yumutech.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import cn.yumutech.Adapter.ViewHolders.AppWorkDongTaiViewHoldewr;
import cn.yumutech.Adapter.ViewHolders.ApplyFooterViewHolder;
import cn.yumutech.Adapter.ViewHolders.ApplyHeaderItemViewHolder;
import cn.yumutech.bean.WorkListManger;
import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/10.
 */
public class WorkDongTaiAdapter extends SectionedRecyclerViewAdapter<ApplyHeaderItemViewHolder,
        AppWorkDongTaiViewHoldewr,
        ApplyFooterViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private WorkListManger mDatas;
    OnitemClick onitemClick;
    public void dataChange(WorkListManger data){
        this.mDatas=data;
        notifyDataSetChanged();
    }
    public WorkDongTaiAdapter(Context context,WorkListManger data){
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
    protected AppWorkDongTaiViewHoldewr onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.work_dongtai_item, parent, false);
        return new AppWorkDongTaiViewHoldewr(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(ApplyHeaderItemViewHolder holder, int section) {

    }

    @Override
    protected void onBindSectionFooterViewHolder(ApplyFooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(AppWorkDongTaiViewHoldewr holder, final int section, final int position) {
        if(holder instanceof AppWorkDongTaiViewHoldewr){
            if(mDatas!=null&&mDatas.data!=null&&mDatas.data.size()>0){
                ((AppWorkDongTaiViewHoldewr)holder).laiyuan.setText(mDatas.data.get(section).original);
                ((AppWorkDongTaiViewHoldewr)holder).textView.setText(mDatas.data.get(section).title);
                ((AppWorkDongTaiViewHoldewr)holder).summary.setText(mDatas.data.get(section).summary);
                ((AppWorkDongTaiViewHoldewr)holder).time.setText(mDatas.data.get(section).publish_date);
                ImageLoader.getInstance().displayImage(mDatas.data.get(section).logo_path, ((AppWorkDongTaiViewHoldewr)holder).iv);
                ((AppWorkDongTaiViewHoldewr)holder).itemView.setOnClickListener(new View.OnClickListener() {
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
        void onitemClice(WorkListManger.DataBean data);
    }
}
