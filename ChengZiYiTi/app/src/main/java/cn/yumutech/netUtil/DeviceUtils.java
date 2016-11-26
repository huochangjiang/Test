package cn.yumutech.netUtil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 获取设备的基本信息
 * 
 * @author liuyang
 *
 */
public class DeviceUtils {

	/**
	 * 获取本机ip
	 * 
	 * @return
	 */
	public static String getLocalIPAddress() {

		String ip = "";

		try {

			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& (inetAddress instanceof Inet4Address)) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}

		} catch (Exception e) {

			ip = "";
		}

		return ip;
	}

	/**
	 * 获取机器imei设备号
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		String imei = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId()
				+ "";

		return imei;
	}

	/**
	 * 获取app版本信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getappVersion(Context context) {

		String appVersion = "0.0.0";

		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			appVersion = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return appVersion;

	}

	/**
	 * 获取app版本号
	 * 
	 * @param context
	 * @return
	 */
	/**
	 * 获取apk的版本号 currentVersionCode
	 *
	 * @param ctx
	 * @return
	 */
	public static int getAPPVersionCodeFromAPP(Context ctx) {
		int currentVersionCode = 0;
		PackageManager manager = ctx.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
			String appVersionName = info.versionName; // 版本名
			currentVersionCode = info.versionCode; // 版本号
			System.out.println(currentVersionCode + " " + appVersionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch blockd
			e.printStackTrace();
		}
		return currentVersionCode;
	}

	/**
	 * 获取android的版本号
	 * 
	 * @return
	 */
	public static String getandroidVersion() {
		String androidVersion = Build.VERSION.RELEASE + "";

		return androidVersion;
	}
}
