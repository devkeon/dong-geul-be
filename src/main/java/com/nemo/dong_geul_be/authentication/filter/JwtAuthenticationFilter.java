package com.nemo.dong_geul_be.authentication.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nemo.dong_geul_be.authentication.util.JwtTokenUtil;
import com.nemo.dong_geul_be.global.exception.BusinessException;
import com.nemo.dong_geul_be.global.response.Code;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import com.nemo.dong_geul_be.member.repository.MemberRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenUtil jwtTokenUtil;
	private final MemberRepository memberRepository;

	@Value("${jwt.token.expire.refresh}")
	private Integer COOKIE_EXPIRATION;

	private static String GRANT_TYPE = "Bearer ";

	protected List<String> filterPassList = List.of("/favicon.ico", "/api/login", "/api/validEmail",
		"/api/validNickname", "/api/sign-up", "/api/auth-code", "/api/authentication", "/v3/api-docs");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		log.info("uri={}", request.getRequestURI());

		if (filterPassList.contains(request.getRequestURI()) || request.getRequestURI().contains("swagger")){
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = jwtTokenUtil.extractAccessToken(request).stream()
			.findAny()
			.orElseThrow(() -> new BusinessException(Code.ACCESS_TOKEN_NOT_FOUND));

		Authentication authentication;

		// 정상 흐름
		try{
			authentication = jwtTokenUtil.getAuthentication(accessToken);

			SecurityContextHolder.getContext().setAuthentication(authentication);

			String refreshToken = String.valueOf(jwtTokenUtil.extractRefreshToken(request));

			if (refreshToken == null){
				throw new BusinessException(Code.REFRESH_TOKEN_NOT_FOUND);
			}

			response.setHeader("Authorization", GRANT_TYPE + accessToken);
			response.setHeader("Set-Cookie", refreshToken);

			// access token 만료 흐름
		} catch (ExpiredJwtException e){

			log.info("access token expired = {}", accessToken);

			Claims claims = e.getClaims();

			String refreshToken = jwtTokenUtil.extractRefreshToken(request).stream()
				.findAny()
				.orElseThrow(() -> new BusinessException(Code.REFRESH_TOKEN_NOT_FOUND));

			if (!jwtTokenUtil.validate(refreshToken)){
				throw new BusinessException(Code.MEMBER_LOGIN_SESSION_EXPIRED);
			}

			Member currentMember = memberRepository.findById(Long.parseLong(claims.get("id").toString())).stream()
				.findAny()
				.orElseThrow(() -> new BusinessException(Code.MEMBER_NOT_FOUND));

			if (!currentMember.getRefreshToken().equals(refreshToken)){
				throw new BusinessException(Code.MEMBER_LOGIN_SESSION_EXPIRED);
			}

			String generateRefreshToken = jwtTokenUtil.generateRefreshToken();

			currentMember.updateRefreshToken(generateRefreshToken);
			memberRepository.save(currentMember);

			Authentication createdAuthentication = jwtTokenUtil.createAuthentication(currentMember);

			String generatedAccessToken = jwtTokenUtil.generateAccessToken(createdAuthentication);

			response.setHeader("Authorization", generatedAccessToken);

			ResponseCookie cookie = ResponseCookie.from("refreshToken", generateRefreshToken)
				.path("/")
				.httpOnly(false)
				.maxAge(COOKIE_EXPIRATION)
				.sameSite("Lax")
				.secure(false)
				.build();

			response.setHeader("Set-Cookie", String.valueOf(cookie));

			SecurityContextHolder.getContext().setAuthentication(createdAuthentication);

		} catch (Exception e){
			throw new RuntimeException(e);
		}

		filterChain.doFilter(request, response);

	}
}
