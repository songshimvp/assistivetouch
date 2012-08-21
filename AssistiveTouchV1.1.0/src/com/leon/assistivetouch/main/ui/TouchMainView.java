package com.leon.assistivetouch.main.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.leon.assistivetouch.main.R;
import com.leon.assistivetouch.main.bean.KeyItemInfo;

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
public class TouchMainView extends LinearLayout{

	private static final String TAG = "TouchMainView";
	
	private Context mContext;
	
	private View mKeysLayout;
	private GridView mKeyGridView;
	private OnKeyClickListener mOnKeyClickListener;
	private KeyGridAdapter mAdapter;
	private List<KeyItemInfo> mItemInfoList;
	
	public TouchMainView(Context context) {
		super(context);
		mContext = context;
		init ();
	}
	
	public TouchMainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init ();
	}
	
	private void init() {
		mItemInfoList = new ArrayList<KeyItemInfo>();
		inflate(mContext, R.layout.touch_main_view, this);
		mKeysLayout = findViewById(R.id.top_view_keys_layout);
		
		mAdapter = new KeyGridAdapter(mContext, mItemInfoList);
		
		mKeyGridView = (GridView) findViewById(R.id.key_grid_view);
		mKeyGridView.setAdapter(mAdapter);
	}
	
	public void setKeyList (List<KeyItemInfo> list) {
		mItemInfoList.clear();
		Iterator<KeyItemInfo> iterator  = list.iterator();
		while (iterator.hasNext()) {
			mItemInfoList.add(iterator.next());
		}
		mAdapter.notifyDataSetChanged();
	}

	public void setOnKeyClickListener (OnKeyClickListener listener) {
		mOnKeyClickListener = listener;
	}
	
	public class OnKeyItemClickListener implements View.OnClickListener {

		private KeyItemInfo mInfo;
		public OnKeyItemClickListener (KeyItemInfo info) {
			this.mInfo = info;
		}
		
		@Override
		public void onClick(View v) {
			if (mOnKeyClickListener != null) {
				mOnKeyClickListener.onClick(mInfo);
			}
		}
		
	} 

	public abstract interface OnKeyClickListener {
		public abstract void onClick(KeyItemInfo info);
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
			if (info == null) {
				holder.title.setText("");
				holder.icon.setImageBitmap(null);
			} else {
				holder.layout.setOnClickListener(new OnKeyItemClickListener(info));
				holder.title.setText(info.getTitle());
				holder.icon.setImageResource(info.getIcon());
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
