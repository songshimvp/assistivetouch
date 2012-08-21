package com.leon.assistivetouch.main;

import java.util.Random;

import com.leon.assistivetouch.main.ui.TouchView;
import com.leon.assistivetouch.main.util.L;
import com.leon.assistivetouch.main.util.Settings;
import com.leon.assistivetouch.main.util.Util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.RemoteException;

/** 
 * 类名      AssistiveTouchService.java
 * 说明   虚拟按键的服务
 * 创建日期 2012-8-21
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
*/
public class AssistiveTouchService extends Service {

	private static final String TAG = "AssistiveTouchService";
	
	public static final String ASSISTIVE_TOUCH_START_ACTION = "com.leon.assistivetouch.assistive_start_action";
	public static final String ASSISTIVE_TOUCH_STOP_ACTION = "com.leon.assistivetouch.assistive_stop_action";
	
	private static final int NOTIFATION_ID = new Random(System.currentTimeMillis()).nextInt() + 1000; 
	
	private Settings mSetting;
	TouchView mTouchView;
	
	@Override
	public void onCreate() {
		super.onCreate();
		L.d(TAG, "service on create");
		mSetting = Settings.getInstance(this);
		mTouchView = TouchView.getInstance(this);
		L.d(TAG, "id:" + NOTIFATION_ID);
		startForeground(NOTIFATION_ID, new Notification());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		L.d(TAG, "onstart command");
		String action = intent.getAction();
		if (Util.isStringNull(action)) {
			return START_STICKY;
		}
		if (action.equals(ASSISTIVE_TOUCH_START_ACTION)) {
			if (isNeedInit()) {
				startTouchService();
			}
		} else if (action.equals(ASSISTIVE_TOUCH_STOP_ACTION)) {
			stopTouchService();
		}
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		L.d(TAG, "onBind");
		return mBinder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		L.d(TAG, "onUnbind");
		if (!isServiceRunning()) {
			this.stopSelf();
		}
		return true;
	}
	
	@Override
	public void onDestroy() {
		L.d(TAG, "server destory");
		boolean enable = mSetting.isEnableAssistiveTouch();
		if (enable) {
			L.d(TAG, "程序被强制退出!");
			AlarmManager am = (AlarmManager)getApplicationContext()
					.getSystemService(ALARM_SERVICE);
			Intent i = new Intent(ASSISTIVE_TOUCH_START_ACTION);
			PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0,
					i, Intent.FLAG_ACTIVITY_NEW_TASK);
			// 一秒后重启服务
			am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pi);
		}
		super.onDestroy();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private final IAssistiveTouchService.Stub mBinder = new IAssistiveTouchService.Stub() {
		@Override
		public boolean isRunning() throws RemoteException {
			return isServiceRunning();
		}

		@Override
		public void start() throws RemoteException {
			startTouchService();
		}

		@Override
		public void stop() throws RemoteException {
			stopTouchService();
		}

		@Override
		public void refresh() throws RemoteException {
			mTouchView.reload();
		}
	};

	public void startTouchService () {
		mSetting.setEnableAssistiveTouch(true);
		mTouchView.showView();
	}
	
	public void stopTouchService () {
		L.d(TAG, "stopTouchService");
		mSetting.setEnableAssistiveTouch(false);
		mTouchView.removeView();
	}

	public boolean isNeedInit () {
		boolean enable = mSetting.isEnableAssistiveTouch();
		if (enable && mTouchView.getShowingView() == 0) {
			return true;
		}
		return false;
	}
	
	public boolean isServiceRunning () {
		if (mTouchView.getShowingView() == 0 ) {
			return false;
		}
		return true;
	}
}
