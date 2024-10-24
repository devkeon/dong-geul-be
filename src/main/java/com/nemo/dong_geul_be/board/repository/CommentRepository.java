package com.nemo.dong_geul_be.board.repository;

import com.nemo.dong_geul_be.board.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
