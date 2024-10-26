package com.nemo.dong_geul_be.board.repository;

import com.nemo.dong_geul_be.board.domain.entity.Post_IMG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<Post_IMG, Long> {

    // 특정 게시글에 속한 이미지 목록을 가져오는 메서드
    List<Post_IMG> findByPostId(Long postId);
}