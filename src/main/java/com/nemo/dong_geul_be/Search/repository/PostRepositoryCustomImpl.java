package com.nemo.dong_geul_be.Search.repository;

import com.nemo.dong_geul_be.board.domain.entity.Post;
import com.nemo.dong_geul_be.board.domain.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> searchPosts(String keyword) {
        QPost post = QPost.post;

        return queryFactory.selectFrom(post)
                .where(
                        post.title.containsIgnoreCase(keyword)
                                .or(post.content.containsIgnoreCase(keyword))
                                .or(post.hashtag.containsIgnoreCase(keyword))
                )
                .fetch();
    }
}
