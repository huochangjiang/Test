package cn.yumutech.unity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.yumutech.weight.Util;


@ContentView(R.layout.activity_date_pick)
public class DatePickActivity extends Activity {

	@ViewInject(R.id.date_picker)
	private DatePicker datePicker;

//	@ViewInject(R.id.time_picker)
//	private TimePicker timePicker;

	private String datestrold;
	private String datestr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		init();
	}

	/**
	 * 数据初始化
	 */
	private void init() {
		datePicker.setCalendarViewShown(false);
//		timePicker.setIs24HourView(true);
		resizePikcer(datePicker);// 调整datepicker大小
//		resizePikcer(timePicker);// 调整timepicker大小
		String str = getIntent().getStringExtra("date");
		if (TextUtils.isEmpty(str)) {
			System.out.println("isempty");
			datestrold = "";
			datestr = "";
		} else {
			datestr = str;
			datestrold = str;
		}

	}

	
	/**
	 * 实现onTouchEvent触屏函数但点击屏幕时
	 * 
	 * @param v
	 */
	@OnClick(R.id.repair_date_other)
	public void exit(View v) {
		back(false);
	}
	
	/**
	 * 调整FrameLayout大小
	 * 
	 * @param tp
	 */
	private void resizePikcer(FrameLayout tp) {
		List<NumberPicker> npList = findNumberPicker(tp);
		for (NumberPicker np : npList) {
			resizeNumberPicker(np);
		}
	}

	/*
	 * 调整numberpicker大小
	 */
	private void resizeNumberPicker(NumberPicker np) {
		LayoutParams params = new LayoutParams(Util.dip2px(this, 45),
				LayoutParams.WRAP_CONTENT);
		params.setMargins(Util.dip2px(this, 5), 0, Util.dip2px(this, 5), 0);
		np.setLayoutParams(params);

	}

	/**
	 * 得到viewGroup里面的numberpicker组件
	 * 
	 * @param viewGroup
	 * @return
	 */
	private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
		List<NumberPicker> npList = new ArrayList<NumberPicker>();
		View child = null;
		if (null != viewGroup) {
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				child = viewGroup.getChildAt(i);
				if (child instanceof NumberPicker) {
					npList.add((NumberPicker) child);
				} else if (child instanceof LinearLayout) {
					List<NumberPicker> result = findNumberPicker((ViewGroup) child);
					if (result.size() > 0) {
						return result;
					}
				}
			}
		}
		return npList;
	}

	/**
	 * 点击取消
	 * 
	 * @param v
	 */
	@OnClick(R.id.repair_date_sel_cancel)
	public void cancel(View v) {
		back(true);
	}
	
	/**
	 * 点击确定
	 * 
	 * @param v
	 */
	@OnClick(R.id.repair_date_sel_ok)
	public void ok(View v) {
		back(false);
	}

	/**
	 * 处理返回按键
	 */
	@Override
	public void onBackPressed() {
		back(true);
		super.onBackPressed();
	}

	/**
	 * 关闭调用 old为true则不变，false则改变
	 * 
	 * @param
	 */
	private void back(boolean old) {
		// 获取时间选择
		Intent intent = new Intent();
		if (old) {
			intent.putExtra("date", datestrold);
		} else {
			datestr = getData();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = sdf.parse(datestr);
				if (!compare(date))
					return;
				intent.putExtra("date", datestr);
				setResult(Activity.RESULT_OK, intent);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		finish();
	}

	// 比较时间
	private boolean compare(Date dt1) {
		Date curDate = new Date(System.currentTimeMillis());
		if (dt1.getTime() > curDate.getTime()) {
			System.out.println("截止时间大于现在的时间");
			return true;
		} else if (dt1.getTime() < curDate.getTime()) {
			Util.showToast(this, "截止时间必须大于当前时间");
			return false;
		} else {// 相等
			System.out.println("相等");
			return false;
		}
	}

	private String getData() {
		StringBuilder str = new StringBuilder().append(datePicker.getYear()).append("-")
				.append((datePicker.getMonth() + 1) < 10 ? "0" + (datePicker.getMonth() + 1)
						: (datePicker.getMonth() + 1))
				.append("-")
				.append((datePicker.getDayOfMonth() < 10) ? "0" + datePicker.getDayOfMonth()
						: datePicker.getDayOfMonth())
				.append(" ");
//				.append((timePicker.getCurrentHour() < 10) ? "0" + timePicker.getCurrentHour()
//						: timePicker.getCurrentHour())
//				.append(":").append((timePicker.getCurrentMinute() < 10) ? "0" + timePicker.getCurrentMinute()
//						: timePicker.getCurrentMinute());

		return str.toString();
	}
}
