package com.nemo.dong_geul_be.board.service;

import com.nemo.dong_geul_be.board.domain.entity.Comment;
import com.nemo.dong_geul_be.board.domain.entity.Post;
import com.nemo.dong_geul_be.board.repository.CommentRepository;
import com.nemo.dong_geul_be.board.repository.PostRepository;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PostService postService;

    //로그인이 아직 없기에 member 지움
    public Comment createComment(Long postId, String content
                                 //, Member member
    ) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        Comment comment = Comment.builder()
                .content(content)
                .post(post)
                //.member(member)
                .createdAt(LocalDateTime.now())
                .build();

        postService.incrementCommentCount(postId);

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}
