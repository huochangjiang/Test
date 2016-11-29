package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yumutech.bean.UserAboutPerson;
import cn.yumutech.bean.UserBean;
import cn.yumutech.unity.App;
import cn.yumutech.unity.R;
import de.greenrobot.event.EventBus;

/**
 * Created by 霍长江 on 2016/11/16.
 */
public class MyMenmberAdapter extends BaseAdapter{

    private List<UserAboutPerson.DataBean> mDatas;
    private Context mContext;
    private int type;
    private Map<Integer,UserAboutPerson.DataBean> maps=new HashMap<>();
    public MyMenmberAdapter(List<UserAboutPerson.DataBean> data,Context context){
        this.mDatas=data;
        this.mContext=context;
    }
    private boolean isXianShi;

    public void dataChange(List<UserAboutPerson.DataBean> data,boolean si){
        this.isXianShi=si;
        this.mDatas=data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
    getIds ids;
    public void setLisener(getIds id){
        this.ids=id;
    }
    @Override
    public UserAboutPerson.DataBean getItem(int position) {
        return mDatas.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      final  ViewHolder vh;
        final int index=position;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.member_item,null);
            vh.cb= (ImageView) convertView.findViewById(R.id.cb);
            vh.tv_phone= (TextView) convertView.findViewById(R.id.tv_phone);
            vh.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            vh.iv= (ImageView) convertView.findViewById(R.id.iv_phone);

            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        if(isXianShi){
            vh.tv_phone.setText(mDatas.get(position).mobile);

        }else{
            vh.tv_phone.setVisibility(View.GONE);
        }
        vh.tv_name.setText(mDatas.get(position).nickname);
        ImageLoader.getInstance().displayImage(mDatas.get(position).logo_path,vh.iv);
        if(mDatas.get(index).type == UserBean.TYPE_CHECKED){
            vh.cb.setImageResource(R.drawable.story_selector);
        }else{
            vh.cb.setImageResource(R.drawable.story_wei);
        }
        if(mDatas.get(index).id.equals(App.getContext().getLogo("logo").data.id)){
            vh.cb.setImageResource(R.drawable.story_selector);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mDatas.get(index).id.equals(App.getContext().getLogo("logo").data.id)){
                if(mDatas.get(index).type==UserBean.TYPE_CHECKED){
                    mDatas.get(index).type=UserBean.TYPE_NOCHECKED;
                    if(ids!=null){
                        if(maps!=null&&maps.size()>0) {
                            maps.remove(index);
                            vh.cb.setImageResource(R.drawable.story_wei);
                            ids.getMenmberIds(maps);
                            mDatas.get(index).setCance(true);
                            EventBus.getDefault().post(mDatas.get(index));
                        }
                    }
                }else {
                    mDatas.get(index).type = UserBean.TYPE_CHECKED;
                    if (ids != null) {
                        vh.cb.setImageResource(R.drawable.story_selector);
                        maps.put(index, mDatas.get(index));
                        ids.getMenmberIds(maps);
                        mDatas.get(index).setCance(false);

                        EventBus.getDefault().post(mDatas.get(index));
                    }
                }
                }
            }
        });
        return convertView;
    }
    public class ViewHolder{
        public ImageView cb;
        public ImageView iv;
        public TextView tv_phone;
        public TextView tv_name;
    }
    public interface getIds{
        void getMenmberIds(Map<Integer,UserAboutPerson.DataBean> beans);
    }

}
