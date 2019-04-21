package com.direct.ai.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Bean工具类
 *
 */
public class BeanUtil {
	private static Logger log = Logger.getLogger(BeanUtil.class);
	// bean容器
	private static Map<String, Object> context = new HashMap<>();
	/** Bean类型 */
	public static enum BeanType{
		prototype, single;
	}
	
	public static class BeanNames{
		public static String faceHandleService = PropertyUtil.getProperty("AIHandleService.Face");
		public static String voiceHandleService = PropertyUtil.getProperty("AIHandleService.Voice");
	}
	
	public static <T> T getSingleBean(String bean) {
		return getBean(bean, BeanType.single);
	}
	
	public static <T> T getBean(String bean) {
		return getBean(bean, BeanType.prototype);
	}
	
	/**
	 * 获取bean对象
	 * @param bean
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String bean, BeanType type) {
		T t = null;
		try {
			if(type == BeanType.single) {
				Object o = context.get(bean);
				if(o == null) {
					t = (T) Class.forName(bean).newInstance();
					context.put(bean, t);
				}else {
					t = (T) o;
				}
			}else {
				t = (T) Class.forName(bean).newInstance();	
			}
		} catch (Exception e) {
			log.error("创建对象异常：", e);
		}
		return t;
	}
	
}
