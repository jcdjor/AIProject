package com.direct.ai.service;

import javax.servlet.http.Part;

import com.direct.ai.util.ResponseData;


public interface FaceHandleServiceI {
	
	/**
	 * 人脸检测
	 * @param imagePart
	 * @return
	 */
	ResponseData detect(Part imagePart);
	
	/**
	 * 人脸匹配
	 * @param imagePart1
	 * @param imagePart2
	 * @return
	 */
	ResponseData match(Part imagePart1, Part imagePart2);
}
