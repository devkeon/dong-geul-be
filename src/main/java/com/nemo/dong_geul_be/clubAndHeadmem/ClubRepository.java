package com.nemo.dong_geul_be.clubAndHeadmem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<ClubAndHeadMem, Long> {
}
