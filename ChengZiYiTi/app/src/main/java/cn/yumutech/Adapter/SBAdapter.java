package cn.yumutech.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.yumutech.bean.ModuleClassifyList;
import cn.yumutech.unity.R;

/**
 * Created by Administrator on 2017/2/15.
 */
public class SBAdapter extends BaseAdapter{
    private Context context;
    private List<ModuleClassifyList.data> mData;
    private int mPosition;
    public SBAdapter(Context context,List<ModuleClassifyList.data> mData){

        this.context=context;
        this.mData=mData;
    }
    public void dataChange(List<ModuleClassifyList.data> mData,int mPosition){
        this.mPosition=mPosition;
        this.mData=mData;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mData!=null&&mData.size()>0?mData.size():0;
    }

    @Override
    public List<ModuleClassifyList.data> getItem(int position) {
        return mData;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View myView =convertView;
        final ViewHolder vh;
        if(myView==null) {
            vh = new ViewHolder();
            myView=View.inflate(context, R.layout.sb_class_item,null);
            vh.textView= (TextView) myView.findViewById(R.id.text);
            vh.rl= (RelativeLayout) myView.findViewById(R.id.rl);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }

       if(mData!=null&&mData.size()>0){
           vh.textView.setText(mData.get(position).value);
       }
        if(mPosition==position){
            vh.rl.setBackground(context.getResources().getDrawable(R.drawable.sb_dian));
            vh.textView.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            vh.rl.setBackground(context.getResources().getDrawable(R.drawable.sb_no));
            vh.textView.setTextColor(context.getResources().getColor(R.color.item_title));
        }
//        vh.rl.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onClick(View v) {
////
////                vh.rl.setBackground(context.getResources().getDrawable(R.drawable.sb_dian));
////                vh.textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
////                changeColor(position);
//                if(position==0){
//                    SaveData.getInstance().fenlei="";
//                }else {
//                    SaveData.getInstance().fenlei=mData.get(position).value;
//                }
//                EventBus.getDefault().post(new ModuleClassifyList());
//            }
//        });
        return myView;
    }
    public class ViewHolder{
        private TextView textView;
        private RelativeLayout rl;
    }
//    List<RelativeLayout> lins=new ArrayList<>();
//    List<TextView> tvs = new ArrayList<>();
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private void changeColor(int position) {
//        for (int i = 0; i < lins.size(); i++) {
//            if (position == i) {
//                lins.get(i).setBackground(context.getResources().getDrawable(R.drawable.sb_dian));
//                tvs.get(i).setTextColor(context.getResources().getColor(R.color.colorPrimary));
////                lins.get(i).setBackground(getResources().getDrawable(R.drawable.zhongdianxuan));
////                tvs.get(i).setBackgroundColor(getResources().getColor(R.color.white));
//            } else {
//                lins.get(i).setBackgroundResource(R.drawable.sb_no);
//                tvs.get(i).setTextColor(context.getResources().getColor(R.color.item_title));
////                lins.get(i).setBackground(getResources().getDrawable(R.drawable.zhongdianwei));
////                tvs.get(i).setBackgroundColor(getResources().getColor(R.color.fengexianone));
//            }
//        }
//    }
}
