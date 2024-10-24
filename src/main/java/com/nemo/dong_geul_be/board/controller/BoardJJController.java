package com.nemo.dong_geul_be.board.controller;


import com.nemo.dong_geul_be.board.domain.entity.Post;
import com.nemo.dong_geul_be.board.service.PostService;
import com.nemo.dong_geul_be.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jejal")
@RequiredArgsConstructor
public class BoardJJController {    //재잘재잘 : 자유게시판

    private final PostService postService;

    //메인페이지
    @GetMapping("") // 재잘재잘은 booelean이 1인 게시글
    public Response<List<Post>> getTruePostTypePosts() {
        List<Post> posts = postService.getTruePostTypePosts();
        return Response.ok(posts);
    }

}
