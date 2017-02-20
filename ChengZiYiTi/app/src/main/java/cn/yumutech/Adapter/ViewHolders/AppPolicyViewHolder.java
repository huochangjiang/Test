package cn.yumutech.Adapter.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/9.
 */
public class AppPolicyViewHolder extends RecyclerView.ViewHolder{
    public TextView textView;
    public  TextView summary;
    public  TextView laiyuan,laiyuan1;
    public  TextView time;
    public ImageView iv;
    public  LinearLayout ll;
    public View view1;

    public AppPolicyViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.tv1);
        summary = (TextView) itemView.findViewById(R.id.sumary);
        laiyuan = (TextView) itemView.findViewById(R.id.laiyuan);
        laiyuan1= (TextView) itemView.findViewById(R.id.laiyuan1);
        time = (TextView) itemView.findViewById(R.id.tv_time);
        view1=itemView.findViewById(R.id.view1);
        iv = (ImageView) itemView.findViewById(R.id.iv);
        ll = (LinearLayout) itemView.findViewById(R.id.ll);
    }
}
