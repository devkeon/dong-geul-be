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

        DonggeulPostRequest donggeulPostRequest = new DonggeulPostRequest();
        donggeulPostRequest.setIsExternal(isExternal);
        donggeulPostRequest.setTitle(title);
        donggeulPostRequest.setContent(content);
        donggeulPostRequest.setHashtag(hashtag);
        donggeulPostRequest.setImages(images);
        donggeulPostRequest.setMemberId(memberId);

        Post newPost = postService.createDonggeulPost(donggeulPostRequest);

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

        List<String> imageUrls = postService.getImageUrlsByPostId(newPost.getId());
        List<PostDetailIMGResponse.SimpleCommentDTO> comments = new ArrayList<>();

        PostDetailIMGResponse response = new PostDetailIMGResponse(simplePostDTO, imageUrls, comments);
        return Response.ok(response);
    }

    // 댓글 작성
    @PostMapping("/donggeul/{postId}/comment")
    public CommentResponse createComment(@PathVariable Long postId, @RequestBody CreateCommentRequest request) {
        // SecurityContextUtil을 사용하여 현재 로그인된 사용자의 memberId를 가져오기
        Long memberId = securityContextUtil.getContextMemberInfo().getMemberId();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .member(member)
                .createdAt(LocalDateTime.now())
                .build();

        postService.incrementCommentCount(postId);
        commentRepository.save(comment);

        boolean isAuthor = member.getId().equals(post.getMember().getId());

        return new CommentResponse(
                comment.getId(),
                postId,
                member.getId(),
                comment.getContent(),
                comment.getCreatedAt().toString(),
                isAuthor
        );
    }
}
