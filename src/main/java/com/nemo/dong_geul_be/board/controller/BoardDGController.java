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
@RequestMapping("/api/donggeul")
@RequiredArgsConstructor
public class BoardDGController {    //동글동글 : 동아리 홍보

    private final PostService postService;
    private final CommentService commentService;

    // 메인페이지
    @GetMapping("")
    public Response<List<PostDTO>> getFalsePostTypePosts() {
        List<PostDTO> posts = postService.getFalsePostTypePosts();
        return Response.ok(posts);
    }

    // 동글동글, 교내
    @GetMapping("/on")
    public Response<List<PostDTO>> getPostFalseExternalFalsePosts() {
        List<PostDTO> posts = postService.getPostFalseExternalFalsePosts();
        return Response.ok(posts);
    }

    // 동글동글, 교외
    @GetMapping("/off")
    public Response<List<PostDTO>> getPostFalseExternalTruePosts() {
        List<PostDTO> posts = postService.getPostFalseExternalTruePosts();
        return Response.ok(posts);
    }

    // 게시글 상세보기
    @GetMapping("/{postId}")
    public Response<PostDetailResponse> getPostDetails(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        List<Comment> comments = commentService.getCommentsByPost(postId);

        PostDetailResponse response = new PostDetailResponse(post, comments);
        return Response.ok(response);
    }

    // 글 작성
    @PostMapping("/create")
    public Response<Post> createDonggeulPost(@RequestBody CreatePostRequest createPostRequest) {
        Post newPost = postService.createDonggeulPost(createPostRequest);
        return Response.ok(newPost);
    }

    // 댓글 작성
    @PostMapping("/{postId}/comment")
    public Response<Comment> createComment(@PathVariable Long postId, @RequestBody String content) {
        Comment newComment = commentService.createComment(postId, content);
        return Response.ok(newComment);
    }

}
