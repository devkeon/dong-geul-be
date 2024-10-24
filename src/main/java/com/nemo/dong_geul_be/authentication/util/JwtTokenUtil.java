package com.nemo.dong_geul_be.authentication.util;

import java.util.Optional;

import org.springframework.security.core.Authentication;

import com.nemo.dong_geul_be.member.domain.entity.Member;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtTokenUtil {

	String generateAccessToken(Authentication authentication);

	String generateRefreshToken();

	Authentication getAuthentication(String accessToken);

	Authentication createAuthentication(Member member);

	Optional<String> extractAccessToken(HttpServletRequest request);

	Optional<String> extractRefreshToken(HttpServletRequest request);

	public boolean validate(String token);

}
