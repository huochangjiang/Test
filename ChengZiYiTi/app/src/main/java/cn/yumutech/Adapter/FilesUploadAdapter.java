package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yumutech.unity.R;
import de.greenrobot.event.EventBus;

/**
 * Created by 霍长江 on 2017/1/19.
 */
public class FilesUploadAdapter extends BaseAdapter{
    public Context context;
    public List<String> mDatas;
    public FilesUploadAdapter(Context context,List<String> data){
        this.context=context;
        this.mDatas=data;
    }
    public void dataChange(List<String> data){
        this.mDatas=data;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas!=null?mDatas.size()+1:1;
    }

    @Override
    public String getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        View view=convertView;
        if(view==null){
            vh=new ViewHolder();
            view=View.inflate(context, R.layout.file_adapter,null);
            vh.tv= (TextView) view.findViewById(R.id.tv);
            vh.iv= (ImageView) view.findViewById(R.id.iv);
            view.setTag(vh);
        }else{
            vh= (ViewHolder) view.getTag();
        }

        if(position==mDatas.size()){
            vh.iv.setVisibility(View.GONE);
            vh.tv.setText("添加文件");
        }else{
            vh.tv.setText(getFileName(mDatas.get(position))+"."+getMimeType(mDatas.get(position)));
            vh.iv.setVisibility(View.VISIBLE);
        }
        vh.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new String(position+""));
//                mDatas.remove(getItem(position));
//                dataChange(mDatas);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==mDatas.size()){
                    EventBus.getDefault().post(new String ("bb"));
                }
            }
        });
        return view;
    }
    public static class ViewHolder{
        public TextView tv;
        public ImageView iv;
    }
    //获取文件名称
    public String getFileName(String pathandname) {

        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }

    }
    //获取文件类型
    public static String getMimeType(String fileName) {
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return prefix;
    }
}
