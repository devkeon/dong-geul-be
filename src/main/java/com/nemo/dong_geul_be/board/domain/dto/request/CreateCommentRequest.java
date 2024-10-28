package com.nemo.dong_geul_be.board.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {
    private Long memberId;
    private String content;
}