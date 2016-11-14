package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yumutech.bean.ExchangeCommenList;
import cn.yumutech.unity.R;

/**
 * Created by Administrator on 2016/11/14.
 */
public class TaShanCommentAdapter extends BaseAdapter{
    private Context context;
    private ExchangeCommenList data;

    public TaShanCommentAdapter(Context context, ExchangeCommenList data){
        this.context=context;
        this.data=data;
    }
    @Override
    public int getCount() {
        return data!=null&&data.data!=null&&data.data.size()>0?data.data.size():0;
    }

    public void dataChange(ExchangeCommenList data){
        this.data=data;
        notifyDataSetChanged();
    }
    @Override
    public ExchangeCommenList getItem(int i) {
        return data;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView =view;
        ViewHolder vh;
        if(myView==null){
            vh=new ViewHolder();
            myView=View.inflate(context, R.layout.tashan_comments_item,null);
            vh.details= (TextView) myView.findViewById(R.id.details);
            vh.time= (TextView) myView.findViewById(R.id.user);
            vh.user= (TextView) myView.findViewById(R.id.time);
            vh.commentsnum= (TextView) myView.findViewById(R.id.commentsnum);
            vh.touxiang= (ImageView) myView.findViewById(R.id.image);
            vh.xian=myView.findViewById(R.id.xian);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) view.getTag();
        }

        vh.details.setText(data.data.get(i).content);
        vh.time.setText(data.data.get(i).publish_date);
        vh.user.setText(data.data.get(i).publish_user_name);
      //  vh.commentsnum.setText(data.data.get(i).comment_count);
       // ImageLoader.getInstance().displayImage(data.data.get(i).logo_path,vh.image);
        if(i==data.data.size()-1){
            vh.xian.setVisibility(View.GONE);
        }
        return myView;
    }
    public class ViewHolder{
        public TextView details,user,time,commentsnum;
        public ImageView touxiang;
        public View xian;
    }
}
