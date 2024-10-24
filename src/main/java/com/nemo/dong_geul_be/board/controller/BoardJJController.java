package com.nemo.dong_geul_be.board.controller;


import com.nemo.dong_geul_be.board.domain.dto.request.CreatePostRequest;
import com.nemo.dong_geul_be.board.domain.dto.response.PostDTO;
import com.nemo.dong_geul_be.board.domain.dto.response.PostDetailResponse;
import com.nemo.dong_geul_be.board.domain.entity.Comment;
import com.nemo.dong_geul_be.board.domain.entity.Post;
import com.nemo.dong_geul_be.board.service.CommentService;
import com.nemo.dong_geul_be.board.service.PostService;
import com.nemo.dong_geul_be.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardJJController {    //재잘재잘 : 자유게시판

    private final PostService postService;
    private final CommentService commentService;

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
        List<Comment> comments = commentService.getCommentsByPost(postId);

        PostDetailResponse response = new PostDetailResponse(post, comments);
        return Response.ok(response);
    }

    // 글 작성
    @PostMapping("/jejal/create")
    public Response<Post> createJejalPost(@RequestBody CreatePostRequest createPostRequest) {
        Post newPost = postService.createJejalPost(createPostRequest);
        return Response.ok(newPost);
    }

    // 댓글 작성
    @PostMapping("/jejal/{postId}/comment")
    public Response<Comment> createComment(@PathVariable Long postId, @RequestBody String content) {
        Comment newComment = commentService.createComment(postId, content);
        return Response.ok(newComment);
    }

}
