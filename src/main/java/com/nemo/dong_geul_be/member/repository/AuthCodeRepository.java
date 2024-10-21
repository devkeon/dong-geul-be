package com.nemo.dong_geul_be.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nemo.dong_geul_be.member.domain.entity.AuthCode;

public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {
}
