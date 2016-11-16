package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.yumutech.bean.ExchangeCommenList;
import cn.yumutech.unity.R;
import cn.yumutech.weight.MyEditText;
import io.rong.imageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ReplyToCommentAdapter extends BaseAdapter{
    private Context context;
    private List<ExchangeCommenList.data> mData;
    public ReplyToCommentAdapter(Context context,List<ExchangeCommenList.data> mData){
        this.context=context;
        this.mData=mData;
    }
    @Override
    public int getCount() {
        return mData!=null&&mData!=null&&mData.size()>0?mData.size():0;
    }
    public void dataChange(List<ExchangeCommenList.data> data){
        this.mData=data;
        notifyDataSetChanged();
    }
    @Override
    public List<ExchangeCommenList.data> getItem(int position) {
        return mData;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView =convertView;
        final ViewHolder vh;
        if(myView==null){
            vh=new ViewHolder();
            myView=View.inflate(context, R.layout.reply_to_comment_item,null);
            vh.details= (TextView) myView.findViewById(R.id.details);
            vh.admin= (TextView) myView.findViewById(R.id.admin);
            vh.unity= (TextView) myView.findViewById(R.id.unity);
            vh.time= (TextView) myView.findViewById(R.id.time);
            vh.onclik_reply= (TextView) myView.findViewById(R.id.onclik_reply);
            vh.touxiang= (ImageView) myView.findViewById(R.id.touxiang);
            vh.shurukuang= (RelativeLayout) myView.findViewById(R.id.shurukuang);
            vh.edit= (MyEditText) myView.findViewById(R.id.edit);
            vh.send= (TextView) myView.findViewById(R.id.send);
            vh.xian=myView.findViewById(R.id.xian);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) myView.getTag();
        }

        vh.details.setText(mData.get(position).content);
        vh.time.setText(mData.get(position).publish_date);
        vh.admin.setText(mData.get(position).publish_user_name);
        vh.unity.setText(mData.get(position).receiver_user_name);
        //  vh.commentsnum.setText(data.data.get(i).comment_count);
        ImageLoader.getInstance().displayImage(mData.get(position).publish_user_logo_path,vh.touxiang);
        if(position==mData.size()-1){
            vh.xian.setVisibility(View.GONE);
        }
        vh.onclik_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vh.shurukuang.setVisibility(View.VISIBLE);
                //弹出软键盘
                InputMethodManager inputManager =
                        (InputMethodManager)vh.onclik_reply.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);

            }
        });
        return myView;
    }
    public class ViewHolder{
        public TextView details,admin,time,unity,onclik_reply;
        public ImageView touxiang;
        public View xian;
        private RelativeLayout shurukuang;
        private MyEditText edit;
        private TextView send;
    }
}
