package com.nemo.dong_geul_be.board.domain.entity;


import com.nemo.dong_geul_be.clubAndHeadmem.ClubAndHeadMem;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import jakarta.persistence.*;

@Entity
public class ClubFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private ClubAndHeadMem club;

    public ClubFavorite() {}

    public ClubFavorite(Member member, ClubAndHeadMem club) {
        this.member = member;
        this.club = club;
    }
}
