package com.nemo.dong_geul_be.mypage.domain;


import com.nemo.dong_geul_be.clubAndHeadmem.ClubAndHeadMem;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyClub {

        @Id
        @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
        private Long id;

        @NotNull
        private String name;

        @NotNull
        private String studentId;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private IsConfirmed isConfirmed;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "clubAndHeadMem_id")
        private ClubAndHeadMem clubAndHeadMem;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "member_id")
        private Member member;

        public void confirmMember() {
                if (this.isConfirmed == IsConfirmed.JOIN) {
                        throw new IllegalStateException("Member is already JOIN.");
                }
                this.isConfirmed = IsConfirmed.JOIN; // IsConfirmed.CONFIRMED로 변경
        }

}
