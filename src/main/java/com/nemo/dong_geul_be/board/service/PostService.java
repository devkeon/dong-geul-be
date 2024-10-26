package com.nemo.dong_geul_be.board.service;

import com.nemo.dong_geul_be.board.domain.dto.request.DonggeulPostRequest;
import com.nemo.dong_geul_be.board.domain.dto.request.JejalPostRequest;
import com.nemo.dong_geul_be.board.domain.dto.response.PostDTO;
import com.nemo.dong_geul_be.board.domain.entity.Post;
import com.nemo.dong_geul_be.board.domain.entity.Post_IMG;
import com.nemo.dong_geul_be.board.repository.PostImageRepository;
import com.nemo.dong_geul_be.board.repository.PostRepository;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import com.nemo.dong_geul_be.member.domain.entity.Role;
import com.nemo.dong_geul_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final PostImageRepository postImageRepository;
    private final MemberRepository memberRepository;

    // 재잘재잘 게시글 가져올 때
    public List<PostDTO> getTruePostTypePosts() {
        List<Post> posts = postRepository.findByPostTypeTrue();
        return posts.stream()
                .map(post -> {
                    // 해당 게시글에 연결된 이미지 중 첫 번째 이미지를 가져옴
                    List<Post_IMG> images = postImageRepository.findByPostId(post.getId());
                    String imageUrl = images.isEmpty() ? null : images.get(0).getUrl();

                    return new PostDTO(
                            post.getTitle(),
                            post.getContent(),
                            post.getCreatedAt(),
                            post.getCommentCount(),
                            post.getIsExternal(),
                            imageUrl // 이미지가 있으면 첫 번째 이미지 URL, 없으면 null
                    );
                })
                .collect(Collectors.toList());
    }

    // 재잘재잘 게시글이면서 교내 동아리
    public List<PostDTO> getPostTrueExternalFalsePosts() {
        List<Post> posts = postRepository.findByPostTypeTrueAndIsExternalFalse();
        return posts.stream()
                .map(post -> {
                    List<Post_IMG> images = postImageRepository.findByPostId(post.getId());
                    String imageUrl = images.isEmpty() ? null : images.get(0).getUrl();

                    return new PostDTO(
                            post.getTitle(),
                            post.getContent(),
                            post.getCreatedAt(),
                            post.getCommentCount(),
                            post.getIsExternal(),
                            imageUrl
                    );
                })
                .collect(Collectors.toList());
    }

    // 재잘재잘 게시글이면서 교외 동아리
    public List<PostDTO> getPostTrueExternalTruePosts() {
        List<Post> posts = postRepository.findByPostTypeTrueAndIsExternalTrue();
        return posts.stream()
                .map(post -> {
                    List<Post_IMG> images = postImageRepository.findByPostId(post.getId());
                    String imageUrl = images.isEmpty() ? null : images.get(0).getUrl();

                    return new PostDTO(
                            post.getTitle(),
                            post.getContent(),
                            post.getCreatedAt(),
                            post.getCommentCount(),
                            post.getIsExternal(),
                            imageUrl
                    );
                })
                .collect(Collectors.toList());
    }

    public Post createJejalPost(JejalPostRequest jejalPostRequest) {
        // Member 조회
        Member member = memberRepository.findById(jejalPostRequest.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // Post 생성
        Post post = Post.builder()
                .title(jejalPostRequest.getTitle())
                .content(jejalPostRequest.getContent())
                .hashtag(jejalPostRequest.getHashtag())
                .postType(true)  // 재잘은 무조건 true
                .createdAt(LocalDateTime.now())
                .member(member)  // 조회한 Member 설정
                .commentCount(0)
                .isExternal(jejalPostRequest.getIsExternal())
                .build();

        return postRepository.save(post);
    }



    // 동글동글 게시글 가져올 때
    public List<PostDTO> getFalsePostTypePosts() {
        List<Post> posts = postRepository.findByPostTypeFalse();
        return posts.stream()
                .map(post -> {
                    // 해당 게시글에 연결된 이미지 중 첫 번째 이미지를 가져옴
                    List<Post_IMG> images = postImageRepository.findByPostId(post.getId());
                    String imageUrl = images.isEmpty() ? null : images.get(0).getUrl();

                    return new PostDTO(
                            post.getTitle(),
                            post.getContent(),
                            post.getCreatedAt(),
                            post.getCommentCount(),
                            post.getIsExternal(),
                            imageUrl // 이미지가 있으면 첫 번째 이미지 URL, 없으면 null
                    );
                })
                .collect(Collectors.toList());
    }

    // 동글동글 게시글이면서 교내 동아리
    public List<PostDTO> getPostFalseExternalFalsePosts() {
        List<Post> posts = postRepository.findByPostTypeFalseAndIsExternalFalse();
        return posts.stream()
                .map(post -> {
                    List<Post_IMG> images = postImageRepository.findByPostId(post.getId());
                    String imageUrl = images.isEmpty() ? null : images.get(0).getUrl();

                    return new PostDTO(
                            post.getTitle(),
                            post.getContent(),
                            post.getCreatedAt(),
                            post.getCommentCount(),
                            post.getIsExternal(),
                            imageUrl
                    );
                })
                .collect(Collectors.toList());
    }

    // 동글동글 게시글이면서 교외 동아리
    public List<PostDTO> getPostFalseExternalTruePosts() {
        List<Post> posts = postRepository.findByPostTypeFalseAndIsExternalTrue();
        return posts.stream()
                .map(post -> {
                    List<Post_IMG> images = postImageRepository.findByPostId(post.getId());
                    String imageUrl = images.isEmpty() ? null : images.get(0).getUrl();

                    return new PostDTO(
                            post.getTitle(),
                            post.getContent(),
                            post.getCreatedAt(),
                            post.getCommentCount(),
                            post.getIsExternal(),
                            imageUrl
                    );
                })
                .collect(Collectors.toList());
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));
    }



    // 동글동글 게시판에서 게시글 생성 (post_type = false)
    public Post createDonggeulPost(DonggeulPostRequest request) {
        // memberId로 Member 조회
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 역할 확인: MANAGER가 아니면 예외 발생
        if (!member.getRole().equals(Role.MANAGER)) {
            throw new IllegalArgumentException("Only MANAGER role can create a post");
        }

        Post post = Post.builder()
                .club(member.getManageClubName())
                .isExternal(request.getIsExternal())
                .title(request.getTitle())
                .content(request.getContent())
                .hashtag(request.getHashtag())
                .postType(false)  // 동글동글 게시판은 무조건 false
                .createdAt(LocalDateTime.now())
                .member(member)
                .commentCount(0)
                .build();

        post = postRepository.save(post);

        // 이미지 파일 업로드 처리
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (MultipartFile image : request.getImages()) {
                String imageUrl = s3Service.uploadFile(image);
                Post_IMG postImg = Post_IMG.builder()
                        .post(post)
                        .url(imageUrl)
                        .build();
                postImageRepository.save(postImg);
            }
        }

        return post;
    }


    // 댓글 작성 시 commentCount++
    public void incrementCommentCount(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);
    }

    public List<String> getImageUrlsByPostId(Long postId) {
        // 해당 게시글의 이미지 목록 가져오기
        List<Post_IMG> postImages = postImageRepository.findByPostId(postId);

        // 이미지 URL만 추출하여 리스트로 반환
        return postImages.stream()
                .map(Post_IMG::getUrl)
                .collect(Collectors.toList());
    }
}