package com.nemo.dong_geul_be.authentication.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtMemberDetail extends User {

	private Long memberId;

	@Builder(builderMethodName = "JwtMemberDetailBuilder")
	public JwtMemberDetail(String username, String password,
		Collection<? extends GrantedAuthority> authorities, Long memberId) {
		super(username, password, authorities);
		this.memberId = memberId;
	}
}
