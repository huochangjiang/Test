package cn.yumutech.weight;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.yumutech.unity.R;

/**
 * Created by huo on 2016/11/26.
 */
public class TiShiDilog {
    public Dialog mDialog;
    private Activity mContext;
    public TiShiDilog(Activity context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tishi_dilog, null);
        mDialog = new Dialog(context, R.style.dialog);
        mContext = context;
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        TextView tv= (TextView) view.findViewById(R.id.tishi);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mContext=context;
    }
    private Context con;

    public void show() {
        mDialog.show();
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    public void dismiss() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
            // animationDrawable.stop();
        }
    }

    public boolean isShowing() {
        if (mDialog.isShowing()) {
            return true;
        }
        return false;
    }






}
