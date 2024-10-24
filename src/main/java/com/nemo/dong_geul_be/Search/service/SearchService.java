package com.nemo.dong_geul_be.Search.service;

import com.nemo.dong_geul_be.board.domain.entity.Post;
import com.nemo.dong_geul_be.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PostRepository postRepository;

    public List<Post> searchPosts(String keyword){
        return postRepository.searchPosts(keyword);
    }
}
