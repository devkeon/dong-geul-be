package com.nemo.dong_geul_be.board.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
//동글동글(이미지 o)을 위한 Request
public class DonggeulPostRequest {
    private Boolean isExternal;
    private String title;
    private String content;
    private String hashtag;
    private List<MultipartFile> images;
    private Long memberId;
}