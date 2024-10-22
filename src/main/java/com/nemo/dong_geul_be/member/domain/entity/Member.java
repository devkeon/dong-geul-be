package com.nemo.dong_geul_be.member.domain.entity;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction(value = "auth <> false")
public class Member {

	@Id @Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Email
	@NotNull
	@Column(unique = true)
	private String email;
	@NotNull
	private String nickname;
	@NotNull
	private String password;
	private String phoneNumber;
	private String refreshToken;
	@NotNull
	@Enumerated(value = EnumType.STRING)
	private Role role;
	@NotNull
	@Builder.Default
	private Boolean auth = false;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auth_code")
	private AuthCode authCode;

	public void updatePassword(String encryptPassword) {
		this.password = encryptPassword;
	}

	public void updatePhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void authComplete() {
		this.auth = true;
	}

	public void updateAuthCode(AuthCode authCode) {
		if (this.authCode != authCode) {
			this.authCode = authCode;
		}
		if (authCode.getMember() != this){
			authCode.updateMember(this);
		}

	}

}
