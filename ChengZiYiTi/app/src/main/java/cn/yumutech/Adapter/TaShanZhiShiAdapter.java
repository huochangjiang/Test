package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.yumutech.bean.HuDongJIaoLiu;
import cn.yumutech.unity.R;

/**
 * Created by Allen on 2016/11/13.
 */
public class TaShanZhiShiAdapter extends BaseAdapter{
    private Context context;
    private HuDongJIaoLiu data;

    public TaShanZhiShiAdapter(Context context, HuDongJIaoLiu data){
        this.context=context;
        this.data=data;
    }
    @Override
    public int getCount() {
        return data!=null&&data.data!=null&&data.data.size()>0?data.data.size():0;
    }

    public void dataChange(HuDongJIaoLiu data){
        this.data=data;
        notifyDataSetChanged();
    }
    @Override
    public HuDongJIaoLiu getItem(int i) {
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
            myView=View.inflate(context, R.layout.tashanzhishi_item,null);
            vh.title= (TextView) myView.findViewById(R.id.title);
            vh.details= (TextView) myView.findViewById(R.id.details);
            vh.myclass= (TextView) myView.findViewById(R.id.myclass);
            vh.time= (TextView) myView.findViewById(R.id.time);
            vh.tv_source= (TextView) myView.findViewById(R.id.tv_source);
            vh.browse_num= (TextView) myView.findViewById(R.id.browse_num);
            vh.comments_num= (TextView) myView.findViewById(R.id.comments_num);
            vh.image= (ImageView) myView.findViewById(R.id.image);
            vh.xian=myView.findViewById(R.id.xian);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) view.getTag();
        }
        vh.title.setText(data.data.get(i).title);
        vh.details.setText(data.data.get(i).summary);
        vh.myclass.setText(data.data.get(i).classify);
        vh.time.setText(data.data.get(i).publish_date);
        vh.tv_source.setText(data.data.get(i).original);
        vh.browse_num.setText(data.data.get(i).browser_count);
        vh.comments_num.setText(data.data.get(i).comment_count);
        ImageLoader.getInstance().displayImage(data.data.get(i).logo_path,vh.image);
        if(i==data.data.size()-1){
            vh.xian.setVisibility(View.GONE);
        }
        return myView;
    }
    public class ViewHolder{
        public TextView title,details,myclass,time,tv_source,browse_num,comments_num;
        public ImageView image;
        public View xian;
    }
}
