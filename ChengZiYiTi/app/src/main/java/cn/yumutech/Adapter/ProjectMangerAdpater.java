package cn.yumutech.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import cn.yumutech.Adapter.ViewHolders.ApplyFooterViewHolder;
import cn.yumutech.Adapter.ViewHolders.ApplyHeaderItemViewHolder;
import cn.yumutech.Adapter.ViewHolders.ProjectItemViewHolder;
import cn.yumutech.bean.ProjectManger;
import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/10.
 */
public class ProjectMangerAdpater extends SectionedRecyclerViewAdapter<ApplyHeaderItemViewHolder,
        ProjectItemViewHolder,
        ApplyFooterViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ProjectManger mDatas;
    OnitemClick onitemClick;
    public void dataChange(ProjectManger data){
        this.mDatas=data;
        notifyDataSetChanged();
    }
    public ProjectMangerAdpater(Context context,ProjectManger data){
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
    protected ProjectItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.project_manger_item, parent, false);
        return new ProjectItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(ApplyHeaderItemViewHolder holder, int section) {

    }

    @Override
    protected void onBindSectionFooterViewHolder(ApplyFooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(ProjectItemViewHolder holder, final int section,final int position) {
        if(holder instanceof ProjectItemViewHolder) {
            if (mDatas != null && mDatas.data != null && mDatas.data.size() > 0) {
                ((ProjectItemViewHolder) holder).laiyuan.setText(mDatas.data.get(section).original);
                ((ProjectItemViewHolder) holder).textView.setText(mDatas.data.get(section).title);
                ((ProjectItemViewHolder) holder).summary.setText(mDatas.data.get(section).summary);
                ((ProjectItemViewHolder) holder).time.setText(mDatas.data.get(section).publish_date);
                if(mDatas.data.get(section).amount.equals("")) {
                    ((ProjectItemViewHolder) holder).money.setText("项目金额:"+"0元");
                }else{
                    ((ProjectItemViewHolder) holder).money.setText("项目金额:"+mDatas.data.get(section).amount+"元");
                }
                ((ProjectItemViewHolder) holder).guifua.setText("类型:"+mDatas.data.get(section).type);
                ImageLoader.getInstance().displayImage(mDatas.data.get(section).logo_path, ((ProjectItemViewHolder) holder).iv);
                ((ProjectItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onitemClick != null) {
                            onitemClick.onitemClice(mDatas.data.get(section));
                        }
                    }
                });
            }
        }
    }

    public interface OnitemClick{
        void onitemClice(ProjectManger.DataBean data);
    }
}
