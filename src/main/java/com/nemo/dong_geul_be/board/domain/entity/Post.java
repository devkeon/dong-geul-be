package com.nemo.dong_geul_be.board.domain.entity;

import com.nemo.dong_geul_be.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = true)
    private String hashtag;

    @Column(nullable = false)
    private Boolean postType;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    //나중에 동아리별 글 목록 조회 시 club이랑 mapping할 필요 있음
    private String club;

    @Setter
    @Column(nullable = false, columnDefinition = "int default 0")
    private int commentCount;

    @Column(nullable = false)
    private Boolean isExternal = false;


}