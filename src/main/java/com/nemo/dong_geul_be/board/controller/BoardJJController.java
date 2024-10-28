package com.nemo.dong_geul_be.board.controller;


import com.nemo.dong_geul_be.authentication.util.SecurityContextUtil;
import com.nemo.dong_geul_be.board.domain.dto.request.CreateCommentRequest;
import com.nemo.dong_geul_be.board.domain.dto.request.JejalPostRequest;
import com.nemo.dong_geul_be.board.domain.dto.response.CommentResponse;
import com.nemo.dong_geul_be.board.domain.dto.response.PostDTO;
import com.nemo.dong_geul_be.board.domain.dto.response.PostDetailResponse;
import com.nemo.dong_geul_be.board.domain.entity.Comment;
import com.nemo.dong_geul_be.board.domain.entity.Post;
import com.nemo.dong_geul_be.board.repository.CommentRepository;
import com.nemo.dong_geul_be.board.repository.PostRepository;
import com.nemo.dong_geul_be.board.service.CommentService;
import com.nemo.dong_geul_be.board.service.PostService;
import com.nemo.dong_geul_be.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardJJController {    //재잘재잘 : 자유게시판

    private final PostService postService;
    private final CommentService commentService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final SecurityContextUtil securityContextUtil;

    //메인페이지
    @GetMapping("/jejal") // 재잘재잘은 booelean이 1인 게시글
    public Response<List<PostDTO>> getTruePostTypePosts() {
        List<PostDTO> posts = postService.getTruePostTypePosts();

        return Response.ok(posts);
    }

    // 재잘재잘, 교내
    @GetMapping("/jejal/on")
    public Response<List<PostDTO>> getPostTrueExternalFalsePosts() {
        List<PostDTO> posts = postService.getPostTrueExternalFalsePosts();
        return Response.ok(posts);
    }

    // 재잘재잘, 교외
    @GetMapping("/jejal/off")
    public Response<List<PostDTO>> getPostTrueExternalTruePosts() {
        List<PostDTO> posts = postService.getPostTrueExternalTruePosts();
        return Response.ok(posts);
    }

    // 게시글 상세보기
    @GetMapping("/jejal/{postId}")
    public Response<PostDetailResponse> getPostDetails(@PathVariable Long postId) {

        Post post = postService.getPostById(postId);

        PostDetailResponse.SimplePostDTO postDto = new PostDetailResponse.SimplePostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getHashtag(),
                post.getPostType(),
                post.getCreatedAt().toString(),
                post.getCommentCount(),
                post.getIsExternal(),
                post.getMember().getId()  // memberId 추가
        );

        List<PostDetailResponse.SimpleCommentDTO> commentDtos = commentService.getCommentsByPost(postId).stream()
                .map(comment -> new PostDetailResponse.SimpleCommentDTO(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt().toString(),
                        comment.getMember().getId(),
                        comment.getMember().getId().equals(post.getMember().getId()) // 게시글 작성자와 댓글 작성자 비교
                ))
                .collect(Collectors.toList());

        PostDetailResponse response = new PostDetailResponse(postDto, commentDtos);

        return Response.ok(response);
    }


    // 글 작성 (Jejal) - 이미지 없이
    @PostMapping("/jejal/create")
    public Response<PostDetailResponse.SimplePostDTO> createJejalPost(@RequestBody JejalPostRequest jejalPostRequest) {

        Long memberId = securityContextUtil.getContextMemberInfo().getMemberId();

        jejalPostRequest.setMemberId(memberId);

        Post newPost = postService.createJejalPost(jejalPostRequest);

        // 응답 DTO 생성
        PostDetailResponse.SimplePostDTO simplePostDTO = new PostDetailResponse.SimplePostDTO(
                newPost.getId(),
                newPost.getTitle(),
                newPost.getContent(),
                newPost.getHashtag(),
                newPost.getPostType(),
                newPost.getCreatedAt().toString(),
                newPost.getCommentCount(),
                newPost.getIsExternal(),
                newPost.getMember().getId()  // memberId 추가
        );

        // 응답 반환
        return Response.ok(simplePostDTO);
    }

    @PostMapping("/jejal/{postId}/comment")
    public Response<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestBody CreateCommentRequest request) {

        Long memberId = securityContextUtil.getContextMemberInfo().getMemberId();

        request.setMemberId(memberId);

        Comment newComment = commentService.createComment(postId, request);

        boolean isAuthor = newComment.getMember().getId().equals(newComment.getPost().getMember().getId());

        CommentResponse responseDto = new CommentResponse(
                newComment.getId(),
                postId,
                newComment.getMember().getId(),
                newComment.getContent(),
                newComment.getCreatedAt().toString(),
                isAuthor
        );

        return Response.ok(responseDto);
    }

}
