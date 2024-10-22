package com.nemo.dong_geul_be.member.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nemo.dong_geul_be.global.exception.BusinessException;
import com.nemo.dong_geul_be.global.response.Code;
import com.nemo.dong_geul_be.member.domain.dto.request.AuthEmailRequest;
import com.nemo.dong_geul_be.member.domain.dto.request.SignUpRequest;
import com.nemo.dong_geul_be.member.domain.entity.AuthCode;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import com.nemo.dong_geul_be.member.domain.entity.Role;
import com.nemo.dong_geul_be.member.repository.AuthCodeRepository;
import com.nemo.dong_geul_be.member.repository.MemberRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final AuthCodeRepository authCodeRepository;
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender javaMailSender;
	private final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+~`|}{$begin:math:display$$end:math:display$:;?><,./-])[A-Za-z\\d!@#$%^&*()_+~`|}{\\[\\]:;?><,./-]{8,14}$";

	@Value("${spring.mail.username}")
	private String senderEmail;

	@Override
	public void authenticateEmail(AuthEmailRequest emailRequest) {

		Member authMember = memberRepository.findMemberByEmailNonRestriction(emailRequest.getEmail()).stream()
			.findAny()
			.orElseThrow(() -> new BusinessException(Code.MEMBER_NOT_FOUND));

		if (!authMember.getAuthCode().getCode().equals(emailRequest.getAuthCode())) {
			throw new BusinessException(Code.AUTH_CODE_NOT_MATCH);
		}

		authMember.authComplete();

	}

	@Override
	public void signUp(SignUpRequest signUpRequest) throws MessagingException {

		// 회원가입 전 유효성 체크
		checkEmailValid(signUpRequest.getEmail());
		checkNicknameValid(signUpRequest.getNickname());

		if (!signUpRequest.getPassword().matches(passwordRegex)){
			throw new BusinessException(Code.MEMBER_PASSWORD_UNAVAILABLE);
		}

		// 회원가입 시작 (영속)
		Member signUpMember = Member.builder()
			.email(signUpRequest.getEmail())
			.nickname(signUpRequest.getNickname())
			.password(passwordEncoder.encode(signUpRequest.getPassword()))
			.role(Role.MEMBER)
			.phoneNumber(signUpRequest.getPhoneNumber())
			.build();

		Member persistedMember = memberRepository.save(signUpMember);

		// 인증 코드 생성
		String authCode = getCode();

		AuthCode authCodeEntity = AuthCode.builder()
			.member(persistedMember)
			.code(authCode)
			.build();

		authCodeRepository.save(authCodeEntity);

		persistedMember.updateAuthCode(authCodeEntity);

		// 인증 코드 발송
		sendEmail(authCode, persistedMember.getEmail());
	}

	@Override
	public void checkNicknameValid(String nickname) {

		if (nickname.length() > 8) {
			throw new BusinessException(Code.MEMBER_NICKNAME_UNAVAILABLE);
		}

		memberRepository.findAllMemberByNickname(nickname).ifPresent(member -> {
			throw new BusinessException(Code.MEMBER_NICKNAME_UNAVAILABLE);
		});

	}

	@Override
	public void checkEmailValid(String email) {

		if (!email.matches("^[a-zA-Z0-9._%+-]+@catholic\\.ac\\.kr$")) {
			throw new BusinessException(Code.NOT_ACADEMY_EMAIL);
		}

		memberRepository.findAllMemberByEmail(email).ifPresent(member -> {
			if (member.getAuth()) throw new BusinessException(Code.MEMBER_EMAIL_UNAVAILABLE);
			throw new BusinessException(Code.MEMBER_ALREADY_ON_PROCESS);
		});

	}

	@Override
	public void sendCodeAgain(String email) throws MessagingException {

		Member member = memberRepository.findAllMemberByEmail(email).stream()
			.findAny()
			.orElseThrow(() -> new BusinessException(Code.MEMBER_NOT_FOUND));

		String code = getCode();

		AuthCode authCode = member.getAuthCode();
		authCode.updateCode(code);

		sendEmail(code, email);

	}

	private void sendEmail(String code, String toEmail) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();

		message.setFrom(senderEmail);
		message.setRecipients(MimeMessage.RecipientType.TO, toEmail);
		message.setSubject("이메일 인증");
		String body = "";
		body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
		body += "<h1>" + code + "</h1>";
		body += "<h3>" + "감사합니다." + "</h3>";
		message.setText(body,"UTF-8", "html");

		javaMailSender.send(message);
	}

	private String getCode() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			stringBuilder.append(ThreadLocalRandom.current().nextInt(10));
		}

		return stringBuilder.toString();
	}

}
