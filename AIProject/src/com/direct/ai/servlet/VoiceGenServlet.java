package com.direct.ai.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.direct.ai.service.VoiceHandleServiceI;
import com.direct.ai.util.BeanUtil;

@MultipartConfig
@WebServlet("/VoiceGen")
public class VoiceGenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//实例化面部是识别服务类
	VoiceHandleServiceI voiceService = BeanUtil.getBean(BeanUtil.BeanNames.voiceHandleService);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String text = req.getParameter("text");
		String voiceTypeStr = req.getParameter("voiceType");
		Integer voiceType = Integer.parseInt(voiceTypeStr);
		
		String fileName = "";
		if(voiceType == 1) {
			voiceType = 3;
			fileName = "情感合成-度逍遥.mp3"; 
		}else {
			voiceType = 4;
			fileName = "情感合成-度丫丫.mp3"; 
		}
		
		//识别并返回数据
		byte[] respDate = voiceService.genVoice(text, voiceType);
		
		System.out.println(Arrays.toString(respDate));
		
		
		int bufferSize = 65000;
		//处理音频数据
		ByteArrayInputStream inputstream = new ByteArrayInputStream (respDate);
		byte abyte0[] = new byte[bufferSize];
		//确保响应输出中文不乱吗
		resp.setContentType ("application/octet-stream; charset=utf-8");
		resp.setContentLength ((int) respDate.length);
		resp.setHeader ("Content-Disposition", "attachment;filename=" + new String (fileName.getBytes ("utf-8"), "ISO8859-1"));
		ServletOutputStream out = resp.getOutputStream ();
		//确保识别请求中的中文
		req.setCharacterEncoding("utf-8");
		
		int sum = 0; 
		int k = 0;
		while ((k = inputstream.read (abyte0, 0, bufferSize)) > -1) {
			out.write (abyte0, 0, k); 
			sum += k; 
		} 
		inputstream.close ();
		out.flush (); 
		out.close ();
				
	}
	
}
