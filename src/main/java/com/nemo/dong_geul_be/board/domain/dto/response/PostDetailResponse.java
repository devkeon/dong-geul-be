package com.nemo.dong_geul_be.board.domain.dto.response;

import lombok.Getter;

import java.util.List;

// 이미지를 포함하지 않는 Response (Jejal)
@Getter
public class PostDetailResponse {
    private SimplePostDTO post;          // 게시글 정보
    private List<SimpleCommentDTO> comments;  // 댓글 목록

    public PostDetailResponse(SimplePostDTO post, List<SimpleCommentDTO> comments) {
        this.post = post;
        this.comments = comments;
    }

    @Getter
    public static class SimplePostDTO {
        private Long id;
        private String title;
        private String content;
        private String hashtag;
        private Boolean postType;
        private String createdAt;
        private int commentCount;
        private Boolean isExternal;
        private Long memberId;  // memberId 추가

        public SimplePostDTO(Long id, String title, String content, String hashtag, Boolean postType,
                             String createdAt, int commentCount, Boolean isExternal, Long memberId) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.hashtag = hashtag;
            this.postType = postType;
            this.createdAt = createdAt;
            this.commentCount = commentCount;
            this.isExternal = isExternal;
            this.memberId = memberId;  // memberId 설정
        }
    }

    @Getter
    public static class SimpleCommentDTO {
        private Long id;
        private Long memberId;
        private String content;
        private String createdAt;
        private boolean isAuthor;


        public SimpleCommentDTO(Long id, String content, String createdAt, Long memberId, boolean isAuthor) {
            this.id = id;
            this.content = content;
            this.createdAt = createdAt;
            this.memberId = memberId;
            this.isAuthor = isAuthor;
        }
    }
}