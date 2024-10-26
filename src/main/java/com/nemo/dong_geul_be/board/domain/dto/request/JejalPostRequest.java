package com.nemo.dong_geul_be.board.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JejalPostRequest {
    private Long memberId;     // memberId
    private Boolean isExternal; // 외부 여부
    private String title;      // 제목
    private String content;    // 내용
    private String hashtag;    // 해시태그
}