package com.leon.assistivetouch.main.util;

import java.lang.Thread.UncaughtExceptionHandler;

import com.leon.assistivetouch.main.AssistiveTouchService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


/** 
 * 类名      CrashHandler.java
 * 说明   捕捉程序异常中止信息
 * 创建日期 2012-8-21
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
*/
public class CrashHandler implements UncaughtExceptionHandler {

	private static final String TAG = "CrashHandler";
	
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private static CrashHandler mInstance;
	private Context mContext;
	
	private CrashHandler() {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	static Object mLock = new Object();

	/** 获取CrashHandler实例 ,单例模式 */
	private static CrashHandler getInstance() {
		synchronized (mLock) {
			if (mInstance == null)
				mInstance = new CrashHandler();
		}
		return mInstance;
	}
	
	public static void init(Context context) {
		getInstance().setContext(context);
	}

	private void setContext(Context context) {
		this.mContext = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		if (!handleException(throwable) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, throwable);
		} else {
			restartApplication();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);
		}
	}

	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		// 生成日志
		L.e(TAG, "", ex);
		return true;
	}
	
	private void restartApplication () {
		// 使用时钟,在1秒后发送请求
		AlarmManager am = (AlarmManager) mContext.getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(AssistiveTouchService.ASSISTIVE_TOUCH_START_ACTION);
		PendingIntent pi = PendingIntent.getService(
				mContext.getApplicationContext(), 0, i,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pi);
	}

	private String getMessageContent () {
		StringBuilder builder = new StringBuilder();
		try {
			PackageManager pm = mContext.getPackageManager();
			PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
			builder.append("----- 应用程序信息 ------").append("\n");
			builder.append("应用程序包名:").append(info.packageName).append("\n");
			builder.append("版本信息:").append(info.versionName).append("\n");
			builder.append("版本号:").append(info.versionCode).append("\n");
			builder.append("安装时间:")
					.append(Util.getFormatDateString(info.firstInstallTime))
					.append("\n");
		} catch (NameNotFoundException e1) {
			L.w(TAG, "", e1);
		}
		try {
			builder.append("\n\n----- 设备信息 ----\n");
			builder.append("PRODUCT ").append(android.os.Build.PRODUCT).append("\n");
			builder.append("BOARD ").append(android.os.Build.BOARD).append("\n");
			builder.append("BOOTLOADER ").append(android.os.Build.BOOTLOADER).append("\n");
			builder.append("BRAND ").append(android.os.Build.BRAND).append("\n");
			builder.append("CPU_ABI ").append(android.os.Build.CPU_ABI).append("\n");
			builder.append("CPU_ABI2 ").append(android.os.Build.CPU_ABI2).append("\n");
			builder.append("DEVICE ").append(android.os.Build.DEVICE).append("\n");
			builder.append("DISPLAY ").append(android.os.Build.DISPLAY).append("\n");
			builder.append("FINGERPRINT ").append(android.os.Build.FINGERPRINT).append("\n");
			builder.append("HARDWARE ").append(android.os.Build.HARDWARE).append("\n");
			builder.append("HOST ").append(android.os.Build.HOST).append("\n");
			builder.append("ID ").append(android.os.Build.ID).append("\n");
			builder.append("MANUFACTURER ").append(android.os.Build.MANUFACTURER).append("\n");
			builder.append("MODEL ").append(android.os.Build.MODEL).append("\n");
			builder.append("PRODUCT ").append(android.os.Build.PRODUCT).append("\n");
			builder.append("RADIO ").append(android.os.Build.RADIO).append("\n");
			builder.append("TAGS ").append(android.os.Build.TAGS).append("\n");
			builder.append("TIME ").append(android.os.Build.TIME).append("\n");
			builder.append("TYPE ").append(android.os.Build.TYPE).append("\n");
			builder.append("USER ").append(android.os.Build.USER).append("\n");
			builder.append("VERSION_CODES.BASE ").append(android.os.Build.VERSION_CODES.BASE).append("\n");
			builder.append("VERSION.RELEASE ").append(android.os.Build.VERSION.RELEASE).append("\n");
			builder.append("SDK").append(android.os.Build.VERSION.SDK).append("\n");
		} catch(Exception e) {
			L.w(TAG, "", e);
		}
		return builder.toString();
	}
}
