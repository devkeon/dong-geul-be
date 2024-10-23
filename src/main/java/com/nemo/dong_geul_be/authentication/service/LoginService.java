package com.nemo.dong_geul_be.authentication.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nemo.dong_geul_be.global.exception.BusinessException;
import com.nemo.dong_geul_be.global.response.Code;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import com.nemo.dong_geul_be.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Member user = memberRepository.findMemberByEmail(username).stream()
			.findAny()
			.orElseThrow(() -> new BusinessException(Code.MEMBER_NOT_FOUND));

		return User.builder()
			.username(user.getEmail())
			.password(user.getPassword())
			.roles(user.getRole().getRole())
			.build();
	}
}
