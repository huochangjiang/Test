package cn.yumutech.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import cn.yumutech.bean.UserXiangGuanQun;
import cn.yumutech.unity.R;

/**
 * Created by Allen on 2016/11/13.
 */
public class GroupListAdapter extends BaseAdapter{
    private Context context;
    private List<UserXiangGuanQun.DataBean> data;
    private final DisplayImageOptions options;

    public GroupListAdapter(Context context, List<UserXiangGuanQun.DataBean> data){
        this.context=context;
        this.data=data;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.touxiao)
                .showImageForEmptyUri(R.drawable.touxiao)
                .showImageOnFail(R.drawable.touxiao)
                // .showImageOnLoading(LayoutToDrawable());
//				.showImageOnLoading(new BitmapDrawable(convertViewToBitmap()))

                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
                // .cacheOnDisc(true)
                // .considerExifParams(true)
                .build();
    }
    @Override
    public int getCount() {
        return data!=null&&data.size()>0?data.size():0;
    }
            public void dataChange( List<UserXiangGuanQun.DataBean> data){
                this.data=data;notifyDataSetChanged();
            }
    @Override
    public UserXiangGuanQun.DataBean getItem(int i) {
        return data.get(i);
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
            myView=View.inflate(context, R.layout.group_list_item,null);
            vh.iv_tou= (ImageView) myView.findViewById(R.id.iv_tou);
            vh.iv_next= (ImageView) myView.findViewById(R.id.iv_next);
            vh.iv_xian=  myView.findViewById(R.id.iv_xian);
            vh.tv_group= (TextView) myView.findViewById(R.id.group);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) view.getTag();
        }
        vh.tv_group.setText(data.get(i).groupName);
//        ImageLoader.getInstance().displayImage(data.get(i).create_user_logo_path,vh.iv_tou,options);
        if(i==data.size()-1){
            vh.iv_xian.setVisibility(View.GONE);
        }
        return myView;



    }
    public class ViewHolder{
        public ImageView iv_tou,iv_next;
        public TextView tv_group;
        public View iv_xian;
    }
}

