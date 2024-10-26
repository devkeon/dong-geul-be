package com.nemo.dong_geul_be.board.controller;

import com.nemo.dong_geul_be.authentication.util.SecurityContextUtil;
import com.nemo.dong_geul_be.board.domain.dto.request.CreateCommentRequest;
import com.nemo.dong_geul_be.board.domain.dto.request.DonggeulPostRequest;
import com.nemo.dong_geul_be.board.domain.dto.response.CommentResponse;
import com.nemo.dong_geul_be.board.domain.dto.response.PostDTO;
import com.nemo.dong_geul_be.board.domain.dto.response.PostDetailIMGResponse;
import com.nemo.dong_geul_be.board.domain.entity.Comment;
import com.nemo.dong_geul_be.board.domain.entity.Post;
import com.nemo.dong_geul_be.board.repository.CommentRepository;
import com.nemo.dong_geul_be.board.repository.PostRepository;
import com.nemo.dong_geul_be.board.service.CommentService;
import com.nemo.dong_geul_be.board.service.PostService;
import com.nemo.dong_geul_be.global.response.Response;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import com.nemo.dong_geul_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardDGController {    // 동글동글 : 동아리 홍보

    private final PostService postService;
    private final CommentService commentService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final SecurityContextUtil securityContextUtil; // SecurityContextUtil 추가
    private final MemberRepository memberRepository;

    // 메인페이지
    @GetMapping("/donggeul")
    public Response<List<PostDTO>> getFalsePostTypePosts() {
        List<PostDTO> posts = postService.getFalsePostTypePosts();
        return Response.ok(posts);
    }

    // 동글동글, 교내
    @GetMapping("/donggeul/on")
    public Response<List<PostDTO>> getPostFalseExternalFalsePosts() {
        List<PostDTO> posts = postService.getPostFalseExternalFalsePosts();
        return Response.ok(posts);
    }

    // 동글동글, 교외
    @GetMapping("/donggeul/off")
    public Response<List<PostDTO>> getPostFalseExternalTruePosts() {
        List<PostDTO> posts = postService.getPostFalseExternalTruePosts();
        return Response.ok(posts);
    }

    // 게시글 상세보기
    @GetMapping("/donggeul/{postId}")
    public Response<PostDetailIMGResponse> getPostDetails(@PathVariable Long postId) {

        Post post = postService.getPostById(postId);

        List<String> imageUrls = postService.getImageUrlsByPostId(postId);
        List<Comment> comments = commentService.getCommentsByPost(postId);

        PostDetailIMGResponse.SimplePostDTO simplePostDTO = new PostDetailIMGResponse.SimplePostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getHashtag(),
                post.getPostType(),
                post.getCreatedAt().toString(),
                post.getCommentCount(),
                post.getIsExternal(),
                post.getMember().getId()
        );

        Long postAuthorId = post.getMember().getId();

        List<PostDetailIMGResponse.SimpleCommentDTO> simpleCommentDTOs = comments.stream()
                .map(comment -> new PostDetailIMGResponse.SimpleCommentDTO(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt().toString(),
                        comment.getMember().getId(),
                        comment.getMember().getId().equals(postAuthorId)
                ))
                .collect(Collectors.toList());

        PostDetailIMGResponse response = new PostDetailIMGResponse(simplePostDTO, imageUrls, simpleCommentDTOs);

        return Response.ok(response);
    }

    // 글 작성 (Donggeul) - 이미지 포함
    @PostMapping(value = "/donggeul/create", consumes = "multipart/form-data")
    public Response<PostDetailIMGResponse> createDonggeulPost(
            @RequestParam("isExternal") Boolean isExternal,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("hashtag") String hashtag,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {
        // SecurityContextUtil을 사용하여 현재 로그인된 사용자의 memberId를 가져오기
        Long memberId = securityContextUtil.getContextMemberInfo().getMemberId();

        // 요청 객체 생성 및 필드 설정
        DonggeulPostRequest donggeulPostRequest = new DonggeulPostRequest();
        donggeulPostRequest.setIsExternal(isExternal);
        donggeulPostRequest.setTitle(title);
        donggeulPostRequest.setContent(content);
        donggeulPostRequest.setHashtag(hashtag);
        donggeulPostRequest.setMemberId(memberId);

        // images 필드가 null이 아니고 비어 있지 않은 경우에만 설정
        if (images != null && !images.isEmpty()) {
            donggeulPostRequest.setImages(images);
        }

        // 게시물 생성 서비스 호출
        Post newPost = postService.createDonggeulPost(donggeulPostRequest);

        // 게시물 생성 후 DTO로 응답 구성
        PostDetailIMGResponse.SimplePostDTO simplePostDTO = new PostDetailIMGResponse.SimplePostDTO(
                newPost.getId(),
                newPost.getTitle(),
                newPost.getContent(),
                newPost.getHashtag(),
                newPost.getPostType(),
                newPost.getCreatedAt().toString(),
                newPost.getCommentCount(),
                newPost.getIsExternal(),
                newPost.getMember().getId()
        );

        // 이미지 URL 리스트 가져오기
        List<String> imageUrls = postService.getImageUrlsByPostId(newPost.getId());
        List<PostDetailIMGResponse.SimpleCommentDTO> comments = new ArrayList<>();

        // 최종 응답 객체 생성
        PostDetailIMGResponse response = new PostDetailIMGResponse(simplePostDTO, imageUrls, comments);
        return Response.ok(response);
    }

    // 댓글 작성
    @PostMapping("/donggeul/{postId}/comment")
    public Response<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestBody CreateCommentRequest request) {

        // 현재 로그인한 사용자의 memberId 가져오기
        Long memberId = securityContextUtil.getContextMemberInfo().getMemberId();

        // 요청 객체에 postId와 memberId 설정
        request.setPostId(postId);
        request.setMemberId(memberId);

        // commentService에 postId와 request를 전달
        Comment newComment = commentService.createComment(postId, request);

        // 게시글 작성자 ID와 댓글 작성자 ID 비교하여 isAuthor 값 설정
        boolean isAuthor = newComment.getMember().getId().equals(newComment.getPost().getMember().getId());

        // CommentResponseDto로 변환 후 반환
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