package com.nemo.dong_geul_be.board.domain.dto.response;

import com.nemo.dong_geul_be.board.domain.entity.Comment;
import com.nemo.dong_geul_be.board.domain.entity.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class PostDetailResponse {
    private Post post;
    private List<Comment> comments;

    public PostDetailResponse(Post post, List<Comment> comments) {
        this.post = post;
        this.comments = comments;
    }
}