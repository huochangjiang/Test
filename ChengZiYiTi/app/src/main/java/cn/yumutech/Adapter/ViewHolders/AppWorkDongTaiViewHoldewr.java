package cn.yumutech.Adapter.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/10.
 */
public class AppWorkDongTaiViewHoldewr  extends RecyclerView.ViewHolder{
    public TextView textView;
    public  TextView summary;
    public  TextView laiyuan;
    public  TextView time;
    public ImageView iv;
    public AppWorkDongTaiViewHoldewr(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.tv1);
        summary = (TextView) itemView.findViewById(R.id.sumary);
        laiyuan = (TextView) itemView.findViewById(R.id.laiyuan);
        time = (TextView) itemView.findViewById(R.id.tv_time);
        iv = (ImageView) itemView.findViewById(R.id.iv);
    }
}
