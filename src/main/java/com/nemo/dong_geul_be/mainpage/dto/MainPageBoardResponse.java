package com.nemo.dong_geul_be.mainpage.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPageBoardResponse {

    private List<MainPostDTO> posts; // 여러 게시물을 담는 리스트

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MainPostDTO {

        private String title;

        private String content;

        private LocalDateTime createdAt;
    }
}
