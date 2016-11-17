package cn.yumutech.Adapter.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/8.
 */
public class ApplyFooterViewHolder extends RecyclerView.ViewHolder{
    private TextView foot_view_item_tv;
    public ApplyFooterViewHolder(View itemView) {
        super(itemView);

        foot_view_item_tv=(TextView)itemView.findViewById(R.id.foot_view_item_tv);
    }
}
