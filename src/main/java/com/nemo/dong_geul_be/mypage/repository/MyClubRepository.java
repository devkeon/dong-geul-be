package com.nemo.dong_geul_be.mypage.repository;

import com.nemo.dong_geul_be.clubAndHeadmem.ClubAndHeadMem;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import com.nemo.dong_geul_be.mypage.domain.IsConfirmed;
import com.nemo.dong_geul_be.mypage.domain.MyClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MyClubRepository extends JpaRepository<MyClub, Long> {

    // 이미 MyClub에 있는지 확인하는 메서드
    @Query("SELECT COUNT(mc) > 0 FROM MyClub mc WHERE mc.member = :member AND mc.clubAndHeadMem = :clubAndHeadMem")
    boolean existsByMemberAndClub(@Param("member") Member member, @Param("clubAndHeadMem") ClubAndHeadMem clubAndHeadMem);


    // 특정 운영진이 관리하는 동아리로 온 상태가 WAITING인 MyClub 목록 조회
    @Query("SELECT mc FROM MyClub mc WHERE mc.clubAndHeadMem.managerEmail = :managerEmail AND mc.isConfirmed = :status")
    List<MyClub> findByClubAndHeadMemAndIsConfirmed(@Param("managerEmail") String managerEmail, @Param("status") IsConfirmed status);

    @Query("SELECT mc FROM MyClub mc WHERE mc.member = :member AND mc.clubAndHeadMem = :clubAndHeadMem")
    Optional<MyClub> findByMemberAndClubAndHeadMem(@Param("member") Member member, @Param("clubAndHeadMem") ClubAndHeadMem clubAndHeadMem);
}
