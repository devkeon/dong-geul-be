package com.nemo.dong_geul_be.board.repository;

import com.nemo.dong_geul_be.board.domain.entity.ClubFavorite;
import com.nemo.dong_geul_be.clubAndHeadmem.ClubAndHeadMem;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubFavoriteRepository extends JpaRepository<ClubFavorite, Long> {
    Optional<ClubFavorite> findByMemberAndClub(Member member, ClubAndHeadMem club);

    // 동아리 이름(클럽 이름)만 반환하도록 수정
    @Query("SELECT cf.club.clubName FROM ClubFavorite cf WHERE cf.member.id = :memberId")
    List<String> findClubNamesByMemberId(@Param("memberId") Long memberId);
}