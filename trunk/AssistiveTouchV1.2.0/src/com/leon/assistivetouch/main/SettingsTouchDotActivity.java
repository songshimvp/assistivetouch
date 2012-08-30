package com.leon.assistivetouch.main;

import java.util.ArrayList;
import java.util.List;

import com.leon.assistivetouch.main.ui.LayoutCheckBoxView;
import com.leon.assistivetouch.main.util.Settings;
import com.leon.assistivetouch.main.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

/** 
 * 类名      SettingActivity.java
 * 说明   description of the class
 * 创建日期 2012-8-21
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
 */
public class SettingsTouchDotActivity extends Activity{

	private Settings mSetting;
	
	private SeekBar mSeekBar;
	private Spinner mSizeSpinner;
	private EditText mInputSizeEdit;
	private Button mSaveBtn;
	private ImageView mPreViwImg;
	
	private LayoutCheckBoxView mEnableLongPressView;
	
	private ArrayAdapter<String> mAdapter;
	private String[] mDatas;
	private List<Integer> mDataList = new ArrayList<Integer>();
	private int mSpinnerPos = 0;
	// 透明度
	private int mTouchDotTrans;
	// 大小
	private int mTouchDotSize;
	// 长按是否隐藏
	private boolean isEnableLongPress;
	private View mCustomView;
	
	public static final int DEFAULT_TOUCH_DOT_SIZE = -3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_settings_touch_dot);
		initView();
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
		if (mTouchDotSize != mSetting.getTouchDotSize() 
				|| mTouchDotTrans != mSetting.getTouchDotTransparency()
				|| isEnableLongPress != mSetting.isEnableLongPress()) {
			showSavePrefDialog();
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
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
	 * 保存修改过的配置信息
	 * */
	private void savePref () {
		boolean change = false;
		if (mTouchDotSize != mSetting.getTouchDotSize()) {
			mSetting.setTouchDotSize(mTouchDotSize);
			change = true;
		}
		if (mTouchDotTrans != mSetting.getTouchDotTransparency()) {
			mSetting.setTouchDotTransparency(mTouchDotTrans);
			change = true;
		}
		if (isEnableLongPress != mSetting.isEnableLongPress()) {
			mSetting.setEnableLongPress(isEnableLongPress);
			change = true;
		}
		if (change) {
			setResult(RESULT_OK);
		} else {
			setResult(RESULT_CANCELED);
		}
		finish();
	}
	
	private void init () {
		mSetting = Settings.getInstance(this);
		mDatas = getResources().getStringArray(R.array.touch_dot_size_name);
		int []values = getResources().getIntArray(R.array.touch_dot_size_value);
		mDataList.add(DEFAULT_TOUCH_DOT_SIZE);
		mTouchDotSize = mSetting.getTouchDotSize();
		int pos = 0;
		for (int i = 0; i < values.length; i ++) {
			pos ++;
			mDataList.add(values[i]);
			if (values[i] == mTouchDotSize) {
				mSpinnerPos = pos;
			}
		}
		pos ++;
		if (mSpinnerPos == 0 && mTouchDotSize != DEFAULT_TOUCH_DOT_SIZE) {
			mSpinnerPos = pos;
		}
		mDataList.add(mTouchDotSize);
		mTouchDotTrans = mSetting.getTouchDotTransparency();
		if (mTouchDotTrans > 255 || mTouchDotTrans < 0) {
			mTouchDotTrans = 255;
			mSetting.setTouchDotTransparency(mTouchDotTrans);
		}
		
		isEnableLongPress = mSetting.isEnableLongPress();
	}
	
	private void initView () {
		mCustomView = findViewById(R.id.settings_custom_size_view);
		mPreViwImg = (ImageView) findViewById(R.id.settings_preview_img);
		mSeekBar = (SeekBar) findViewById(R.id.settings_touch_dot_transparent_bar);
		mSizeSpinner = (Spinner) findViewById(R.id.settings_touch_dot_size_spinner);
		mEnableLongPressView = (LayoutCheckBoxView) findViewById(R.id.setting_touch_dot_enable_long_press);
		
		mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
		mSeekBar.setMax(255);
		mSeekBar.setProgress(mTouchDotTrans);
		
		mInputSizeEdit = (EditText) findViewById(R.id.settings_input_size_edit);
		
		mSaveBtn = (Button) findViewById(R.id.settings_save_btn);
		mSaveBtn.setOnClickListener(mOnClickListener);
		
		changePreViewSize(mTouchDotSize);
		
		mEnableLongPressView.setChecked(isEnableLongPress);
		mEnableLongPressView.setOnLayoutCheckedChangeListener(mOnLayoutCheckedChangeListener);
		
		mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mDatas);
		mAdapter.setDropDownViewResource(R.layout.spinner_drop_view);
		mSizeSpinner.setAdapter(mAdapter);
		mSizeSpinner.setSelection(mSpinnerPos);
		
		if (mSpinnerPos != mDatas.length -1) {
			mCustomView.setVisibility(View.GONE);
		} else {
			mCustomView.setVisibility(View.VISIBLE);
			mInputSizeEdit.setText(String.valueOf(mDataList.get(mSpinnerPos)));
		}
		mSizeSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
		mInputSizeEdit.addTextChangedListener(mTextWatcher);
	}
	
	private void changePreViewSize (int size) {
		changeImageViewSize(this, mPreViwImg, size);
	}
	
	/**
	 * 更改触摸点大小
	 * */
	private boolean chagePreViewSize (String custom) {
		boolean result = false;
		if (!Util.isStringNull(custom)) {
			try {
				int size = Integer.parseInt(custom);
				mTouchDotSize = size;
				result = true;
			} catch (Exception e) {
				mTouchDotSize = DEFAULT_TOUCH_DOT_SIZE;
				result = false;
			}
		} else {
			mTouchDotSize = DEFAULT_TOUCH_DOT_SIZE;
			result = false;
		}
		changePreViewSize(mTouchDotSize);
		return result;
	}
	
	public static void changeImageViewSize (Context context, ImageView img, int size) {
		if (size == DEFAULT_TOUCH_DOT_SIZE) {
			size = (int) context.getResources().getDimension(R.dimen.size_icon_app);
		}
		ViewGroup.LayoutParams params = img.getLayoutParams();
		params.height = size;
		params.width = size;
		img.setLayoutParams(params);
	}
	
	private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			if (position == mDatas.length - 1) { // 最后一个"自定义"
				mCustomView.setVisibility(View.VISIBLE);
				String custom = mInputSizeEdit.getText().toString();
				chagePreViewSize(custom);
			} else {
				mCustomView.setVisibility(View.GONE);
				mTouchDotSize = mDataList.get(position);
				changePreViewSize(mTouchDotSize);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
		
	};
	
	private LayoutCheckBoxView.OnLayoutCheckedChangeListener mOnLayoutCheckedChangeListener = 
			new LayoutCheckBoxView.OnLayoutCheckedChangeListener() {
				@Override
				public void onCheckedChangedd(View view, boolean isChecked) {
					isEnableLongPress = isChecked;
				}
			};
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.settings_save_btn) {
				savePref();
			}
		}
	};
	
	private TextWatcher mTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String value = s.toString();
			boolean result = chagePreViewSize(value);
			if (!result) {
				s.clear();
			}
		}
	};
	
	private OnSeekBarChangeListener mOnSeekBarChangeListener = new OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			mTouchDotTrans = seekBar.getProgress();
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			mPreViwImg.setAlpha(progress);
		}
	};
	
}
