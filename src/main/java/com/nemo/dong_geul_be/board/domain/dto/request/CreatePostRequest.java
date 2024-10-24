package com.nemo.dong_geul_be.board.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequest {
    private Boolean isExternal;
    private String title;
    private String content;
    private String hashtag;
}