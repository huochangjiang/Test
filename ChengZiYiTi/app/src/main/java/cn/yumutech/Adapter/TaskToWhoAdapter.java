package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.bean.UserBean;
import cn.yumutech.unity.R;
import cn.yumutech.weight.Yuanxing;

/**
 * Created by Administrator on 2016/11/22.
 */
public class TaskToWhoAdapter extends BaseAdapter{
    private Context context;
    private List<UserAboutPerson.DataBean> mData;
    private boolean isSelector;
    public static Map<Integer,UserAboutPerson.DataBean> maps=new HashMap<>();
    public TaskToWhoAdapter(Context context,List<UserAboutPerson.DataBean> mData){
        this.context=context;
        this.mData=mData;
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
            vh.touxiang= (Yuanxing) myView.findViewById(R.id.touxiang);
            vh.cb= (CheckBox) myView.findViewById(R.id.cb);
            vh.employees= (TextView) myView.findViewById(R.id.employees);
            vh.selecte= (ImageView) myView.findViewById(R.id.selecte);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) myView.getTag();
        }
        ImageLoader.getInstance().displayImage(mData.get(position).logo_path,vh.touxiang);
        vh.employees.setText(mData.get(position).nickname);
//        if(mData.get(index).type == UserBean.TYPE_CHECKED){
////            vh.selecte.setChecked(true);
//            vh.selecte.setImageResource(R.drawable.story_selector);
//        }else{
//            vh.selecte.setImageResource(R.drawable.story_wei);
//        }
        maps.clear();
        if(mData.get(index).type == UserBean.TYPE_CHECKED){
            vh.cb.setChecked(true);
        }else{
            vh.cb.setChecked(false);
        }
//        myView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               if(isSelector){
//                   vh.selecte.setImageResource(R.drawable.story_wei);
//                   isSelector=false;
//               }else {
//                   vh.selecte.setImageResource(R.drawable.story_selector);
//                   isSelector=true;
//               }
//
//                if(mData.get(index).type==UserBean.TYPE_CHECKED){
//                    mData.get(index).type=UserBean.TYPE_NOCHECKED;
//                    if(ids!=null){
//                        if(maps!=null&&maps.size()>0) {
//                            maps.remove(index);
//                            ids.getMenmberIds(maps);
//                        }
//                    }
//                }else{
//                    mData.get(index).type=UserBean.TYPE_CHECKED;
//                    if(ids!=null){
//
//                        maps.put(index,mData.get(index));
//                        ids.getMenmberIds(maps);
//                    }
//                }
//            }
//        });
        vh.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mData.get(index).type==UserBean.TYPE_CHECKED){
                    mData.get(index).type=UserBean.TYPE_NOCHECKED;
                    if(ids!=null){
                        if(maps!=null&&maps.size()>0) {
                            maps.remove(index);
                            ids.getMenmberIds(maps);
                        }
                    }
                }else{
                    mData.get(index).type=UserBean.TYPE_CHECKED;
                    if(ids!=null){
                        maps.put(index,mData.get(index));
                        if(maps.size()>2){
                            maps.remove(0);
                        }
                        ids.getMenmberIds(maps);
                    }
                }
            }
        });

        return myView;
    }

    public class ViewHolder{
        public Yuanxing touxiang;
        public TextView employees;
        public ImageView selecte;
        public CheckBox cb;
    }
    public interface getIds{
        void getMenmberIds(Map<Integer,UserAboutPerson.DataBean> beans);
    }
}
