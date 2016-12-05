package cn.yumutech.weight;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.yumutech.unity.R;


public class SignOutDilog1 implements  OnClickListener{
	public Dialog mDialog;
	private Activity mContext;
	private EditText et;

	public SignOutDilog1(Activity context, String message) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.tuichu_dilg1, null);
		mDialog = new Dialog(context, R.style.dialog);
		mContext = context;
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		initView(view,message);
		mContext=context;
	}
	private Context con;
	public SignOutDilog1(Context context, String message) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.tuichu_dilg1, null);
		mDialog = new Dialog(context, R.style.dialog);
		con = context;
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		initView(view,message);
		con=context;
	}
	private void initView(View view,String message) {
		Button queren = (Button) view.findViewById(R.id.queren);
		Button quxiao = (Button) view.findViewById(R.id.quxiao);
		tishi = (TextView) view.findViewById(R.id.tishi);
		et = (EditText) view.findViewById(R.id.et);
		if(message.equals("讨论组名字")){
			et.setHint("请输入");
			tishi.setText(message);
		}else{
			et.setText(message);
			tishi.setText("修改讨论组名字");
		}
		queren.setOnClickListener(this);
		quxiao.setOnClickListener(this);
		
	}

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

	int index;
	private TextView tishi;
	private onListern1 onlistern;
	public void setOnLisener(onListern1 onliListern){
		this.onlistern=onliListern;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.queren:
if(et.getText().toString().trim()==null||et.getText().toString().trim().equals("")){
	Toast.makeText(mContext, "请输入讨论组名字", Toast.LENGTH_SHORT).show();
	return;
}
				if(onlistern!=null)
				onlistern.send(et.getText().toString().trim());
				dismiss();
			break;
		case R.id.quxiao:
			dismiss();
			break;
		}
	}
	
	public interface onListern1{
		void send(String name);
	}
}
