package com.leon.assistivetouch.main.util;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

/** 
 * 类名      L.java
 * 说明   调试类
 * 创建日期 2012-8-21
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
*/
public class L {
	
	private static final String TAG = "AssistiveTouch";
	
	public static boolean Debug = Constan.isDebug;
	
	public static final String WARN 	= "WARN";
	public static final String ERROR 	= "ERROE";
	public static final String VERBOSE = "VERBOSE";
	
	public static final String LOG_SUFFIX = "txt";
	
	public static void i (String str){
		i(TAG,str);
	}
	
	public static void i (String tag, String str){
		i (tag, str, null);
	}
	
	public static void i (String tag, String msg, Throwable tr) {
		if(Debug)
			Log.i(tag, msg, tr);
	}
	
	public static void e (String str) {
		e(TAG, str);
	}
	
	public static void e (String tag, String str) {
		e (tag, str, null);
	}
	
	public static void e (String tag, String msg, Throwable tr) {
		if(Debug)
			Log.e(tag, msg, tr);
		//writeLogFile(getLogStyle(ERROR, tag, msg, tr));
	}
	
	public static void v (String str) {
		v(TAG, str);
	}
	
	public static void v (String tag, String str) {
		v (tag, str, null);
	}
	
	public static void v (String tag, String msg, Throwable tr) {
		if(Debug)
			Log.i(tag, msg, tr);
		//writeLogFile(getLogStyle(VERBOSE, tag, msg, tr));
	}
	
	public static void d (String str) {
		d(TAG, str);
	}
	
	public static void d (String tag, String str) {
		d (tag, str, null);
	}
	
	public static void d (String tag, String msg, Throwable tr) {
		if(Debug)
			Log.d(tag, msg, tr);
	}
	
	public static void w (String str) {
		w(TAG, str);
	}
	
	public static void w (String tag, String str) {
		w (tag, str, null);
	}
	
	public static void w (String tag, String msg, Throwable tr) {
		if(Debug)
			Log.w(tag, msg, tr);
		//writeLogFile(getLogStyle(WARN, tag, msg, tr));
	}
	
	public static String getLogStyle (String type, String tag, String msg, Throwable tr) {
		StringBuilder log = new StringBuilder();
		String date = DateFormat.format("yyyy-MM-dd aa hh:mm:ss", 
				System.currentTimeMillis()).toString();
		log.append("[");
		log.append(date);
		log.append("]");
		log.append("[");
		log.append(type);
		log.append("]");
		log.append("[");
		log.append(tag);
		log.append("]");
		log.append(msg);
		if (tr != null) {
			log.append(" ");
			log.append(getStackTraceString(tr));
		}
		return log.toString();
	}
	
	public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }
	
	public static void Toast(String str, Context context){
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
	
	public static void Toast (int resId, Context context) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 字符集的测试,在乱码状态下使用
	 * @param datastr
	 * <p>传入需要测试的字符串</p>
	 * */
	public static void testCharset(String datastr){
        try {
            String temp = new String(datastr.getBytes(), "GBK");
            L.v("****** getBytes() -> GBK ******\n"+temp);
            
            temp = new String(datastr.getBytes("GBK"), "UTF-8");
            L.v("****** GBK -> UTF-8 *******\n"+temp);
            
            temp = new String(datastr.getBytes("GBK"), "ISO-8859-1");
            L.v("****** GBK -> ISO-8859-1 *******\n"+temp);
            
            temp = new String(datastr.getBytes("ISO-8859-1"), "UTF-8");
            L.v("****** ISO-8859-1 -> UTF-8 *******\n"+temp);
            
            temp = new String(datastr.getBytes("ISO-8859-1"), "GBK");
            L.v("****** ISO-8859-1 -> GBK *******\n"+temp);
            
            temp = new String(datastr.getBytes("UTF-8"), "GBK");
            L.v("****** UTF-8 -> GBK *******\n"+temp);
            
            temp = new String(datastr.getBytes("UTF-8"), "ISO-8859-1");
            L.v("****** UTF-8 -> ISO-8859-1 *******\n"+temp);
        } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
        }
	}
	
	/**
	 * 将byte数组以16进制形式输出
	 * */
	public synchronized static void writeLog (byte log[]) {
		String slog = "";
		for (int i = 0; i < log.length; i ++) {
			slog += " " + L.toHex(log[i]);
		}
		L.writeLogFile(slog);
	}
	
	
	/**
	 * 写入调试日志
	 * @param log 要写入的调试内容
	 * */
	public synchronized static void writeLogFile (String log) {
		long currentTime = System.currentTimeMillis();
		String logFileName = DateFormat.format("yyyy-MM-dd_hh_mm", currentTime).toString();
		writeLogFile(logFileName + "." + LOG_SUFFIX, log);
	}
	
	/**
	 * 写入调试日志
	 * @param filename 要保存的文件名
	 * @param content 要写入的调试内容
	 * */
	public synchronized static void writeLogFile (String filename, String content) {
		/*try {
			FileCache cache = FileCache.getInstance();
			if (cache == null)
				return;
			File saveLogDir = cache.getLogCacheDir();
			if (!saveLogDir.exists()) {
				saveLogDir.mkdirs();
			}
			File fileName = new File(saveLogDir, filename);
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content + "\n");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * 将byte转为16进制
	 * @param b
	 * @return
	 * */
	public static final String toHex(byte b) {
		  return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) 
				  + "0123456789ABCDEF".charAt(b & 0xf));
	}
	
	public synchronized static void buildLogMessage(Throwable ex) {
		StringBuilder builder = new StringBuilder();
		if (ex != null) {
			builder.append("\n\n").append("------ 程序错误信息 -------").append("\n")
					.append(L.getLogStyle(L.ERROR, TAG, "", ex));
		}
		long currentTime = System.currentTimeMillis();
		String logFileName = "mark_" + DateFormat.format("yyyy-MM-dd_hh_mm", currentTime).toString();
		writeLogFile(logFileName + "." + LOG_SUFFIX, builder.toString());
	}
}
