package com.nemo.dong_geul_be.board.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;


//게시글 목록이나 검색에서 가져올 DTO
@Data
public class PostDTO {
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int commentCount;
    private Boolean isExternal;
    private String imageUrl; // 이미지 URL 필드 추가

    //Response 간소화
    public PostDTO(String title, String content, LocalDateTime createdAt, int commentCount, Boolean isExternal, String imageUrl) {
        this.title = title; //제목
        this.content = content; //내용
        this.createdAt = createdAt; //작성시간
        this.commentCount = commentCount;   //댓글 수
        this.isExternal = isExternal;   //교내외
        this.imageUrl = imageUrl;   //대표이미지 Url
    }
}