package com.nemo.dong_geul_be.board.domain.dto.response;

import lombok.Getter;
import java.util.List;

// 간단화된 Response (동글)
@Getter
public class PostDetailIMGResponse {
    private SimplePostDTO post;           // 간단한 게시글 정보
    private List<String> imageUrls;       // 이미지 URL 목록
    private List<SimpleCommentDTO> comments; // 간단한 댓글 목록

    public PostDetailIMGResponse(SimplePostDTO post, List<String> imageUrls, List<SimpleCommentDTO> comments) {
        this.post = post;
        this.imageUrls = imageUrls;
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
        private Integer commentCount;
        private Boolean isExternal;
        private Long memberId;

        public SimplePostDTO(Long id, String title, String content, String hashtag, Boolean postType, String createdAt, Integer commentCount, Boolean isExternal, Long memberId) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.hashtag = hashtag;
            this.postType = postType;
            this.createdAt = createdAt;
            this.commentCount = commentCount;
            this.isExternal = isExternal;
            this.memberId = memberId;
        }
    }

    @Getter
    public static class SimpleCommentDTO {
        private Long id;
        private String content;
        private String createdAt;
        private Long memberId;
        private boolean isAuthor;  // 댓글 작성자가 게시글 작성자인지 여부

        public SimpleCommentDTO(Long id, String content, String createdAt, Long memberId, boolean isAuthor) {
            this.id = id;
            this.content = content;
            this.createdAt = createdAt;
            this.memberId = memberId;
            this.isAuthor = isAuthor;
        }
    }
}
