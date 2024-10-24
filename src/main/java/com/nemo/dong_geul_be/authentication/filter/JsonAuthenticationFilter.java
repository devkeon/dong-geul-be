package com.nemo.dong_geul_be.authentication.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final String LOGIN_REQUEST_URI = "/api/login";
	private static final String CONTENT_TYPE = "application/json";
	private static final String HTTP_METHOD = "POST";
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
		new AntPathRequestMatcher(LOGIN_REQUEST_URI, HTTP_METHOD);

	private final ObjectMapper objectMapper;

	public JsonAuthenticationFilter(ObjectMapper objectMapper) {
		super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
		this.objectMapper = objectMapper;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException,
		IOException,
		ServletException {
		if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
			throw new AuthenticationServiceException("Authentication content type not supported: " + request.getContentType());
		}

		String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

		Map<String, String> emailPasswordMap = objectMapper.readValue(messageBody, Map.class);

		String email = emailPasswordMap.get(EMAIL);
		String password = emailPasswordMap.get(PASSWORD);

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);

		return this.getAuthenticationManager().authenticate(authRequest);
	}
}
