package com.nemo.dong_geul_be.board.repository;

import com.nemo.dong_geul_be.board.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPostTypeTrue();    // 재잘재잘

    List<Post> findByPostTypeFalse();   // 동글동글

    List<Post> findByIsExternalFalse(); // 교내

    List<Post> findByIsExternalTrue();  // 교외

    List<Post> findByPostTypeFalseAndIsExternalFalse(); // 동글동글, 교내

    List<Post> findByPostTypeFalseAndIsExternalTrue();  // 동글동글, 교외

    List<Post> findByPostTypeTrueAndIsExternalFalse();  // 재잘재잘, 교내

    List<Post> findByPostTypeTrueAndIsExternalTrue(); // 재잘재잘, 교외
}
