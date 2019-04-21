package com.direct.ai.util;

/**
 * 常量类
 * 
 */
public class CommonConstant {
	/**
	 * 百度AI AppID
	 */
	public static String BaiduAIAppID = PropertyUtil.getProperty("Baidu.AI.AppID");
	/**
	 * 百度AI APIKey
	 */
	public static String BaiduAIAPIKey = PropertyUtil.getProperty("Baidu.AI.APIKey");
	/**
	 * 百度AI SecretKey
	 */
	public static String BaiduAISecretKey = PropertyUtil.getProperty("Baidu.AI.SecretKey");

	/**
	 * 字符编码
	 */
	public static final String UTF8 = "utf-8";
	/**
	 * 服务器地址 地址+端口+项目名称
	 */
	public static String SERVER_PATH = null;
	/**
	 * 服务器地址 地址+端口+项目名称+图片URI
	 */
	public static String SERVER_IMG_PATH = null;
	
}
