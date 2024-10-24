package com.nemo.dong_geul_be.authentication.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginSuccessResponse {

	private String nickname;

	public static LoginSuccessResponse of(String nickname) {
		return new LoginSuccessResponse(nickname);
	}

}
