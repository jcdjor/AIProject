package com.direct.ai.service.impl;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.Part;

import org.json.JSONObject;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.MatchRequest;
import com.direct.ai.service.BaiduAIBase;
import com.direct.ai.service.FaceHandleServiceI;
import com.direct.ai.util.Base64Util;
import com.direct.ai.util.ResponseData;

public class BaiduFaceHandleServicelImpl extends BaiduAIBase implements FaceHandleServiceI {
	//调用客户端的接口数据

	//初始化客户端
	AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
	
	@Override
	public ResponseData detect(Part imagePart) {
		//获取图片编码
		String imageStr = Base64Util.part2Base64(imagePart);
		//接口请求参数
		HashMap<String, String> params = new HashMap<>();
		params.put("face_field", "age,gender,beauty,expression");	//面部属性
		//3、调用检测图片的接口
		JSONObject json = client.detect(imageStr, "BASE64", params);
		//System.out.println(json);
		
		//4、提取面部属性
		JSONObject result = json.getJSONObject("result");	//获取返回结果
		JSONObject face_list = result.getJSONArray("face_list").getJSONObject(0);	//获取结果中的面部属性
		//4.1获取性别、年龄、颜值、表情
		String gender = face_list.getJSONObject("gender").getString("type");
		Integer age = face_list.getInt("age");
		double beauty = face_list.getDouble("beauty");
		String expression = face_list.getJSONObject("expression").getString("type");
		if("male".equals(gender)){
			gender = "男性";
		}else{
			gender = "女性";
		}
		if("none".equals(expression)){
			expression = "无表情";
		}else if("smile".equals(expression)){
			expression = "微笑";
		}else{
			expression = "狂笑";
		}
		 
		//返回数据
		 HashMap<String, Object> data = new HashMap<>();
		 data.put("gender", gender);
		 data.put("age", age);
		 data.put("beauty", beauty);
		 data.put("expression", expression);
		
		 //创建返回数据
		 return ResponseData.success(data);
	}

	@Override
	public ResponseData match(Part imagePart1, Part imagePart2) {
		// 转换成base64
		String image1 = Base64Util.part2Base64(imagePart1);
		String image2 = Base64Util.part2Base64(imagePart2);
		
		// 封装平台接口请求对象
		MatchRequest req1 = new MatchRequest(image1, "BASE64");
		MatchRequest req2 = new MatchRequest(image2, "BASE64");
		ArrayList<MatchRequest> requests = new ArrayList<MatchRequest>();
		requests.add(req1);
		requests.add(req2);
		// 人脸匹配
		JSONObject json = client.match(requests);
		System.out.println(json);
		// 响应数据处理, 匹配分值
		double score = json.getJSONObject("result").getDouble("score");
		
		//System.out.println(json);
		
		//5、返回数据
		 HashMap<String, Object> map = new HashMap<>();
		 map.put("score", score + "%");
		 
		//5.1、创建返回数据
		 return ResponseData.success(map);
	}

}
