package cn.yumutech.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.bean.UserBean;
import cn.yumutech.unity.R;
import cn.yumutech.weight.SaveData;

/**
 * Created by Administrator on 2016/11/22.
 */
public class TaskToWhoAdapter extends BaseAdapter{
    private Context context;
    private List<UserAboutPerson.DataBean> mData;
    private boolean isSelector;
    private final DisplayImageOptions options;

    public static Map<Integer,UserAboutPerson.DataBean> maps=new HashMap<>();
    public static Map<Integer,UserAboutPerson.DataBean> mapsbeen=new HashMap<>();
    public TaskToWhoAdapter(Context context,List<UserAboutPerson.DataBean> mData){
        this.context=context;
        this.mData=mData;
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
                .displayer(new RoundedBitmapDisplayer(10))

                .build();
    }
    public void dataChange(List<UserAboutPerson.DataBean> mData){
        this.mData=mData;
        notifyDataSetChanged();
    }
    getIds ids;
    public void setLisener(getIds id){
        this.ids=id;
    }
    @Override
    public int getCount() {
        return mData!=null&&mData.size()>0?mData.size():0;
    }

    @Override
    public List<UserAboutPerson.DataBean> getItem(int position) {
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
        final int index=position;
        if(myView==null) {
            vh = new ViewHolder();
            myView=View.inflate(context, R.layout.task_to_who_item,null);
            vh.touxiang= (ImageView) myView.findViewById(R.id.touxiang);
            vh.tv_phone= (TextView) myView.findViewById(R.id.tv_phone);
            vh.employees= (TextView) myView.findViewById(R.id.employees);
            vh.selecte= (ImageView) myView.findViewById(R.id.selecte);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) myView.getTag();
        }
        ImageLoader.getInstance().displayImage(mData.get(position).logo_path,vh.touxiang,options);
        vh.employees.setText(mData.get(position).nickname);
        //如果没权限直接把可以选和部门影藏
        if(SaveData.getInstance().isPermissions){
            vh.tv_phone.setVisibility(View.VISIBLE);
            vh.tv_phone.setText(mData.get(position).mobile);
        }else {
            vh.tv_phone.setVisibility(View.GONE);
        }
        if(mData.get(index).type == UserBean.TYPE_CHECKED&&SaveData.getInstance().isPermissions){
            vh.selecte.setImageResource(R.drawable.story_selector);
        }else{
            vh.selecte.setImageResource(R.drawable.story_wei);
        }
        maps.clear();
        mapsbeen.clear();
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mData.get(index).type==UserBean.TYPE_CHECKED){
                    mData.get(index).type=UserBean.TYPE_NOCHECKED;
                    if(ids!=null&&SaveData.getInstance().isPermissions){
                        if(maps!=null&&maps.size()>0) {
                            vh.selecte.setImageResource(R.drawable.story_wei);
                            maps.remove(index);
                            mapsbeen.remove(index);
                            mapsbeen.put(index,mData.get(index));
                            ids.getMenmberIds(maps,mapsbeen,false);
                            mapsbeen.remove(index);
                        }
                    }
                }else{
                    mData.get(index).type=UserBean.TYPE_CHECKED;
                    if(ids!=null&&SaveData.getInstance().isPermissions){
                        vh.selecte.setImageResource(R.drawable.story_selector);
                        maps.put(index,mData.get(index));
                        mapsbeen.put(index,mData.get(index));
                        ids.getMenmberIds(maps,mapsbeen,true);
                    }
                }
            }
        });
        return myView;
    }

    public class ViewHolder{
        public ImageView touxiang;
        public TextView employees,tv_phone;
        public ImageView selecte;
    }
    public interface getIds{
        void getMenmberIds(Map<Integer,UserAboutPerson.DataBean> beans,Map<Integer,UserAboutPerson.DataBean> cleanBeen,boolean isAdd);
    }
}
