package cn.yumutech;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.yumutech.bean.ShowTaskDetail;
import cn.yumutech.unity.R;

/**
 * Created by Allen on 2016/11/24.
 */
public class LookResultAdapter extends BaseAdapter{
    private Context context;
    private ShowTaskDetail data;

    public LookResultAdapter(Context context, ShowTaskDetail data){
        this.context=context;
        this.data=data;
    }
    @Override
    public int getCount() {
        return data!=null&&data.data!=null&&data.data.task_comment!=null&&data.data.task_comment.photos!=null&&data.data.task_comment.photos.size()>0?data.data.task_comment.photos.size():0;
    }

    public void dataChange(ShowTaskDetail data){
        this.data=data;
        notifyDataSetChanged();
    }
    @Override
    public ShowTaskDetail getItem(int i) {
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
            myView=View.inflate(context, R.layout.complete_grid_item,null);
            vh.image= (ImageView) myView.findViewById(R.id.image);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) view.getTag();
        }
        if(data!=null&&data.data!=null&&data.data.task_comment!=null&&data.data.task_comment.photos!=null){
            ImageLoader.getInstance().displayImage(data.data.task_comment.photos.get(i).photo_path,vh.image);
        }
        return myView;
    }
    public class ViewHolder{
        public ImageView image;
    }
}
