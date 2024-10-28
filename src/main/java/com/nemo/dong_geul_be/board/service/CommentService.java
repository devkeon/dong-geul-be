package com.nemo.dong_geul_be.board.service;

import com.nemo.dong_geul_be.board.domain.dto.request.CreateCommentRequest;
import com.nemo.dong_geul_be.board.domain.entity.Comment;
import com.nemo.dong_geul_be.board.domain.entity.Post;
import com.nemo.dong_geul_be.board.repository.CommentRepository;
import com.nemo.dong_geul_be.board.repository.PostRepository;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import com.nemo.dong_geul_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    private final PostService postService;

    public Comment createComment(Long postId, CreateCommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .member(member)
                .createdAt(LocalDateTime.now())
                .build();

        postService.incrementCommentCount(postId);

        return commentRepository.save(comment);
    }


    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}