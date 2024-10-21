package com.nemo.dong_geul_be.member.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCode {

	@Id @Column(name = "auth_code_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String code;

	@OneToOne(mappedBy = "authCode")
	private Member member;

	public void updateMember(Member member) {
		if (this.member != member) {
			this.member = member;
		}
		if (member.getAuthCode() != this) {
			member.updateAuthCode(this);
		}
	}

	public void updateCode(String newCode) {
		this.code = newCode;
	}

}
