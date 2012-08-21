package com.leon.assistivetouch.main;

import java.util.Properties;

import com.leon.assistivetouch.main.system.WebserviceTask;
import com.leon.assistivetouch.main.util.Constan;
import com.leon.assistivetouch.main.util.CrashHandler;
import com.leon.assistivetouch.main.util.L;
import com.leon.assistivetouch.main.util.RootContext;
import com.leon.assistivetouch.main.util.Settings;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

/** 
 * 类名      AssistiveTouchApplication.java
 * 说明   description of the class
 * 创建日期 2012-8-21
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
*/
public class AssistiveTouchApplication extends Application {

	private static final String TAG = "AssistiveTouchApplication";
	private static AssistiveTouchApplication instance;
	
	private static final int NOTIFICATION_ID = 56789065;
	private PendingIntent mUpdateIntent = null;

	@Override
	public void onCreate() {
		super.onCreate();
		L.d(TAG, "AssistiveTouchApplication oncreate");
		instance = this;
		CrashHandler.init(this);

		boolean root = RootContext.hasRootAccess(this);
		RootContext.getInstance();
		Settings.getInstance(this).setRoot(root);
		if (!root) {
			Toast.makeText(this, R.string.no_root_tip, Toast.LENGTH_LONG)
					.show();
		}
		Intent i = new Intent(AssistiveTouchService.ASSISTIVE_TOUCH_START_ACTION);
		startService(i);
		setupAutoUpdate();
	}

	public static AssistiveTouchApplication getInstance() {
		return instance;
	}
	
	/**
	 * 检查更新
	 * */
	public void setupAutoUpdate() {
		L.d(TAG, "setupAutoUpdate");
		if (!Settings.getInstance(this).isEnableAutoUpdate()) {
			L.d(TAG, "Update-checks are disabled!");
			return;
		}
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		if (mUpdateIntent == null) {
			Intent i = new Intent(this, CheckUpdateReceiver.class);
			mUpdateIntent = PendingIntent.getBroadcast(this, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);
		} else {
			am.cancel(mUpdateIntent);
		}
		
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), Constan.CHECK_UPDATE_TIME_LAG, mUpdateIntent);
	}
	
	public void cancelAutoUpdate () {
		L.d(TAG, "cancelAutoUpdate");
		if (mUpdateIntent != null) {
			AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			am.cancel(mUpdateIntent);
		}
	}
	
	public static Message getUpdateMessage (Context context) {
		// 获取 Properties
		Properties updateProperties = WebserviceTask
				.queryForProperty(Constan.APPLICATION_PROPERTIES_URL);
		if (updateProperties != null
				&& updateProperties.containsKey("versionCode")) {

			int availableVersion = Integer.parseInt(updateProperties.getProperty("versionCode"));
			int installedVersion = AssistiveTouchApplication.getVersionNumber(context);
			String fileName = updateProperties.getProperty("fileName", "");
			String updateMessage = updateProperties.getProperty("message", context.getString(R.string.find_update_message));
			String updateTitle = updateProperties.getProperty("title", context.getString(R.string.find_update_title));
			if (availableVersion > installedVersion) {
				L.d(TAG, "安装的版本为:'" 
						+ installedVersion
						+ "' 可更新的版本为: '"
						+ availableVersion + "' 发现更新!");
				Message msg = new Message();
				Bundle bundle = msg.getData();
				bundle.putInt("installedVersion", installedVersion);
				bundle.putInt("availableVersion", availableVersion);
				bundle.putString("url", Constan.APPLICATION_DOWNLOAD_URL + fileName);
				bundle.putString("fileName", fileName);
				bundle.putString("title", updateTitle);
				bundle.putString("message", updateMessage);
				return msg;
			}
		}
		return null;
	}
	
	public static int getVersionNumber(Context context) {
		int version = -1;
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = pi.versionCode;
		} catch (Exception e) {
			L.e(TAG, "Package name not found", e);
		}
		return version;
	}

	public static String getVersionName(Context context) {
		String version = "?";
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo( context.getPackageName(), 0);
			version = pi.versionName;
		} catch (Exception e) {
			L.e(TAG, "Package name not found", e);
		}
		return version;
	}
	
	/**
	 * 显示信息到通知栏
	 * */
	public static void showUpdateNotify (Context context, Message msg) {
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(NOTIFICATION_ID);
    	Bundle bundle = msg.getData();
    	String title = bundle.getString("title");
    	String message = bundle.getString("message");
    	String url = bundle.getString("url");
    	Notification notification = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		notification.setLatestEventInfo(context, title, message, contentIntent);
		nm.notify(NOTIFICATION_ID, notification);
    }
}
