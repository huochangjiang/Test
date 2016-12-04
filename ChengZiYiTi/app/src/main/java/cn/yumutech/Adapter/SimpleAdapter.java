package cn.yumutech.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import cn.yumutech.weight.SaveData;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

public class SimpleAdapter extends PagerAdapter {

//	private static final int[] sDrawables = { R.drawable.wallpaper,
//			R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper,
//			R.drawable.wallpaper, R.drawable.wallpaper };

    private ArrayList<String> sDrawables;
    private Dialog acti;
    private Context context;

    private ImageView baocun;
    private ImageView photoView;
    public SimpleAdapter(ArrayList<String> sds,Dialog activity,Context context){
        this.sDrawables=sds;
        this.acti=activity;
        this.context=context;
    }


    @Override
    public int getCount() {
        return sDrawables.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View view=container;
//        view=View.inflate(context, R.layout.simpleadapter_item,null);
         PhotoView photoView = new PhotoView(container.getContext());
//        photoView= (ImageView) view.findViewById(R.id.image);
//		photoView.setImageResource(sDrawables[position]);
//        baocun= (ImageView) view.findViewById(R.id.baocun);
        ImageLoader.getInstance().displayImage(sDrawables.get(position), photoView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                SaveData.getInstance().mybt.add(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });





        PhotoViewAttacher attacher=new PhotoViewAttacher(photoView);

        attacher.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                // TODO Auto-generated method stub
                acti.dismiss();
            }
        });
        attacher.setOnViewTapListener(new OnViewTapListener() {

            @Override
            public void onViewTap(View view, float x, float y) {
                // TODO Auto-generated method stub
                acti.dismiss();

            }
        });
        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        return photoView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    //保存图片
//    public static void saveBitmap(View view, String filePath){
//
//        // 创建对应大小的bitmap
//        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
//                Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);
//        view.draw(canvas);
//
//        //存储
//        FileOutputStream outStream = null;
//        File file=new File(filePath);
////		if(file.isDirectory()){//如果是目录不允许保存
////			return;
////		}
//        try {
//            outStream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//            outStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                bitmap.recycle();
//                if(outStream!=null){
//                    outStream.close();
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
