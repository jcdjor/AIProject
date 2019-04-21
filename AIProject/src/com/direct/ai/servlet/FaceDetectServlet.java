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

import com.direct.ai.service.FaceHandleServiceI;
import com.direct.ai.util.BeanUtil;
import com.direct.ai.util.ResponseData;

@MultipartConfig
@WebServlet("/FaceDetect")
public class FaceDetectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//实例化面部是识别服务类
	FaceHandleServiceI faceService = BeanUtil.getBean(BeanUtil.BeanNames.faceHandleService);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//获取文件信息
		Part imagePart = req.getPart("image");
		
		//识别并返回数据
		ResponseData respDate = faceService.detect(imagePart);
		
		//转换字符串
		JSONObject respJson = new JSONObject(respDate);
		String respStr = respJson.toString();
		 
		//System.out.println(respStr);
		 
		//返回给浏览器
		PrintWriter writer = resp.getWriter();
		writer.write(respStr);
	}
}
