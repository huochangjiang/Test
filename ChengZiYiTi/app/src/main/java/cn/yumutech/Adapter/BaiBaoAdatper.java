package cn.yumutech.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yumutech.bean.BaiBao;
import cn.yumutech.unity.R;


public class BaiBaoAdatper extends BaseAdapter{

	private List<BaiBao> mBaibaos;
	private Context context;
	private int mCount=-1;
	public BaiBaoAdatper(Context conte, ArrayList<BaiBao> baibaos){
		this.mBaibaos=baibaos;
		this.context=conte;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mBaibaos.size();
	}
	public void dataChange(ArrayList<BaiBao> baibaos,int count){
		this.mCount=count;
		this.mBaibaos=baibaos;
		notifyDataSetChanged();
	}

	@Override
	public BaiBao getItem(int position) {
		// TODO Auto-generated method stub
		return mBaibaos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		View view=convertView;
		if(view==null){
			vh=new ViewHolder();
			view=View.inflate(context, R.layout.baibaoitem, null);
			vh.iv=(ImageView) view.findViewById(R.id.iv);
			vh.tv=(TextView) view.findViewById(R.id.tv);
			view.setTag(vh);
		}else{
			vh=(ViewHolder) view.getTag();
		}
		if(position==4) {
			if(mCount!=-1) {
				vh.tv.setText(mCount+"");
			}
		}
		vh.iv.setImageResource(mBaibaos.get(position).getId());
		return view;
	}
class ViewHolder{

	public ImageView iv;
	public TextView tv;

  }
}
