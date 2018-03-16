package com.auth0.samples.authapi.security;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class CustomFilter extends GenericFilterBean {
	/**
	 * The Origin header indicates where the cross-origin request or preflight
	 * request originates from.
	 */
	public static final String REQUEST_HEADER_ORIGIN = "Origin";
	@Override
	public void doFilter(
			ServletRequest servletRequest,
			ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String originHeader = request.getHeader(REQUEST_HEADER_ORIGIN);
		chain.doFilter(servletRequest, response);
	}
}
