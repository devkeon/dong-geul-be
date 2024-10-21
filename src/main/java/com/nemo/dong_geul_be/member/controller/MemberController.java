package com.nemo.dong_geul_be.member.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nemo.dong_geul_be.global.response.Response;
import com.nemo.dong_geul_be.member.domain.dto.request.AuthEmailRequest;
import com.nemo.dong_geul_be.member.domain.dto.request.EmailRequest;
import com.nemo.dong_geul_be.member.domain.dto.request.SignUpRequest;
import com.nemo.dong_geul_be.member.service.MemberService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/validEmail")
	public Response<Void> validateEmail(@RequestParam("email") String email) {
		log.info("email={}", email);

		memberService.checkEmailValid(email);

		return Response.ok();
	}

	@GetMapping("/validNickname")
	public Response<Void> validateNickname(@RequestParam("nickname") String nickname) {
		memberService.checkNicknameValid(nickname);

		return Response.ok();
	}

	@PostMapping("/sign-up")
	public Response<Void> signUp(@Validated @RequestBody SignUpRequest signUpRequest) throws MessagingException {
		memberService.signUp(signUpRequest);

		return Response.ok();
	}

	@PutMapping("/auth-code")
	public Response<Void> resendAuthCode(@Validated @RequestBody EmailRequest emailRequest) throws MessagingException {
		memberService.sendCodeAgain(emailRequest.getEmail());

		return Response.ok();
	}

	@PutMapping("/authentication")
	public Response<Void> authenticateEmail(@Validated @RequestBody AuthEmailRequest authEmailRequest) {
		memberService.authenticateEmail(authEmailRequest);

		return Response.ok();
	}

}
