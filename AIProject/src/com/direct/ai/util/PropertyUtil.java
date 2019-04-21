package com.direct.ai.util;


import java.io.*;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 文件获取工具类
 * 
 */
public class PropertyUtil {
	private static final Logger logger = LogManager.getLogger(PropertyUtil.class);
	private static Properties props;
	static {
		loadProps("/app.properties");
	}

	synchronized static private void loadProps(String pfile) {
		InputStream in = null;
		try {
			// 通过类进行获取properties文件流
			in = PropertyUtil.class.getResourceAsStream(pfile);
			if(in == null){
				logger.warn(pfile + " 文件不存在");
				return;
			}
			props = new Properties();
			props.load(in);
		} catch (FileNotFoundException e) {
			logger.error("jdbc.properties文件未找到");
		} catch (IOException e) {
			logger.error("出现IOException");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("jdbc.properties文件流关闭出现异常");
			}
		}
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}
}