package com.direct.ai.util.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求数据字符集
 * @author DXK
 *
 */
@WebFilter(filterName="CodingFilter", urlPatterns="/*")
public class CodingFilter extends HttpFilter {
	private static final long serialVersionUID = 1L;
	private static final String reqContentType = "UTF-8";
	private static final String respContentType = "text/json;charset=utf-8";
	
	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 请求数据字符集
		request.setCharacterEncoding(reqContentType);
		// 返回数据字符集
		response.setContentType(respContentType);
		super.doFilter(request, response, chain);
	}
}
