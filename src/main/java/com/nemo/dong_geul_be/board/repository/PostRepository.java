package com.nemo.dong_geul_be.board.repository;

import com.nemo.dong_geul_be.Search.repository.PostRepositoryCustom;
import com.nemo.dong_geul_be.board.domain.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> , PostRepositoryCustom {
    List<Post> findByPostTypeTrueOrderByCreatedAtDesc();

    List<Post> findByPostTypeTrueAndIsExternalFalseOrderByCreatedAtDesc();

    List<Post> findByPostTypeTrueAndIsExternalTrueOrderByCreatedAtDesc();

    List<Post> findByPostTypeFalseOrderByCreatedAtDesc();

    List<Post> findByPostTypeFalseAndIsExternalFalseOrderByCreatedAtDesc();

    List<Post> findByPostTypeFalseAndIsExternalTrueOrderByCreatedAtDesc();

    @Query("SELECT p FROM Post p WHERE p.club IN :clubNames AND p.postType = false ORDER BY p.createdAt DESC")
    List<Post> findByClubIdInAndPostTypeFalse(@Param("clubNames") List<String> clubNames);
}