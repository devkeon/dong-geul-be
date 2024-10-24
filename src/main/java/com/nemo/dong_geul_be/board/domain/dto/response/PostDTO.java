package com.nemo.dong_geul_be.board.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostDTO {
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int commentCount;
    private Boolean isExternal;
}
