package com.direct.ai.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;

import com.direct.ai.service.VoiceHandleServiceI;
import com.direct.ai.util.BeanUtil;
import com.direct.ai.util.ResponseData;

@MultipartConfig
@WebServlet("/VoiceRecognize")
public class VoiceRecognizeServicel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//实例化面部是识别服务类
	VoiceHandleServiceI voiceService = BeanUtil.getBean(BeanUtil.BeanNames.voiceHandleService);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Part voicePart = req.getPart("voice");
		
		//识别并返回数据
		ResponseData respDate = voiceService.recognize(voicePart);
		
		//转换字符串
		JSONObject respJson = new JSONObject(respDate);
		
		String respStr = respJson.toString();
		 
		//System.out.println(respStr);
		 
		//返回给浏览器
		PrintWriter writer = resp.getWriter();
		writer.write(respStr);
	}
}
