package com.nemo.dong_geul_be.board.domain.dto.response;

import lombok.Data;

@Data
public class CommentResponse {
    private Long id;
    private Long postId;
    private Long memberId;
    private String content;
    private String createdAt;
    private boolean isAuthor; // 게시글 작성자인지 여부 추가

    public CommentResponse(Long id, Long postId, Long memberId, String content, String createdAt, boolean isAuthor) {
        this.id = id;
        this.postId = postId;
        this.memberId = memberId;
        this.content = content;
        this.createdAt = createdAt;
        this.isAuthor = isAuthor;
    }
}
