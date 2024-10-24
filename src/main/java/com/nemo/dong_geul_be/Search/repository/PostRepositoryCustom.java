package com.nemo.dong_geul_be.Search.repository;

import com.nemo.dong_geul_be.board.domain.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> searchPosts(String keyword);
}
