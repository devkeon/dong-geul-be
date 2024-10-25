package com.nemo.dong_geul_be.mainpage.controller;


import com.nemo.dong_geul_be.mainpage.dto.MainPageBoardResponse;
import com.nemo.dong_geul_be.mainpage.service.MainPageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/mainPage")
@RequiredArgsConstructor
public class MainPageController {
    private final MainPageService mainPageService;


    @Operation(summary = "메인 페이지", description = "메인 페이지 내 동글 게시판의 정보를 반환합니다.")
    @GetMapping("/donggeul")
    public ResponseEntity<MainPageBoardResponse> getDongGeulBoard() {
        List<MainPageBoardResponse.MainPostDTO> posts = mainPageService.getDongGeulBoard().getPosts();

        return ResponseEntity.ok(new MainPageBoardResponse(posts)); // 200
    }

    @Operation(summary = "메인 페이지", description = "메인 페이지 내 재잘 게시판의 정보를 반환합니다.")
    @GetMapping("/jaejal")
    public ResponseEntity<MainPageBoardResponse> getJaeJalBoard() {
        List<MainPageBoardResponse.MainPostDTO> posts = mainPageService.getJaeJalBoard().getPosts();

        return ResponseEntity.ok(new MainPageBoardResponse(posts)); // 200
    }
}






