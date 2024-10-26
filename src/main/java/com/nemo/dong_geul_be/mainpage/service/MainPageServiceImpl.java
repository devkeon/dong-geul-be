package com.nemo.dong_geul_be.mainpage.service;

import com.nemo.dong_geul_be.board.domain.entity.Post;
import com.nemo.dong_geul_be.mainpage.repository.MainPostRepository;
import com.nemo.dong_geul_be.mainpage.dto.MainPageBoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService {
    private final MainPostRepository mainPostRepository; // 게시물 저장소

    @Override
    public MainPageBoardResponse getDongGeulBoard() {
        // 동글 게시물들을 조회
        List<Post> dongGeulPosts = mainPostRepository.findDongGeulPosts(); // 동글 게시물 조회 메소드 가정
        List<MainPageBoardResponse.MainPostDTO> postDTOs = dongGeulPosts.stream()
                .map(post -> new MainPageBoardResponse.MainPostDTO(post.getTitle(), post.getContent(), post.getCreatedAt()))
                .collect(Collectors.toList());

        return MainPageBoardResponse.builder()
                .code("success") // 예시 코드
                .data(postDTOs)
                .message("DongGeul 게시판이 성공적으로 조회되었습니다.")
                .build();
    }

    @Override
    public MainPageBoardResponse getJaeJalBoard() {
        // 재잘 게시물들을 조회
        List<Post> jaeJalPosts = mainPostRepository.findJaeJalPosts(); // 재잘 게시물 조회 메소드 가정
        List<MainPageBoardResponse.MainPostDTO> postDTOs = jaeJalPosts.stream()
                .map(post -> new MainPageBoardResponse.MainPostDTO(post.getTitle(), post.getContent(), post.getCreatedAt()))
                .collect(Collectors.toList());

        return MainPageBoardResponse.builder()
                .code("success") // 예시 코드
                .data(postDTOs)
                .message("JaeJal 게시판이 성공적으로 조회되었습니다.")
                .build();
    }
}
