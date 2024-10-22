package com.nemo.dong_geul_be.member.service;

import com.nemo.dong_geul_be.member.domain.dto.request.AuthEmailRequest;
import com.nemo.dong_geul_be.member.domain.dto.request.SignUpRequest;

import jakarta.mail.MessagingException;

public interface MemberService {

	void authenticateEmail(AuthEmailRequest emailRequest);

	void signUp(SignUpRequest signUpRequest) throws MessagingException;

	void checkNicknameValid(String nickname);

	void checkEmailValid(String email);

	void sendCodeAgain(String email) throws MessagingException;

}
