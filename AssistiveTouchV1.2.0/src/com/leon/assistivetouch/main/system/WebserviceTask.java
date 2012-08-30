
package com.leon.assistivetouch.main.system;

import java.io.IOException;
import java.util.Properties;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.leon.assistivetouch.main.util.L;


/** 
 * 类名      WebserviceTask.java
 * 说明  网络任务处理
 * 创建日期 2012-8-21
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
*/
public class WebserviceTask {
	
	private static final String MSG_TAG = "WebserviceTask";
	
	public static Properties queryForProperty(String url) {
		Properties properties = null; 
		HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(String.format(url));
        try {
            HttpResponse response = client.execute(request);

            StatusLine status = response.getStatusLine();
            L.d(MSG_TAG, "Request returned status " + status);
            if (status.getStatusCode() == 200) {
	            HttpEntity entity = response.getEntity();
	            properties = new Properties();
	            properties.load(entity.getContent());
            }
        } catch (IOException e) {
        	L.d(MSG_TAG, "Can't get property '"+url+"'.");
        }
		return properties;
	}
}