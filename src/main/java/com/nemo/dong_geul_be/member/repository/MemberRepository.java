package com.nemo.dong_geul_be.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nemo.dong_geul_be.member.domain.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findMemberByEmail(String email);

	@Query(nativeQuery = true, value = "select * from member as m where m.nickname=:nickname")
	Optional<Member> findAllMemberByNickname(@Param("nickname") String nickname);

	@Query(nativeQuery = true, value = "select * from member as m join auth_code as ac on ac.auth_code_id=m.auth_code where m.email=:email")
	Optional<Member> findMemberByEmailNonRestriction(@Param("email") String email);

	@Query(nativeQuery = true, value = "select * from member as m where m.email=:email")
	Optional<Member> findAllMemberByEmail(@Param("email") String email);

}
