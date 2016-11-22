package cn.yumutech.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/22.
 */
public class CompleteAdapter extends BaseAdapter{

    private Context mContext;
    private List<Bitmap> mDatas;
    public CompleteAdapter(Context context,List<Bitmap> data){
        this.mContext=context;
        this.mDatas=data;
    }
public void dataChange(List<Bitmap> data){
    this.mDatas=data;
    notifyDataSetChanged();
}

    @Override
    public int getCount() {
        return mDatas.size()+1;
    }

    @Override
    public Bitmap getItem(int i) {
        return mDatas.get(i);
    }
    setOnItemClic onItemClic;
    public void setLisener(setOnItemClic lisener){
        this.onItemClic=lisener;

    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolders vh;
        if(view==null){
            vh=new ViewHolders();
            view=View.inflate(mContext, R.layout.complet_item,null);
            vh.iv= (ImageView) view.findViewById(R.id.iv);
            view.setTag(vh);
        }else{
            vh= (ViewHolders) view.getTag();
        }
        if(i==mDatas.size()){
            vh.iv.setImageResource(R.drawable.touxiao);
        }else {
            vh.iv.setImageBitmap(mDatas.get(i));
        }

        return view;
    }
    public class ViewHolders{
        public ImageView iv;
    }

    public interface setOnItemClic{
        void getOnitem(int postion);
    }
}
