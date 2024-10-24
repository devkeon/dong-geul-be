package com.nemo.dong_geul_be.board.service;

import com.nemo.dong_geul_be.board.domain.dto.request.CreatePostRequest;
import com.nemo.dong_geul_be.board.domain.dto.response.PostDTO;
import com.nemo.dong_geul_be.board.domain.entity.Post;
import com.nemo.dong_geul_be.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 재잘재잘 게시글 가져올 때
    public List<Post> getTruePostTypePosts() {  //재잘재잘
        return postRepository.findByPostTypeTrue();
    }

    // 동글동글 게시글 가져올 때
    public List<PostDTO> getFalsePostTypePosts() {
        List<Post> posts = postRepository.findByPostTypeFalse();
        return posts.stream()
                .map(post -> new PostDTO(       //게시글 목록으로 제목, 내용, 날짜, 댓글 수, 교외/교내 분류
                        post.getTitle(),
                        post.getContent(),
                        post.getCreatedAt(),
                        post.getCommentCount(),
                        post.getIsExternal()
                ))
                .collect(Collectors.toList());
    }

    // 동글동글 게시글이면서 교내 동아리
    public List<PostDTO> getPostFalseExternalFalsePosts() {
        List<Post> posts = postRepository.findByPostTypeFalseAndIsExternalFalse();
        return posts.stream()
                .map(post -> new PostDTO(
                        post.getTitle(),
                        post.getContent(),
                        post.getCreatedAt(),
                        post.getCommentCount(),
                        post.getIsExternal()
                ))
                .collect(Collectors.toList());
    }

    // 동글동글 게시글이면서 교외 동아리
    public List<PostDTO> getPostFalseExternalTruePosts() {
        List<Post> posts = postRepository.findByPostTypeFalseAndIsExternalTrue();
        return posts.stream()
                .map(post -> new PostDTO(
                        post.getTitle(),
                        post.getContent(),
                        post.getCreatedAt(),
                        post.getCommentCount(),
                        post.getIsExternal()
                ))
                .collect(Collectors.toList());
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));
    }

    // 로그인이 없어 우선 테스트로 member 지움
    // 동글동글 게시판에서 게시글 생성 (post_type = false)
    public Post createDonggeulPost(CreatePostRequest request
                                   //, Member member
    ) {
        Post post = Post.builder()
                .isExternal(request.getIsExternal())
                .title(request.getTitle())
                .content(request.getContent())
                .hashtag(request.getHashtag())
                .postType(false)  // 동글동글 게시판은 무조건 false
                .createdAt(LocalDateTime.now())
                //.member(member)
                .commentCount(0)
                .build();

        return postRepository.save(post);
    }

    // 댓글 작성 시 commentCount++
    public void incrementCommentCount(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);
    }
}
