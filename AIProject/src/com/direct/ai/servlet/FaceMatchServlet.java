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
@WebServlet("/FaceMatch")
public class FaceMatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//实例化面部是识别服务类
	FaceHandleServiceI faceService = BeanUtil.getBean(BeanUtil.BeanNames.faceHandleService);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//获取对比的两张图片的信息
		Part imagePart1 = req.getPart("image1");
		Part imagePart2 = req.getPart("image2");
		
		//识别并返回数据
		ResponseData respDate = faceService.match(imagePart1, imagePart2);
		
		//转换字符串
		JSONObject respJson = new JSONObject(respDate);
		String respStr = respJson.toString();
		 
		System.out.println(respStr);
		//5.3、返回给浏览器
		PrintWriter writer = resp.getWriter();
		writer.write(respStr);
		
		
		

	}
}
