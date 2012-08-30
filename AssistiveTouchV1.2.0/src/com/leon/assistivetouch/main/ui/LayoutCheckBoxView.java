package com.leon.assistivetouch.main.ui;

import com.leon.assistivetouch.main.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/** 
 * 类名      LayoutCheckBoxView.java
 * 说明   description of the class
 * 创建日期 2012-8-30
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
 */
public class LayoutCheckBoxView extends LinearLayout{

	private TextView mText;
	private CheckBox mCheckBox;
	private View mHeadDivider;
	private View mFooterDivider;
	private View mLayoutView;
	private OnLayoutCheckedChangeListener mOnLayoutCheckedChangeListener;
	
	public LayoutCheckBoxView(Context context) {
		super(context);
		initView();
		init();
	}
	
	public LayoutCheckBoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LayoutCheckBoxView);
		String text = a.getString(R.styleable.LayoutCheckBoxView_text);
		boolean enable_head = a.getBoolean(R.styleable.LayoutCheckBoxView_headerDividersEnabled, false);
		boolean enable_foot = a.getBoolean(R.styleable.LayoutCheckBoxView_footerDividersEnabled, false);
		boolean checked = a.getBoolean(R.styleable.LayoutCheckBoxView_checked, false);
		this.mText.setText(text);
		this.mCheckBox.setChecked(checked);
		this.mHeadDivider.setVisibility(enable_head ? View.VISIBLE : View.GONE);
		this.mFooterDivider.setVisibility(enable_foot ? View.VISIBLE : View.GONE);
		a.recycle();
		init();
		
	}
	
	private void initView () {
		View layout = inflate(getContext(), R.layout.checkbox_item_layout, this);
		this.mLayoutView = layout.findViewById(R.id.layout_view);
		this.mText = (TextView) layout.findViewById(R.id.layout_text);
		this.mCheckBox = (CheckBox) layout.findViewById(R.id.layout_checkbox);
		this.mHeadDivider = layout.findViewById(R.id.layout_header_divider);
		this.mFooterDivider = layout.findViewById(R.id.layout_footer_divider);
	}
	
	private void init () {
		this.mCheckBox.setOnCheckedChangeListener(mOnCheckedChangeListener);
		this.mLayoutView.setOnClickListener(mOnClickListener);
	}
	
	public void setOnLayoutCheckedChangeListener (OnLayoutCheckedChangeListener listener) {
		this.mOnLayoutCheckedChangeListener = listener;
	}
	
	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (mOnLayoutCheckedChangeListener != null) {
				mOnLayoutCheckedChangeListener.onCheckedChangedd(LayoutCheckBoxView.this, isChecked);
			}
		}
	};
	
	public void setText (int resid) {
		mText.setText(resid);
	}
	
	public void setText (CharSequence text) {
		mText.setText(text);
	}
	
	public void setChecked (boolean checked) {
		mCheckBox.setChecked(checked);
	}
	
	public boolean isChecked () {
		return mCheckBox.isChecked();
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mCheckBox.toggle();
		}
	};
	
	public abstract interface OnLayoutCheckedChangeListener {
		public abstract void onCheckedChangedd (View view, boolean isChecked);
	}
}
