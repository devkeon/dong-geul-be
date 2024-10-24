package com.nemo.dong_geul_be.authentication.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nemo.dong_geul_be.authentication.domain.JwtMemberDetail;

import lombok.Getter;

@Getter
@Component
public class SimpleSecurityContextUtil implements SecurityContextUtil {

	@Override
	public JwtMemberDetail getContextMemberInfo() {
		return (JwtMemberDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
