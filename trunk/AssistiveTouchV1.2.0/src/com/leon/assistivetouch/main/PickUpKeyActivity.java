package com.leon.assistivetouch.main;

import java.util.ArrayList;
import java.util.List;

import com.leon.assistivetouch.main.bean.KeyItemInfo;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/** 
 * 类名      PickUpKeyActivity.java
 * 说明   description of the class
 * 创建日期 2012-8-30
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
*/
public class PickUpKeyActivity extends ListActivity{

	private PickUpKeyAdapter mAdapter;
	private List<KeyItemInfo> mDataList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_pick_up_key);
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
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

	private void init () {
		mDataList = new ArrayList<KeyItemInfo>();
		mDataList.add(getInfo(KeyItemInfo.KEY_HOME));
		mDataList.add(getInfo(KeyItemInfo.KEY_BACK));
		mDataList.add(getInfo(KeyItemInfo.KEY_MENU));
		mDataList.add(getInfo(KeyItemInfo.KEY_RECENT));
		mDataList.add(getInfo(KeyItemInfo.KEY_SEARCH));
		mDataList.add(getInfo(KeyItemInfo.KEY_POWER));
		mAdapter = new PickUpKeyAdapter(this, mDataList);
	}
	
	private void initView() {
		getListView().setAdapter(mAdapter);
	}
	
	private KeyItemInfo getInfo (int key) {
		return KeyItemInfo.getKeyItemInfo(this, KeyItemInfo.TYPE_KEY, String.valueOf(key));
	}
	
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		KeyItemInfo info = mDataList.get(position);
		Intent data = new Intent();
		data.putExtra("type", KeyItemInfo.TYPE_KEY);
		data.putExtra("data", info.getData());
		setResult(RESULT_OK, data);
		finish();
	}



	private class PickUpKeyAdapter extends BaseAdapter {
		
		private LayoutInflater inflater;
		private List<KeyItemInfo> mList;
		public PickUpKeyAdapter (Context context, List<KeyItemInfo> list) {
			inflater = LayoutInflater.from(context);
			mList = list;
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
				convertView = inflater.inflate(R.layout.pick_up_item, null, false);
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.pick_up_item, null, false);
				holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
				holder.title = (TextView) convertView.findViewById(R.id.item_title);
				holder.summary = (TextView) convertView.findViewById(R.id.item_summary);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			KeyItemInfo info = getItem(position);
			holder.icon.setImageDrawable(info.getIcon());
			holder.title.setText(info.getTitle());
			holder.summary.setVisibility(View.GONE);
			return convertView;
		}
	}
	
	private class ViewHolder {
		ImageView icon;
		TextView title;
		TextView summary;
	}
}
