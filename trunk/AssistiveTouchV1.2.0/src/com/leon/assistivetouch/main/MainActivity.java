package com.leon.assistivetouch.main;

import com.leon.assistivetouch.main.util.L;
import com.leon.assistivetouch.main.util.MemoryCache;
import com.leon.assistivetouch.main.util.Settings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;


/** 
 * 类名      MainActivity.java
 * 说明   主配置界面
 * 创建日期 2012-8-21
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
*/
public class MainActivity extends Activity implements OnClickListener{
	
	private static final String TAG = "MainActivity";
	
	private Button mServiceBtn;
	private Button mSettingDotBtn;
	private Button mSettingPanelBtn;
	private IAssistiveTouchService mService;
	private boolean isRunning;
	private Settings mSetting;
	private Handler mHandler = new Handler();
	AssistiveTouchApplication app = null;
	private static final int REQUEST_CODE_SETTING = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initView();
        mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				connect ();
			}
		}, 1500);
    }
    
    @Override
    public void onDestroy () {
    	super.onDestroy();
    	if (mService != null) {
    		getApplicationContext().unbindService(mServiceConn);
    	}
    	MemoryCache.clear();
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_CANCELED) {
    		return;
    	}
    	// 在SettingActivity中配置更改，如果服务开启的话就更新界面
    	if (requestCode == REQUEST_CODE_SETTING) {
    		if (mService != null) {
    			try {
					mService.refresh();
				} catch (RemoteException e) {
					L.w(TAG, "", e);
				}
    		}
    	}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_update:
			checkUpdate();
			break;
		}
		return true;
	}

	private void init () {
		app = (AssistiveTouchApplication)getApplication();
    	mSetting = Settings.getInstance(this);
    }
    
    private void initView () {
    	mServiceBtn = (Button) findViewById(R.id.service_start_stop_btn);
    	mServiceBtn.setEnabled(false);
    	mServiceBtn.setOnClickListener(this);

    	mSettingDotBtn = (Button) findViewById(R.id.main_settings_touch_dot_btn);
    	mSettingDotBtn.setEnabled(false);
    	mSettingDotBtn.setOnClickListener(this);
    	
    	mSettingPanelBtn = (Button) findViewById(R.id.main_settings_touch_panel_btn);
    	mSettingPanelBtn.setEnabled(false);
    	mSettingPanelBtn.setOnClickListener(this);
    	
    	CheckBox box = (CheckBox)findViewById(R.id.enable_boot_checkbox);
    	boolean enable = mSetting.isEnableBootStart();
    	box.setChecked(enable);
    	box.setOnCheckedChangeListener(mOnCheckedChangeListener);
    	
    	CheckBox auto_update_box = (CheckBox) findViewById(R.id.enable_update_checkbox);
    	enable = mSetting.isEnableAutoUpdate();
    	auto_update_box.setChecked(enable);
    	auto_update_box.setOnCheckedChangeListener(mOnCheckedChangeListener);
    	
    	TextView version = (TextView) findViewById(R.id.version_textview);
    	String name = AssistiveTouchApplication.getVersionName(this);
		String v = getString(R.string.version_code, name);
		version.setText(v);
    }
    
    private void checkUpdate () {
    	Toast.makeText(this, R.string.menu_checking_update, Toast.LENGTH_SHORT).show();
    	// 异步访问网络
    	new AsyncTask<Void, Void, Message>() {
			@Override
			protected Message doInBackground(Void... params) {
				Message msg = AssistiveTouchApplication.getUpdateMessage(MainActivity.this);
				return msg;
			}

			@Override
			protected void onPostExecute(Message result) {
				if (result == null) {
					Toast.makeText(MainActivity.this, R.string.menu_no_found_update, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, R.string.find_update_title, Toast.LENGTH_SHORT).show();
					AssistiveTouchApplication.showUpdateNotify(MainActivity.this, result);
				}
			}
			
		}.execute();
    }
    
    private void changeButtonStatu (boolean isStart) {
    	mServiceBtn.setText(isStart ? R.string.stop_service : R.string.start_service);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.service_start_stop_btn:
			if (mService != null) {
				try {
					if (isRunning) {
						mService.stop();
					} else {
						mService.start();
					}
					isRunning = mService.isRunning();
					changeButtonStatu(isRunning);
				} catch (RemoteException e) {
					L.e(TAG, "", e);
				}
			} else {
				L.d(TAG, "mService == null");
			}
			break;
		case R.id.main_settings_touch_dot_btn:
			Intent i = new Intent(this, SettingsTouchDotActivity.class);
			startActivityForResult(i, REQUEST_CODE_SETTING);
			break;
		case R.id.main_settings_touch_panel_btn:
			Intent ii = new Intent(this, SettingsTouchPanelActivity.class);
			startActivityForResult(ii, REQUEST_CODE_SETTING);
			break;
		}
	}
	
	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView.getId() == R.id.enable_boot_checkbox) {
				mSetting.setEnableBootStart(isChecked);
			} else if (buttonView.getId() == R.id.enable_update_checkbox) {
				mSetting.setEnableAutoUpdate(isChecked);
				AssistiveTouchApplication app = (AssistiveTouchApplication) getApplication();
				if (isChecked) {
					app.setupAutoUpdate();
				} else {
					app.cancelAutoUpdate();
				}
			}
		}
		
	};
	
	/**
	 * 绑定服务
	 * */
	private void connect () {
		L.d(TAG, "connect ...");
		Intent i = new Intent(AssistiveTouchService.ASSISTIVE_TOUCH_START_ACTION);
		getApplicationContext().bindService(i, mServiceConn, Context.BIND_AUTO_CREATE);
	}
	
	private ServiceConnection mServiceConn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			L.d(TAG, "onServiceDisconnected");
			mService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			L.d(TAG, "onServiceConnected");
			mServiceBtn.setEnabled(true);
			mSettingDotBtn.setEnabled(true);
			mSettingPanelBtn.setEnabled(true);
			mService = IAssistiveTouchService.Stub.asInterface(service);
			try {
				isRunning = mService.isRunning();
				L.d(TAG, "service is running:" + isRunning);
				changeButtonStatu (isRunning);
			} catch (RemoteException e) {
				L.e(TAG, "", e);
			}
		}
	};
}
