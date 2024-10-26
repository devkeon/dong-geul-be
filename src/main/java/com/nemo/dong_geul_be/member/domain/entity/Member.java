package com.nemo.dong_geul_be.member.domain.entity;

import com.nemo.dong_geul_be.mypage.domain.MyClub;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction(value = "auth <> false")
@Setter
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

	private String manageClubName;

	@OneToMany(mappedBy = "member")
	private List<MyClub> myClubs = new ArrayList<>();

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
