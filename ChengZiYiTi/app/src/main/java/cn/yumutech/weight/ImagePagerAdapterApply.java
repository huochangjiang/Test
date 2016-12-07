/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package cn.yumutech.weight;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import cn.yumutech.bean.LeaderActivitys;
import cn.yumutech.unity.LeadersDetaislActivity;
import cn.yumutech.unity.R;

/**
 * @Description: 图片适配器
 * @author http://blog.csdn.net/finddreams
 */
public class ImagePagerAdapterApply extends BaseAdapter {
	public static final String IMAGEURL="http://image.xgimi.com";
	private Context context;
	private int size;
	private boolean isInfiniteLoop;
	private DisplayImageOptions options;
	private LeaderActivitys film;
   private ScrollView scrool;
	private final DisplayImageOptions options1;

	public ImagePagerAdapterApply(Context context, LeaderActivitys film, ScrollView myscro) {
		this.scrool=myscro;
		this.context = context;
		this.film = film;
		if (film != null) {
			this.size = film.data.size();
		}
		isInfiniteLoop = false;
		// .showImageOnLoading(R.drawable.icon_default)
// .showImageOnLoading(LayoutToDrawable());
//				.showImageOnLoading(new BitmapDrawable(convertViewToBitmap()))
// .cacheOnDisc(true)
// .considerExifParams(true)
		options1 = new DisplayImageOptions.Builder()
				// .showImageOnLoading(R.drawable.icon_default)
				.showImageOnLoading(R.drawable.jiazaizhong)
				.showImageForEmptyUri(R.drawable.jiazaizhong).showImageOnFail(R.drawable.jiazaizhong)
				// .showImageOnLoading(LayoutToDrawable());
//				.showImageOnLoading(new BitmapDrawable(convertViewToBitmap()))
				.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				// .cacheOnDisc(true)
				// .considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(10))
				.build();
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : film.data.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view=View.inflate(context, R.layout.viewflowertext,null);

			holder.tv= (TextView) view.findViewById(R.id.tv);
			holder.imageView= (ImageView) view.findViewById(R.id.iv);
//			view = holder.imageView = new ImageView(context);
//			holder.imageView
//					.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
//			holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		ImageLoader.getInstance().displayImage(
				film.data.get(getPosition(position)).logo_path,
				holder.imageView,options1);
		holder.tv.setText(film.data.get(getPosition(position)).title);
		holder.imageView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					scrool.requestDisallowInterceptTouchEvent(true);
					break;
				case MotionEvent.ACTION_UP:
					scrool.requestDisallowInterceptTouchEvent(false);
					
					break;

				default:
					break;
				}
				return false;
			}
		});
		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//跳转

				Intent intent=new Intent(context,LeadersDetaislActivity.class);
				intent.putExtra("id",film.data.get(getPosition(position)).id);
				intent.putExtra("type","1");
				context.startActivity(intent);
			}
		});

		return view;
	}

	private static class ViewHolder {

		ImageView imageView;
		TextView tv;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapterApply setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

}
