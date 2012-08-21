package com.leon.assistivetouch.main.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

/** 
 * 类名      Util.java
 * 说明  工具类
 * 创建日期 2012-8-21
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
*/
public class Util {
	
	private static final String MSG_TAG = "FileUtil";
	
	/**
	 * 根据文件名去获取文件后缀名
	 * @param fileName 文件名
	 * */
	public static String getExtFromFileName (String fileName) {
		// 获取文件的扩展名
		String expName = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length()).toLowerCase();
		return expName;
	}
	
	/**
	 * 转换文件大小的显示格式
	 * @param file 文件的File对象
	 * */
	public static String formatSizeString (File file) {
		String fileSize = "";
		if (file.isFile()) {
			long length = file.length();
			fileSize = formatSizeString(length);
		}
		return fileSize;
	}
	
	/**
	 * 转换文件大小的显示格式
	 * @param size 文件大小
	 * */
	public static String formatSizeString (long size) {
		int sub_index = 0;
		String fileSize = "";
		long length = size;
		if (length >= 1073741824) {
			sub_index = (String.valueOf((float)length / 1073741824)).indexOf(".");
			fileSize = ((float) length / 1073741824 + "000").substring(0, sub_index + 3) + "GB";
		} else if (length >= 1048576) {
			sub_index = (String.valueOf((float)length / 1048576)).indexOf(".");
			fileSize = ((float) length / 1048576 + "000").substring(0, sub_index + 3) + "MB";
		} else if (length >= 1024) {
			sub_index = (String.valueOf((float)length / 1024)).indexOf(".");
			fileSize = ((float) length / 1024 + "000").substring(0, sub_index + 3) + "KB";
		} else {
			fileSize = String.valueOf(length) + "B";
		}
		return fileSize;
	}
	
	/**
	 * 获取apk文件的图标
	 * @param context
	 * @param path apk文件路径
	 * */
	public static Drawable getApkIcon (Context context, String path) {
		PackageManager manager = context.getPackageManager();
		PackageInfo packageInfo = manager.getPackageArchiveInfo(path, 
				PackageManager.GET_ACTIVITIES);
		if (null != packageInfo) {
			ApplicationInfo appInfo = packageInfo.applicationInfo;
			try {
				return manager.getApplicationIcon(appInfo);
			} catch (OutOfMemoryError e) {
				L.e(MSG_TAG, "OutOfMemoryError:" + e.getMessage());
			}
		}
		L.d(MSG_TAG, "null == packageInfo");
		return null;
	}
	
	// 获取apk文件的图标
	public static Bitmap getAPKIcon(Context context, String apkPath) {
         String PATH_PackageParser = "android.content.pm.PackageParser";  
         String PATH_AssetManager = "android.content.res.AssetManager"; 
         Drawable draw = null;
         Resources mResources = context.getResources();
         try {  
             // apk包的文件路径  
             // 这是一个Package 解释器, 是隐藏的  
             // 构造函数的参数只有一个, apk文件的路径  
             // PackageParser packageParser = new PackageParser(apkPath);  
             Class<?>[] typeArgs = new Class[1];  
             typeArgs[0] = String.class;  
             Constructor<?> pkgParserCt = Class.forName(PATH_PackageParser).getConstructor(typeArgs);  
             Object[] valueArgs = new Object[1];  
             valueArgs[0] = apkPath;  
             Object pkgParser = pkgParserCt.newInstance(valueArgs);  
             // 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况  
             DisplayMetrics metrics = new DisplayMetrics();  
             metrics.setToDefaults();  
          
             typeArgs = new Class[4];  
             typeArgs[0] = File.class;  
             typeArgs[1] = String.class;  
             typeArgs[2] = DisplayMetrics.class;  
             typeArgs[3] = Integer.TYPE;  
             Method pkgParser_parsePackageMtd = Class.forName(PATH_PackageParser).getDeclaredMethod("parsePackage",  
                     typeArgs);  
             valueArgs = new Object[4];  
             valueArgs[0] = new File(apkPath);  
             valueArgs[1] = apkPath;  
             valueArgs[2] = metrics;  
             valueArgs[3] = 0;  
             Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);  
             // 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开  
             // ApplicationInfo info = mPkgInfo.applicationInfo;  
             Field appInfoFld = pkgParserPkg.getClass().getDeclaredField("applicationInfo");  
             ApplicationInfo info = (ApplicationInfo) appInfoFld.get(pkgParserPkg);  
             // uid 输出为"-1"，原因是未安装，系统未分配其Uid。  
  
             Class<?> assetMagCls = Class.forName(PATH_AssetManager);  
             Constructor<?> assetMagCt = assetMagCls.getConstructor((Class[]) null);  
             Object assetMag = assetMagCt.newInstance((Object[]) null);  
             typeArgs = new Class[1];  
             typeArgs[0] = String.class;  
             Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath",  
                     typeArgs);  
             valueArgs = new Object[1];  
             valueArgs[0] = apkPath;  
             assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);  
             
             typeArgs = new Class[3];  
             typeArgs[0] = assetMag.getClass();  
             typeArgs[1] = mResources.getDisplayMetrics().getClass();  
             typeArgs[2] = mResources.getConfiguration().getClass();  
             Constructor<?> resCt = Resources.class.getConstructor(typeArgs);  
             valueArgs = new Object[3];  
             valueArgs[0] = assetMag;  
             valueArgs[1] = mResources.getDisplayMetrics();  
             valueArgs[2] = mResources.getConfiguration();  
             mResources = (Resources) resCt.newInstance(valueArgs);  

             // 这里就是读取一个apk程序的图标  
             if (info.icon != 0) {  
                 draw = mResources.getDrawable(info.icon);
                 
             }
         } catch (Exception e) {  
             e.printStackTrace();  
         }
         if (null == draw)
        	 return null;
         return ((BitmapDrawable)draw).getBitmap(); 
     }
	
	/**
	 * 获取文件名，不加上扩展名
	 * @param name 文件全名
	 * @return
	 * */
	public static String getNameFromFileName (String name) {
		int index = name.lastIndexOf(".");
		if (index != -1) {
			name = name.substring(0, index);
		}
		return name;
	}
	
	/**
	 * 根据文件路径获取文件名
	 * @param path
	 * @return
	 * */
	public static String getNameFromFilepath (String path) {
		int index = path.lastIndexOf(File.separator);
		String name = "";
		if (index != -1) {
			name = path.substring(index + 1);
		} else {
			name = path;
		}
		return name;
	}
	
	/**
	 * 根据全路径，获取路径(不包括文件名)
	 * @param path
	 * @return
	 * */
	public static String getPathFromFilepath (String path) {
		int index = path.lastIndexOf(File.separator);
		String p = File.separator;
		if (index != -1) {
			p = path.substring(0, index);
		}
		return p;
	}
	
	/**
	 * 根据全路径，获取路径(不包括文件名)
	 * @param root 默认的根目录路径
	 * @param path 要获取该目录路径的父目录
	 * @return
	 * */
	public static String getParentPath (String root, String path) {
		int index = path.lastIndexOf(File.separator);
		String p = File.separator;
		if (index != -1) {
			p = path.substring(0, index);
		}
		if (!p.startsWith(root))
			p = root;
		return p;
	}
	
	public static String getNetDiskPathFromPath (String path) {
		int index = path.lastIndexOf("/");
		if (index == -1)
			return "";
		return path.substring(0, index);
	}
	
	public static String getNetDiskPath (String path, String name) {
		if (path == null || name == null)
			return null;
		if (path.equals("")) {
			return name;
		}
		return path + File.separator + name;
	}
	
	/**
	 * 生成路径
	 * @param path 文件路径
	 * @param name 文件名
	 * @return
	 * */
	public static String makePath (String path, String name) {
		String str1 = File.separator;
		String p = "";
		if (path.endsWith(str1)) {
			p = path + name;
		} else {
			p = path + str1 + name;
		}
		return p;
	}
	
	/** 
	 * 获取SD卡的信息
	 * {@link SDCardInfo}
	 * */
	public static SDCardInfo getSDCardInfo() {
		if (isSDCardReady()) {
			StatFs statfs = new StatFs(getSdDirectory());
			long l1 = statfs.getBlockCount();
			long l2 = statfs.getBlockSize();
			long l3 = statfs.getAvailableBlocks();
			int free = statfs.getFreeBlocks();
			L.d("free blocks=" + free);
			SDCardInfo info = new SDCardInfo();
			info.total = l1 * l2;
			info.free = l3 * l2;
			return info;
		}
		return null;
	}
	
	/** 获取SD卡的目录路径*/
	public static String getSdDirectory () {
		return Environment.getExternalStorageDirectory().getPath();
	}
	
	/** 判断Sd卡是否已经挂载*/
	public static boolean isSDCardReady () {
		return Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 为TextView设置文件信息
	 * @param view 要设置的view
	 * @param resid view所在的资源id
	 * @param text 要设置的内容
	 * */
	public static boolean setText (View view, int resid, String text) {
		TextView mTextView = (TextView)view.findViewById(resid);
		if (mTextView != null) {
			mTextView.setText(text);
			return true;
		}
		return false;
	}
	
	/** 将字符串转为long*/
	public static long getFormatMillisTime (String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date d = format.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			return calendar.getTimeInMillis();
		} catch (ParseException e) {
			L.w(MSG_TAG, "parse date error:" + e.getMessage());
			return 0;
		}
	}
	
	public static String getSimPleDataFormatDataString (String f, long timeInMs) {
		SimpleDateFormat format = new SimpleDateFormat(f);
		Date date = new Date(timeInMs);
		try {
			return format.format(date);
		} catch (Exception e) {
			L.e(MSG_TAG, "", e);
			return null;
		}
	}

	/** 
	 * 返回格式化后的日期字符串
	 * @param date GMT时间,从1970.1.1开始的微秒
	 * */
	public static String getFormatDateString (long date) {
		return getFormatDateString ("yyyy/MM/dd aa HH:mm:ss", date);
	}
	
	/**
	 * 自定义格式化后的日期字符串
	 * */
	public static String getFormatDateString (String formatstr, long time) {
		SimpleDateFormat format = new SimpleDateFormat(formatstr);
		Date d = new Date(time);
		return format.format(d);
	}
	
	/**
	 * 自定义格式化后的Date
	 * @return {@link Date}
	 * */
	public static Date getFormatDate (String formatStr, String parse) {
		if (isStringNull(parse))
			return new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = format.parse(parse);
		} catch (ParseException e) {
			L.w(MSG_TAG, "", e);
			date = new Date(System.currentTimeMillis());
		}
		return date;
	}
	
	/** 获取'时间'字符串 **/
	public static String getStrTime(int time){
		int min = 0;
		int sec = 0;
		String strTime = "";
		time /= 1000;
		if(time < 60){
			min = 0;
			sec = time;
		}else{
			min = time / 60;
			sec = time % 60;
		}
		if(min < 10){
			strTime += "0" + min + ":" ;
		}else{
			strTime += min + ":" ;
		}
		if(sec<10){
			strTime += "0" + sec ;
		}else{
			strTime += sec ;
		}
		
		return strTime;
	}
	
	public static String getRealPath (String real_root, String root, String path) {
		if (path.equals(root)) {
			return real_root;
		}
		String real_path = (real_root.equals("/") ? "" : real_root) + path.substring(root.equals("/") ? 0 : root.length());
		return real_path;
	}
	
	/**
	 * 拷贝文件
	 * */
	public static void CopyFile (String fromFilePath, String toFilePath) {
		try {
			BufferedInputStream reader = new BufferedInputStream(
					new FileInputStream(new File (fromFilePath)));
			File saveFile = new File (toFilePath);
			int count = 0;
			while (saveFile.exists()) {
				saveFile = new File (toFilePath + "_" + (count ++)); 
			}
			BufferedOutputStream writer = new BufferedOutputStream(
					new FileOutputStream(saveFile));
			try {
				byte buff[] = new byte[8192];
				int offset;
				while((offset = reader.read(buff, 0, buff.length)) != -1) {
					writer.write(buff, 0, offset);
				}				
			} catch(IOException e) {
				L.e(MSG_TAG, "ioexception...");
			} finally {
				try {
					if(reader != null) {
						writer.close();
						reader.close();
					}
				} catch(IOException e) {
					L.e(MSG_TAG, "Error closing files when transferring ");
				}
			}
		} catch (FileNotFoundException e) {
			L.e(MSG_TAG, "", e);
		}
	}
	
	public static File getFile(String curdir, String file) {
		String separator = "/";
		  if (curdir.endsWith("/")) {
			  separator = "";
		  }
		   File clickedFile = new File(curdir + separator
		                       + file);
		return clickedFile;
	}
	
	public static Bitmap decodeFile(File f, int maxSize){
	    Bitmap b = null;
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;

	        FileInputStream fis = new FileInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        fis.close();

	        int scale = 1;
	        if (o.outHeight > maxSize || o.outWidth > maxSize) {
	            scale = (int)Math.pow(2, (int) Math.round(Math.log(maxSize / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        fis = new FileInputStream(f);
	        b = BitmapFactory.decodeStream(fis, null, o2);
	        fis.close();
	    } catch (IOException e) {
	    }
	    return b;
	}
	
	/**
	 * 获取bitmap
	 * @param path 本地图片的路径
	 * @param size 获取bitmap的大小
	 * @return
	 * */
	public static Bitmap getBitmap (String path, int size) {
		Bitmap bmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
					
		opts.inSampleSize = computeSampleSize(opts, -1, size);		
		opts.inJustDecodeBounds = false;
		try {
			bmp = BitmapFactory.decodeFile(path, opts);
		    } catch (OutOfMemoryError err) {
		    	L.d(MSG_TAG, "Out of memory....");
		    	return null;
		    }
		return bmp;
	}
	
	/**
	 * 计算返回bitmap的大小
	 * */
	private static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,
	            maxNumOfPixels);

	    int roundedSize;
	    if (initialSize <= 8) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }

	    return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}
	
	/**
	 * 根据日期，获取星期几
	 * */
	public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	
	/**
	 * 格式化为秒
	 * */
	public static String getTimeWithSecond (long second) {
		long min = 0;
		long sec = 0;
		String str = "";
		if(second < 60){
			min = 0;
			sec = second;
			str = sec + "秒";
		}else{
			min = second / 60;
			sec = second % 60;
			str = min + "分" + sec + "秒";
		}
		return str;
	}
	
	/**
	 * 简单判断是不是合法的手机号码
	 * */
	public static boolean isMobileNumber (String number) {
		if (number == null || number.length() != 11)
			return false;
		String reg = "(13|15|18)[0-9]{9}";
		CharSequence inputStr = number;
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(inputStr);
		return m.matches();
	}
	
	/**
	 * 判断是不是正确的Email格式
	 * */
	public static boolean isEmail (String email) {
		if (email == null || email.equals(""))
			return false;
		email = email.toLowerCase();
		String reg = "^[a-z]([a-z0-9]*[-_]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[\\.][a-z]{2,3}([\\.][a-z]{2})?$";
		CharSequence inputStr = email;
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(inputStr);
		return m.matches();
	}
	
	/**
	 * 判断是不是手机号码
	 * */
	public static boolean isNumber (String n) {
		if (n == null || n.equals(""))
			return false;
		return Pattern.compile("[0-9]*").matcher(n).matches();
	}
	
	/**
	 * 判断字符串是否为空
	 * */
	public static boolean isStringNull (String str) {
		if (str == null || str.equals(""))
			return true;
		return false;
	}
	
	/**
	 * 获取数据库名字
	 * */
	public static String getUserDatabaseName (String account) {
		return account + ".db";
	}
	
	/**
	 * 检查是否有sd卡
	 * @param requireWriteAccess 检查是否需要可写权限
	 * @return
	 * */
	public static boolean hasStorage(boolean requireWriteAccess) {
		String state = Environment.getExternalStorageState();
		L.d(MSG_TAG, "storage state is " + state);

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			if (requireWriteAccess) {
				boolean writable = checkCanWrite(Environment.getExternalStorageDirectory());
				return writable;
			} else {
				return true;
			}
		} else if (!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	/** 检查读写权限 */
	public static boolean checkCanWrite(File file) {
		if (file.canWrite())
			return true;
		return false;
	}

	/** 检查读写权限 */
	public static boolean checkCanWrite(String path) {
		File file = new File(path);
		return checkCanWrite(file);
	}
	
	public static class SDCardInfo {
		public long free;
		public long total;
	}
	
	/*------------------------------------------------*/
	/**
	 * 将ip转为字符串
	 * */
	public static String intToIp (int i) {
		return (i & 0xFF)+ "." + ((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) + "." + ((i >> 24 ) & 0xFF);
	}
	
	/**
	 * 获取IP地址
	 */
	public static String getWifiIp (Context context) {
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		boolean enable = wifiManager.isWifiEnabled();
		if (!enable) {
			return null;
		}
		WifiInfo wifiinfo= wifiManager.getConnectionInfo();
		return intToIp(wifiinfo.getIpAddress());
	}
	
	/**
	 * 获取网关IP地址
	 * */
	public static String getGateWayAddress (Context context) {
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		boolean enable = wifiManager.isWifiEnabled();
		if (!enable) {
			return null;
		}
		DhcpInfo info = wifiManager.getDhcpInfo();
		return intToIp(info.gateway);
	}
	
	/**
	 * 将value分割并将重复的去除
	 * */
	public static String[] getValues (String value) {
		String vs[] = value.split(";");
		// 去除相同项目
		HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < vs.length; i ++) {
			set.add(vs[i]);
		}
		Iterator<String> iterator = set.iterator();
		vs = new String[set.size()];
		int i = 0;
		while (iterator.hasNext()) {
			vs[i] = iterator.next();
			i ++;
		}
		return vs;
	}
}