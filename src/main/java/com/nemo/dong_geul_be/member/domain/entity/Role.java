package com.nemo.dong_geul_be.member.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

	MEMBER("MEMBER");

	private final String role;

}
