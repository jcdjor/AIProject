package com.direct.ai.service;

import org.apache.log4j.Logger;

import com.direct.ai.util.CommonConstant;
import com.direct.ai.util.PropertyUtil;


public abstract class BaiduAIBase {
	protected static Logger log = Logger.getLogger(BaiduAIBase.class);
	
	// 设置APPID/AK/SK
	protected static final String APP_ID = CommonConstant.BaiduAIAppID;
	protected static final String API_KEY = CommonConstant.BaiduAIAPIKey;
	protected static final String SECRET_KEY = CommonConstant.BaiduAISecretKey;
	// 音频格式
	protected static final String PCM = ".pcm";
	protected static final String WAV = ".wav";
	protected static final String MP3 = ".mp3";
	// 保存路径
	protected static final String workspace = PropertyUtil.getProperty("Uploadfile.Path");
}
