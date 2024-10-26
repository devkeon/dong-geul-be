package com.nemo.dong_geul_be.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Code {

	OK("COM-000", "Ok."),

	MEMBER_NOT_FOUND("MEM-001", "Member not found."),
	MEMBER_EMAIL_UNAVAILABLE("MEM-002", "Email cannot used."),
	MEMBER_NICKNAME_UNAVAILABLE("MEM-003", "Nickname cannot used."),
	MEMBER_PASSWORD_UNAVAILABLE("MEM-004", "Password cannot used."),
	MEMBER_ALREADY_ON_PROCESS("MEM-999", "Member is already on process."),

	NOT_ACADEMY_EMAIL("EEM-001", "Email is not a university email."),
	AUTH_CODE_NOT_MATCH("ATH-001", "Auth code not match."),
	MAIL_SERVER_ERROR("MSE-001", "Sending mail got error."),
	ACCESS_TOKEN_NOT_FOUND("ATH-002", "Auth token not found."),
	REFRESH_TOKEN_NOT_FOUND("ATH-003", "Refresh token not found."),
	MEMBER_LOGIN_SESSION_EXPIRED("ATH-004", "Auth session expired."),

	CLUB_NOT_FOUND("CLB-001", "Club not found."),
	CLUB_REQUEST_ALREADY_EXISTS("CLB-002", "Club request already exists."),
	CLUB_HEADMEM_NOT_FOUND("CLB-003", "Club head member not found."),
	CLUB_HEADMEMANDNAME_NOT_FOUND("CLB-004", "요청된 계정은 해당 동아리의 운영진이 아닙니다."),

	SERVER_ERROR("SEV-999", "Check the server.")
	;

	private final String code;
	private final String message;

}
