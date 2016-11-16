package cn.yumutech.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yumutech.bean.ExchangeCommenList;
import cn.yumutech.unity.R;
import cn.yumutech.unity.ReplyToCommentActivity;
import io.rong.imageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/11/14.
 */
public class TaShanCommentAdapter extends BaseAdapter{
    private Context context;
    private List<ExchangeCommenList.data> data;

    public TaShanCommentAdapter(Context context, List<ExchangeCommenList.data> data){
        this.context=context;
        this.data=data;
    }
    @Override
    public int getCount() {
        return data!=null&&data!=null&&data.size()>0?data.size():0;
    }

    public void dataChange(List<ExchangeCommenList.data> data){
        this.data=data;
        notifyDataSetChanged();
    }
    @Override
    public List<ExchangeCommenList.data> getItem(int i) {
        return data;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View myView =view;
        ViewHolder vh;
        if(myView==null){
            vh=new ViewHolder();
            myView=View.inflate(context, R.layout.tashan_comments_item,null);
            vh.details= (TextView) myView.findViewById(R.id.details);
            vh.time= (TextView) myView.findViewById(R.id.user);
            vh.user= (TextView) myView.findViewById(R.id.time);
            vh.commentsnum= (TextView) myView.findViewById(R.id.commentsnum);
            vh.touxiang= (ImageView) myView.findViewById(R.id.touxiang);
            vh.xian=myView.findViewById(R.id.xian);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) myView.getTag();
        }

        vh.details.setText(data.get(i).content);
        vh.time.setText(data.get(i).publish_user_name);
        vh.user.setText(data.get(i).publish_date);
      //  vh.commentsnum.setText(data.data.get(i).comment_count);
        ImageLoader.getInstance().displayImage(data.get(i).publish_user_logo_path,vh.touxiang);
        if(i==data.size()-1){
            vh.xian.setVisibility(View.GONE);
        }
        vh.commentsnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(context, ReplyToCommentActivity.class);
                intent.putExtra("commentId",data.get(i).comment_id);
                context.startActivity(intent);
            }
        });
        return myView;
    }
    public class ViewHolder{
        public TextView details,user,time,commentsnum;
        public ImageView touxiang;
        public View xian;
    }
}
