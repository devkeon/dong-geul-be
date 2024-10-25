package com.nemo.dong_geul_be.mainpage.repository;

import com.nemo.dong_geul_be.board.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MainPostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.postType = false")
    List<Post> findDongGeulPosts();

    @Query("SELECT p FROM Post p WHERE p.postType = true")
    List<Post> findJaeJalPosts();
}