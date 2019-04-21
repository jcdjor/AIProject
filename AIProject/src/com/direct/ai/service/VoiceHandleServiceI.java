package com.direct.ai.service;

import javax.servlet.http.Part;

import com.direct.ai.util.ResponseData;


public interface VoiceHandleServiceI {
	
	/**
	 * 语音识别
	 * @param voicePart
	 * @return
	 */
	ResponseData recognize(Part voicePart);
	
	/**
	 * 语音合成
	 * @param text 文字
	 * @param voiceType
	 * @return
	 */
	byte[] genVoice(String text, Integer voiceType);
}
