package com.leon.assistivetouch.main.util;

import java.util.LinkedHashMap;
import java.util.Map;

import com.leon.assistivetouch.main.AssistiveTouchApplication;
import com.leon.assistivetouch.main.SettingActivity;
import com.leon.assistivetouch.main.bean.KeyItemInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


/** 
 * 类名      Setting.java
 * 说明   description of the class
 * 创建日期 2012-8-21
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
*/
public class Settings implements Constan{

	private static Settings current = null;
	private static Object mLock = new Object();
	
	public static void init (Context context) {
		synchronized(mLock) {
			if (current == null) {
				current = new Settings(context);
			}
		}
	}
	
	public static Settings getInstance () {
		synchronized (mLock) {
			if (current == null) {
				throw new NullPointerException("Setting is not int");
			}
			return current;
		}
	}
	
	public static Settings getInstance (Context context) {
		synchronized (mLock) {
			if (current == null) {
				current = new Settings(context);
			}
			return current;
		}
	} 
	
	private SharedPreferences mSpref = null;
	private Context mContext;
	
	private LinkedHashMap<Integer, KeyItemInfo> mMainItemMap;
	
	private boolean root;
	private boolean isEnableLongPress;
	private boolean isEnableDoubleTap;
	private boolean isEnableVirbrator;
	private int mTouchDotPosX;
	private int mTouchDotPosY;
	private int mTouchDotSize;
	private int mTouchDotTransparency;
	
	public Settings (Context context) {
		L.d("初始化setting..");
		mContext = context;
		mSpref = PreferenceManager.getDefaultSharedPreferences(context);
		
		int init_code = mSpref.getInt(INIT_APPLICATION_VERSION_CODE, -1);
		int version_code = AssistiveTouchApplication.getVersionNumber(mContext);
		init (init_code, version_code);
		
		isEnableDoubleTap = mSpref.getBoolean(ENABLE_DOUBLE_TAP_KEY, false);
		isEnableLongPress = mSpref.getBoolean(ENABLE_LONG_PRESS_KEY, false);
		isEnableVirbrator = mSpref.getBoolean(ENABLE_VIBRATOR_KEY, true);
		
		mTouchDotPosX = mSpref.getInt(TOUCH_DOT_VIEW_POS_X_KEY,
				DEFAULT_TOUCH_DOT_VIEW_POS_X);
		mTouchDotPosY = mSpref.getInt(TOUCH_DOT_VIEW_POS_Y_KEY,
				DEFAULT_TOUCH_DOT_VIEW_POS_Y);
		
		mTouchDotSize = mSpref.getInt(TOUCH_DOT_SIZE_KEY, SettingActivity.DEFAULT_TOUCH_DOT_SIZE);
		mTouchDotTransparency = mSpref.getInt(TOUCH_DOT_TRANSPARENCY_KEY, -1);
		
		setupMainItemMap ();
	}
	
	private void init (int init_code, int version_code) {
		if (init_code >= version_code) {
			return;
		}
		Editor editor = mSpref.edit();
		if (init_code == -1) {
			editor.putInt(TOUCH_MAIN_ITEM_TYPE_2, KeyItemInfo.TYPE_KEY);
			editor.putInt(TOUCH_MAIN_ITEM_TYPE_4, KeyItemInfo.TYPE_KEY);
			editor.putInt(TOUCH_MAIN_ITEM_TYPE_5, KeyItemInfo.TYPE_KEY);
			editor.putInt(TOUCH_MAIN_ITEM_TYPE_6, KeyItemInfo.TYPE_KEY);
			editor.putInt(TOUCH_MAIN_ITEM_TYPE_8, KeyItemInfo.TYPE_KEY);
			
			editor.putString(TOUCH_MAIN_ITEM_DATA_2, String.valueOf(KeyItemInfo.KEY_RECENT));
			editor.putString(TOUCH_MAIN_ITEM_DATA_4, String.valueOf(KeyItemInfo.KEY_BACK));
			editor.putString(TOUCH_MAIN_ITEM_DATA_5, String.valueOf(KeyItemInfo.KEY_HIDE));
			editor.putString(TOUCH_MAIN_ITEM_DATA_6, String.valueOf(KeyItemInfo.KEY_MENU));
			editor.putString(TOUCH_MAIN_ITEM_DATA_8, String.valueOf(KeyItemInfo.KEY_HOME));
		}
		editor.putInt(INIT_APPLICATION_VERSION_CODE, version_code);
		editor.commit();
	}
	
	public boolean isRoot () {
		return this.root;
	}
	
	public void setRoot (boolean root) {
		this.root = root; 
	}
	
	public boolean isEnableAutoUpdate () {
		return mSpref.getBoolean(ENABLE_AUTO_UPDATE_KEY, true);
	}
	
	public void setEnableAutoUpdate (boolean enable) {
		mSpref.edit().putBoolean(ENABLE_AUTO_UPDATE_KEY, enable).commit();
	}
	
	public boolean isEnableAssistiveTouch () {
		return mSpref.getBoolean(ENABLE_ASSISTIVE_KEY, false);
	}
	
	/**
	 * 是否启动 虚拟按键助手
	 * */
	public void setEnableAssistiveTouch (boolean enable) {
		mSpref.edit().putBoolean(ENABLE_ASSISTIVE_KEY, enable).commit();
	}
	
	/**
	 * 设置是否开机启动
	 * */
	public void setEnableBootStart (boolean enable) {
		mSpref.edit().putBoolean(ENABLE_BOOT_START_KEY, enable).commit();
	}
	
	public boolean isEnableBootStart () {
		return mSpref.getBoolean(ENABLE_BOOT_START_KEY, false);
	}
	
	public int getTouchPositionX () {
		return mTouchDotPosX;
	}
	
	public int getTouchPositionY () {
		return mTouchDotPosY;
	}
	
	public int getTouchDotSize () {
		return mTouchDotSize;
	}
	
	public void setTouchDotSize (int size) {
		mTouchDotSize = size;
		mSpref.edit().putInt(TOUCH_DOT_SIZE_KEY, size).commit();
	}
	
	public int getTouchDotTransparency () {
		return mTouchDotTransparency;
	}
	
	public void setTouchDotTransparency (int transparency) {
		mTouchDotTransparency = transparency;
		mSpref.edit().putInt(TOUCH_DOT_TRANSPARENCY_KEY, transparency).commit();
	}
	
	/**
	 * 设置TouchDot的位置
	 * */
	public void setTouchPosition (int x, int y) {
		mTouchDotPosX = x;
		mTouchDotPosY = y;
		Editor edit = mSpref.edit();
		edit.putInt(TOUCH_DOT_VIEW_POS_X_KEY, x);
		edit.putInt(TOUCH_DOT_VIEW_POS_Y_KEY, y);
		edit.commit();
	}
	
	/**
	 * 是否使用点击按键震动效果
	 * */
	public void setEnableVibrator (boolean enable) {
		isEnableVirbrator = enable;
		mSpref.edit().putBoolean(ENABLE_VIBRATOR_KEY, enable).commit();
	}
	
	public boolean isEnableVirbrator () {
		return isEnableVirbrator;
	}
	
	public void setEnableDoubleTap (boolean enable) {
		isEnableDoubleTap = enable;
		mSpref.edit().putBoolean(ENABLE_DOUBLE_TAP_KEY, enable);
	}
	
	public boolean isEnableDoubleTap () {
		return isEnableDoubleTap;
	}
	
	public void setEnableLongPress (boolean enable) {
		isEnableLongPress = enable;
		mSpref.edit().putBoolean(ENABLE_LONG_PRESS_KEY, enable).commit();
	}
	
	public boolean isEnableLongPress () {
		return isEnableLongPress;
	}
	
	public void setupMainItemMap () {
		if (mMainItemMap == null) {
			mMainItemMap = new LinkedHashMap<Integer, KeyItemInfo>();
		} else {
			mMainItemMap.clear();
		}
		for (int i = 1; i <= 9; i ++) {
			int type = mSpref.getInt("main_item_type" + i, 0);
			String data = mSpref.getString("main_item_data" + i, "");
			mMainItemMap.put(i, getKeyItemInfo(type, data));
		}
	}
	
	public Map<Integer, KeyItemInfo> getMainItemMap () {
		return mMainItemMap;
	}
	
	private KeyItemInfo getKeyItemInfo (int type, String data) {
		KeyItemInfo info = null;
		if (type == KeyItemInfo.TYPE_APP) {
			
		} else if (type == KeyItemInfo.TYPE_KEY) {
			info = KeyItemInfo.getKeysKeyItemInfo(mContext, type, data);
		} else if (type == KeyItemInfo.TYPE_TOOL) {
			
		}
		return info;
		/*PackageManager packageManager = mContext.getPackageManager();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
							mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> allapps = packageManager.queryIntentActivities(mainIntent, 0);*/
	}
}
