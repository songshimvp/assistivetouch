package com.leon.assistivetouch.main.bean;

import android.content.Context;

import com.leon.assistivetouch.main.R;
import com.leon.assistivetouch.main.util.KeyAction;
import com.leon.assistivetouch.main.util.Util;

/** 
 * 类名      KeyItemInfo.java
 * 说明   description of the class
 * 创建日期 2012-8-20
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
 */
public class KeyItemInfo {
	private String title;
	private int icon;
	private int type;
	private String data;
	
	public static final int TYPE_APP = 1;
	public static final int TYPE_TOOL = 2;
	public static final int TYPE_KEY = 3;
	
	public static final int KEY_HOME = 1;
	public static final int KEY_BACK = 2;
	public static final int KEY_RECENT = 3;
	public static final int KEY_MENU = 4;
	public static final int KEY_SEARCH = 5;
	public static final int KEY_HIDE = 6;
	
	public KeyItemInfo () {}
	
	public KeyItemInfo (String title, int icon, int type, String data) {
		this.title = title;
		this.icon = icon;
		this.type = type;
		this.data = data;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public static void doKeyEvent (String data) {
		int key = Integer.parseInt(data);
		switch(key) {
		case KEY_BACK:
			KeyAction.doBackAction();
			break;
		case KEY_HOME:
			KeyAction.doHomeAction();
			break;
		case KEY_MENU:
			KeyAction.doMenuAction();
			break;
		case KEY_RECENT:
			KeyAction.doRecentAction();
			break;
		case KEY_SEARCH:
			KeyAction.doSearchAction();
			break;
		default:
			return;
		}
	}
	
	public static int getKeyResource (String data) {
		int key = Integer.getInteger(data, 0);
		int res = -1;
		switch(key) {
		case KEY_BACK:
			res = R.drawable.ic_sysbar_back;
			break;
		case KEY_HOME:
			res = R.drawable.ic_sysbar_home;
			break;
		case KEY_MENU:
			res = R.drawable.ic_sysbar_menu;
			break;
		case KEY_RECENT:
			res = R.drawable.ic_sysbar_recent;
			break;
		case KEY_SEARCH:
			res = R.drawable.ic_sysbar_search;
			break;
		}
		return res;
	}
	
	public static KeyItemInfo getKeysKeyItemInfo (Context context, int type, String data) {
		if (Util.isStringNull(data)) {
			return null;
		}
		if (type != TYPE_KEY) {
			return null;
		}
		KeyItemInfo info = new KeyItemInfo();
		int key = Integer.parseInt(data);
		int res = -1;
		String title = "";
		switch(key) {
		case KEY_BACK:
			title = context.getString(R.string.key_back_name);
			res = R.drawable.ic_sysbar_back;
			break;
		case KEY_HOME:
			title = context.getString(R.string.key_home_name);
			res = R.drawable.ic_sysbar_home;
			break;
		case KEY_MENU:
			title = context.getString(R.string.key_menu_name);
			res = R.drawable.ic_sysbar_menu;
			break;
		case KEY_RECENT:
			title = context.getString(R.string.key_recent_name);
			res = R.drawable.ic_sysbar_recent;
			break;
		case KEY_SEARCH:
			title = context.getString(R.string.key_search_name);
			res = R.drawable.ic_sysbar_search;
			break;
		case KEY_HIDE:
			title = context.getString(R.string.key_hide_name);
			res = R.drawable.ic_home;
			break;
		}
		info.setIcon(res);
		info.setTitle(title);
		info.setType(type);
		info.setData(data);
		return info;
	}
}
