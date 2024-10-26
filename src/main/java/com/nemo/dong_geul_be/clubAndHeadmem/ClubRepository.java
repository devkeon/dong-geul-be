package com.nemo.dong_geul_be.clubAndHeadmem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<ClubAndHeadMem, Long> {
    Optional<ClubAndHeadMem> findClubAndHeadMemByClubName(String clubName);

    //헤드 이메일로 클럽 이름 찾기
    Optional<ClubAndHeadMem> findClubAndHeadMemByManagerEmail(String managerEmail);
}
