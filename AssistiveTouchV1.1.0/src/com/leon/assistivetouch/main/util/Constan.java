package com.leon.assistivetouch.main.util;

/** 
 * 类名      Constan.java
 * 说明  配置文件
 * 创建日期 2012-8-21
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
*/
public interface Constan {

	public static final boolean isDebug = false;
	
	// 每三小时更新一次
	public static final long CHECK_UPDATE_TIME_LAG = 1000 * 60 * 60 * 3;
	
	/**
	 * Update Url
	 * 在svn的download/update/stable 目录下存放一个配置文件，包含versionCode、fileName、title、message
	 * 所以先下载这个配置文件，然后以 versionCode是作为判断是否需要更新的条件，而fileName是下载的文件名
	 * 下载地址为:http://assistivetouch.googlecode.com/files/{fileName}
	 * title 更新的标题
	 * message 更新的内容
	 * */
	public static final String APPLICATION_PROPERTIES_URL = "http://assistivetouch.googlecode.com/svn/download/update/stable/application.properties";
	public static final String APPLICATION_DOWNLOAD_URL = "http://assistivetouch.googlecode.com/files/";
	
	public static final String TOUCH_DOT_VIEW_POS_X_KEY = "touch_dot_pos_x";
	public static final String TOUCH_DOT_VIEW_POS_Y_KEY = "touch_dot_pos_y";
	
	public static final int DEFAULT_TOUCH_DOT_VIEW_POS_X = 0;
	public static final int DEFAULT_TOUCH_DOT_VIEW_POS_Y = 0;
	
	public static final String INIT_APPLICATION_VERSION_CODE = "init_version_code"; 
	
	public static final String ENABLE_ASSISTIVE_KEY = "enable_assisitive";
	public static final String ENABLE_BOOT_START_KEY = "enable_boot_start";
	public static final String ENABLE_VIBRATOR_KEY = "enable_virbator";
	public static final String ENABLE_LONG_PRESS_KEY = "enable_long_press";
	public static final String ENABLE_DOUBLE_TAP_KEY = "enable_double_tap";
	public static final String ENABLE_AUTO_UPDATE_KEY = "enable_auto_update";
	
	public static final String TOUCH_DOT_TRANSPARENCY_KEY = "touch_dot_transparency";
	public static final String TOUCH_DOT_SIZE_KEY = "touch_dot_size";
	
	// 按键自定义设置
	public static final String TOUCH_MAIN_ITEM_DATA_1 = "main_item_data1";
	public static final String TOUCH_MAIN_ITEM_DATA_2 = "main_item_data2";
	public static final String TOUCH_MAIN_ITEM_DATA_3 = "main_item_data3";
	public static final String TOUCH_MAIN_ITEM_DATA_4 = "main_item_data4";
	public static final String TOUCH_MAIN_ITEM_DATA_5 = "main_item_data5";
	public static final String TOUCH_MAIN_ITEM_DATA_6 = "main_item_data6";
	public static final String TOUCH_MAIN_ITEM_DATA_7 = "main_item_data7";
	public static final String TOUCH_MAIN_ITEM_DATA_8 = "main_item_data8";
	public static final String TOUCH_MAIN_ITEM_DATA_9 = "main_item_data9";
	
	public static final String TOUCH_MAIN_ITEM_TYPE_1 = "main_item_type1";
	public static final String TOUCH_MAIN_ITEM_TYPE_2 = "main_item_type2";
	public static final String TOUCH_MAIN_ITEM_TYPE_3 = "main_item_type3";
	public static final String TOUCH_MAIN_ITEM_TYPE_4 = "main_item_type4";
	public static final String TOUCH_MAIN_ITEM_TYPE_5 = "main_item_type5";
	public static final String TOUCH_MAIN_ITEM_TYPE_6 = "main_item_type6";
	public static final String TOUCH_MAIN_ITEM_TYPE_7 = "main_item_type7";
	public static final String TOUCH_MAIN_ITEM_TYPE_8 = "main_item_type8";
	public static final String TOUCH_MAIN_ITEM_TYPE_9 = "main_item_type9";
}
