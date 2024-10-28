package com.nemo.dong_geul_be.clubAndHeadmem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<ClubAndHeadMem, Long> {
    Optional<ClubAndHeadMem> findClubAndHeadMemByClubName(String clubName);

    Optional<ClubAndHeadMem> findClubAndHeadMemByManagerEmail(String managerEmail);

    Optional<ClubAndHeadMem> findClubAndHeadMemByManagerEmailAndClubName(String managerEmail, String clubName);

    Optional<ClubAndHeadMem> findByClubName(String clubName);
}
