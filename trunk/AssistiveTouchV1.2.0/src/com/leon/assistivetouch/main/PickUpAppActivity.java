package com.leon.assistivetouch.main;

import java.util.List;

import com.leon.assistivetouch.main.bean.KeyItemInfo;
import com.leon.assistivetouch.main.util.L;
import com.leon.assistivetouch.main.util.MemoryCache;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/** 
 * 类名      PickUpAppActivity.java
 * 说明   description of the class
 * 创建日期 2012-8-26
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
 */
public class PickUpAppActivity extends ListActivity{

	private static final String TAG = "PickUpAppActivity";
	
	private PickUpAppAdater mAdapter;
	private PackageManager mPackageManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_up_app);
		init();
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
		mPackageManager = getPackageManager();
		mAdapter = new PickUpAppAdater(MemoryCache.getAllApps(mPackageManager), this);
		
	}
	
	private void initView () {
		getListView().setAdapter(mAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent();
		ResolveInfo info = mAdapter.getItem(position);
		i.putExtra("type", KeyItemInfo.TYPE_APP);
		String data = new StringBuilder().append(info.activityInfo.packageName)
				.append(":").append(info.activityInfo.name).toString();
		i.putExtra("data", data);
		setResult(RESULT_OK, i);
		finish();
	}

	private class PickUpAppAdater extends BaseAdapter {

		private List<ResolveInfo> mList = null;
		private LayoutInflater inflater;
		
		public PickUpAppAdater (List<ResolveInfo> list, Context context) {
			this.mList = list;
			inflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public ResolveInfo getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ResolveInfo info = getItem(position);
			final ViewHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.pick_up_item, null, false);
				holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
				holder.title = (TextView) convertView.findViewById(R.id.item_title);
				holder.summary = (TextView) convertView.findViewById(R.id.item_summary);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			holder.title.setText(info.loadLabel(mPackageManager));
			holder.summary.setText(info.activityInfo.name);
			holder.icon.setImageDrawable(info.loadIcon(mPackageManager));
			return convertView;
		}
		
	}
	
	private class ViewHolder {
		ImageView icon;
		TextView title;
		TextView summary;
	}
}
