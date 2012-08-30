package com.leon.assistivetouch.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.leon.assistivetouch.main.bean.KeyItemInfo;
import com.leon.assistivetouch.main.ui.LayoutCheckBoxView;
import com.leon.assistivetouch.main.ui.TouchPanelView;
import com.leon.assistivetouch.main.util.L;
import com.leon.assistivetouch.main.util.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

/** 
 * 类名      SettingsTouchMainActivity.java
 * 说明   description of the class
 * 创建日期 2012-8-22
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
 */
public class SettingsTouchPanelActivity extends Activity implements View.OnClickListener{

	private static final String TAG = "SettingsTouchPanelActivity";
	
	private TouchPanelView mTouchMainView;
	
	private LayoutCheckBoxView mEnableItemTextView;
	private LayoutCheckBoxView mEnableClickEventView;
	private LayoutCheckBoxView mEnableVirtraborView;
	private Settings mSetting;
	
	private boolean isEnableClickEvent;
	private boolean isEnableItemText;
	private boolean isEnableVibrator;
	
	private boolean isPanelChange;
	
	private List<KeyItemInfo> mDatalist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_settings_touch_panel);
		initView ();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed () {
		if (isEnableClickEvent != mSetting.isEnableClickEvent() || 
				isEnableItemText != mSetting.isEnableItemText() || 
				isEnableVibrator != mSetting.isEnableVirbrator() || 
				isPanelChange) {
			showSavePrefDialog();
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int postion, int resultCode, Intent i) {
		if (resultCode != RESULT_OK) {
			return;
		}
		isPanelChange = true;
		int type = i.getIntExtra("type", KeyItemInfo.TYPE_NONE);
		String data = i.getStringExtra("data");
		L.d(TAG, String.format("选择的type:%d, data:%s", type, data));
		KeyItemInfo info = KeyItemInfo.getKeyItemInfo(this, type, data);
		mDatalist.set(postion, info);
		mTouchMainView.notifyDataSetChanged();
	}

	private void init () {
		mSetting = Settings.getInstance(this);
		mDatalist = new ArrayList<KeyItemInfo>();
	}
	
	private void initView () {
		mTouchMainView = (TouchPanelView) findViewById(R.id.setting_touch_main_view);
		mEnableClickEventView = (LayoutCheckBoxView) findViewById(R.id.setting_touch_panel_click_event_view);
		mEnableItemTextView = (LayoutCheckBoxView) findViewById(R.id.setting_touch_panel_item_text_view);
		mEnableVirtraborView = (LayoutCheckBoxView) findViewById(R.id.setting_touch_panel_enable_virbrator_view);
		
		findViewById(R.id.setting_touch_panel_click_event_view).setOnClickListener(this);
		findViewById(R.id.settings_save_btn).setOnClickListener(this);

		isEnableItemText = mSetting.isEnableItemText();
		mEnableItemTextView.setChecked(isEnableItemText);
		
		isEnableClickEvent = mSetting.isEnableClickEvent();
		mEnableClickEventView.setChecked(isEnableClickEvent);
		
		isEnableVibrator = mSetting.isEnableVirbrator();
		mEnableVirtraborView.setChecked(isEnableVibrator);
		
		mEnableClickEventView.setOnLayoutCheckedChangeListener(mOnLayoutCheckedChangeListener);
		mEnableItemTextView.setOnLayoutCheckedChangeListener(mOnLayoutCheckedChangeListener);
		mEnableVirtraborView.setOnLayoutCheckedChangeListener(mOnLayoutCheckedChangeListener);
		
		Map<Integer, KeyItemInfo> map = mSetting.getMainItemMap();
		
		for (int i = 1; i <= map.size(); i ++) {
			KeyItemInfo info = map.get(i);
			if (info == null) {
				info = new KeyItemInfo(this, "", R.drawable.selector_item_add, KeyItemInfo.TYPE_NONE, null);
			}
			mDatalist.add(info);
		}
		mTouchMainView.setOnKeyClickListener(mItemClickListener);
		mTouchMainView.setEnableItemText(isEnableItemText);
		mTouchMainView.setKeyList(mDatalist);
	}
	
	/**
	 * 显示是否放弃当前编辑的对话框
	 * */
	private void showSavePrefDialog () {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_setting_title);
		builder.setMessage(R.string.dialog_setting_message);
		builder.setNegativeButton(R.string.dialog_cancel, null);
		builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		builder.create().show();
	}
	
	/**
	 * 保存更改的配置
	 * */
	private void savePref () {
		boolean isChange = false;
		if (isEnableClickEvent != mSetting.isEnableClickEvent()) {
			mSetting.setEnableClickEvent(isEnableClickEvent);
			isChange = true;
		}
		if (isEnableItemText != mSetting.isEnableItemText()) {
			mSetting.setEnableItemText(isEnableItemText);
			isChange = true;
		}
		if (isEnableVibrator != mSetting.isEnableVirbrator()) {
			mSetting.setEnableVibrator(isEnableVibrator);
			isChange = true;
		}
		Map<Integer, KeyItemInfo> map = mSetting.getMainItemMap();
		for (int i = 1; i <= map.size(); i ++) {
			KeyItemInfo info1 = map.get(i);
			KeyItemInfo info2 = mDatalist.get(i - 1);
			if (!KeyItemInfo.isEquals(info1, info2)) {
				isChange = true;
				mSetting.setPanelItemData(i, info2.getType(), info2.getData());
			}
		}
		setResult(isChange ? RESULT_OK : RESULT_CANCELED);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settings_save_btn:
			savePref ();
			break;
		}
	}
	
	private LayoutCheckBoxView.OnLayoutCheckedChangeListener mOnLayoutCheckedChangeListener = 
			new LayoutCheckBoxView.OnLayoutCheckedChangeListener () {
				@Override
				public void onCheckedChangedd(View view, boolean isChecked) {
					switch (view.getId()) {
					case R.id.setting_touch_panel_enable_virbrator_view:
						isEnableVibrator = isChecked;
						break;
					case R.id.setting_touch_panel_item_text_view:
						isEnableItemText = isChecked;
						mTouchMainView.setEnableItemText(isEnableItemText);
						break;
					case R.id.setting_touch_panel_click_event_view:
						isEnableClickEvent = isChecked;
						break;
					}
				}
	};
	
	/*-------------------------------------------------------------------------------------*/
	
	private SelectDialog mSelectDialog = null;
	private void showSelectFunctionDialog (int pos) {
		if (mSelectDialog == null) {
			mSelectDialog = new SelectDialog(this);
		}
		mSelectDialog.setPosition(pos);
		mSelectDialog.show();
	}
	
	private class SelectDialog {
		private Dialog mDialog = null;
		private ArrayAdapter<String> mSelectAdapter = null;
		private int position;
		public SelectDialog (Context context) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.dialog_select_command_title);
			String []datas = getResources().getStringArray(R.array.key_function);
			mSelectAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, datas);
			builder.setAdapter(mSelectAdapter, mClick);
			mDialog = builder.create();
		}
		
		DialogInterface.OnClickListener mClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					mDatalist.set(position, new KeyItemInfo(SettingsTouchPanelActivity.this, "", R.drawable.selector_item_add, KeyItemInfo.TYPE_NONE, null));
					mTouchMainView.notifyDataSetChanged();
					break;
				case 1:
					Intent i = new Intent(SettingsTouchPanelActivity.this, PickUpAppActivity.class);
					startActivityForResult(i, position);
					break;
				case 2:
					Intent ii = new Intent(SettingsTouchPanelActivity.this, PickUpKeyActivity.class);
					startActivityForResult(ii, position);
					break;
				case 3:
					break;
				}
			}
		};
		
		public void setPosition (int pos) {
			position = pos;
		}
		
		public int getPosition () {
			return position;
		}
		
		public void show () {
			mDialog.show();
		}
	}
	
	/*-------------------------------------------------------------------------------------*/
	private TouchPanelView.OnKeyClickListener mItemClickListener = new TouchPanelView.OnKeyClickListener () {
		@Override
		public void onClick(int position, KeyItemInfo info) {
			if (position == 4) {
				return;
			}
			showSelectFunctionDialog(position);
		}
	}; 
}
