package com.direct.ai.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.direct.ai.util.ResponseData;


@WebServlet("/Demo")
public class DemoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 请求处理
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, Object> data = new HashMap<>();
		data.put("username", "user");
		data.put("password", "password");
		// 返回接口格式数据
		ResponseData respData = ResponseData.success(data);
		respData.setMsg("登录成功");
		PrintWriter out = response.getWriter();
		String respStr = new JSONObject(respData).toString();
		out.println(respStr);
	}
}
