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
    @JoinColumn(name = "member_id", nullable = true)    //임시로 테스트할때 null값 가능하도록
    private Member member;

    @Setter
    @Column(nullable = false, columnDefinition = "int default 0")
    private int commentCount;

    @Column(nullable = false)
    private Boolean isExternal = false;


}
