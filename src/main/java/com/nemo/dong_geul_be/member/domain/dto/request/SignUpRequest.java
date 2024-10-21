package com.nemo.dong_geul_be.member.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {

	@NotNull
	private String email;
	@NotNull
	private String password;
	private String phoneNumber;
	@NotNull
	private String nickname;

}
