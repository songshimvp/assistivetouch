package com.leon.assistivetouch.main.ui;

import java.util.List;

import com.leon.assistivetouch.main.R;
import com.leon.assistivetouch.main.bean.KeyItemInfo;
import com.leon.assistivetouch.main.util.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/** 
 * 类名      TouchMainView.java
 * 说明   description of the class
 * 创建日期 2012-8-21
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
*/
public class TouchPanelView extends LinearLayout{

	private static final String TAG = "TouchMainView";
	
	private Context mContext;
	
	private View mKeysLayout;
	private GridView mKeyGridView;
	private OnKeyClickListener mOnKeyClickListener;
	private KeyGridAdapter mAdapter;
	private boolean isEnableItemText;
	
	public TouchPanelView(Context context) {
		super(context);
		mContext = context;
		init ();
	}
	
	public TouchPanelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init ();
	}
	
	private void init() {
		inflate(mContext, R.layout.touch_panel_view, this);
		mKeysLayout = findViewById(R.id.top_view_keys_layout);
		mKeyGridView = (GridView) findViewById(R.id.key_grid_view);
	}
	
	public void setKeyList (List<KeyItemInfo> list) {
		mAdapter = new KeyGridAdapter(mContext, list);
		mKeyGridView.setAdapter(mAdapter);
	}

	public void setOnKeyClickListener (OnKeyClickListener listener) {
		mOnKeyClickListener = listener;
	}
	
	public void notifyDataSetChanged () {
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}
	
	public void setEnableItemText (boolean enable) {
		isEnableItemText = enable;
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}
	
	private class OnKeyItemClickListener implements View.OnClickListener {

		private KeyItemInfo mInfo;
		private int mPosition;
		public OnKeyItemClickListener (int postion, KeyItemInfo info) {
			this.mInfo = info;
			this.mPosition = postion;
		}
		
		@Override
		public void onClick(View v) {
			if (mOnKeyClickListener != null) {
				mOnKeyClickListener.onClick(mPosition, mInfo);
			}
		}
		
	} 

	public abstract interface OnKeyClickListener {
		public abstract void onClick(int position, KeyItemInfo info);
	}
	
	private class KeyGridAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private List<KeyItemInfo> mList;
		public KeyGridAdapter (Context context, List<KeyItemInfo> list) {
			mList = list;
			inflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public KeyItemInfo getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				convertView = inflater.inflate(R.layout.key_grid_item, null, false);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.key_item_title_text);
				holder.icon = (ImageView) convertView.findViewById(R.id.key_item_icon_img);
				holder.layout = convertView.findViewById(R.id.key_grid_item_view);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			KeyItemInfo info = getItem(position);
			holder.title.setVisibility(isEnableItemText ? View.VISIBLE :View.GONE);
			if (info == null) {
				holder.title.setText("");
				holder.icon.setImageBitmap(null);
			} else {
				holder.layout.setOnClickListener(new OnKeyItemClickListener(position, info));
				holder.layout.setBackgroundResource(R.drawable.selector_sysbar_bg);
				if (Util.isStringNull(info.getTitle())) {
					holder.title.setVisibility(View.GONE);
				} else {
					holder.title.setText(info.getTitle());
				}
				holder.icon.setImageDrawable(info.getIcon());
			}
			return convertView;
		}
		
		private class ViewHolder {
			TextView title;
			ImageView icon;
			View layout;
		}
	}
}
