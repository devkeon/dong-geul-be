package com.nemo.dong_geul_be.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nemo.dong_geul_be.authentication.filter.JsonAuthenticationFilter;
import com.nemo.dong_geul_be.authentication.filter.JwtAuthenticationFilter;
import com.nemo.dong_geul_be.authentication.handler.jwt.LoginFailureHandler;
import com.nemo.dong_geul_be.authentication.handler.jwt.LoginSuccessHandler;
import com.nemo.dong_geul_be.authentication.util.JwtTokenUtil;
import com.nemo.dong_geul_be.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final MemberRepository memberRepository;
	private final JwtTokenUtil jwtTokenUtil;
	private final UserDetailsService loginService;
	private final ObjectMapper objectMapper;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.formLogin(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.cors((cors) ->
				cors.configurationSource(corsConfiguration()))
			.headers((headers) ->
				headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
			)
			.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests((authorizeHttpRequests) ->
				authorizeHttpRequests
					.requestMatchers("/**")
					.permitAll()
					.anyRequest()
					.authenticated()
			);
		httpSecurity.addFilterAfter(jsonAuthenticationFilter(), LogoutFilter.class);
		httpSecurity.addFilterBefore(jwtAuthenticationFilter(), LogoutFilter.class);

		return httpSecurity.build();

	}

	@Bean
	public CorsConfigurationSource corsConfiguration() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();

		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addExposedHeader("Authorization");
		corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		corsConfiguration.setAllowedOrigins(List.of(
			"http://localhost:3000",
			"http://localhost:8080"
		));
		corsConfiguration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", corsConfiguration);

		return source;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtTokenUtil, memberRepository);
	}

	@Bean
	public JsonAuthenticationFilter jsonAuthenticationFilter() {
		JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter(objectMapper);

		jsonAuthenticationFilter.setAuthenticationManager(authenticationManager());
		jsonAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
		jsonAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());

		return jsonAuthenticationFilter;
	}

	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler(memberRepository, jwtTokenUtil, objectMapper);
	}

	@Bean
	public LoginFailureHandler loginFailureHandler() {
		return new LoginFailureHandler(objectMapper);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(loginService);
		return new ProviderManager(provider);
	}

}
