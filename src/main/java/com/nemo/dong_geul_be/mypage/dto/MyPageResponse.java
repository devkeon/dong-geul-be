package com.nemo.dong_geul_be.mypage.dto;

import com.nemo.dong_geul_be.member.domain.entity.Member;
import com.nemo.dong_geul_be.member.domain.entity.Role;
import com.nemo.dong_geul_be.mypage.domain.IsConfirmed;
import com.nemo.dong_geul_be.mypage.domain.MyClub;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponse<T>  {
    private String message;
    private List<MyPageDTO> data;


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageDTO{
        String nickName;
        String email;
        //운영진 여부
        Role role;
        //가입한 동아리 리스트
        List<ClubDTO> myClubList;
        //요청 수락 대기중인 회원 리스트 -> Member일 경우 null
        List<WaitingClubMemberDTO> waitingMemberList;
        //전체 동아리 이름 리스트
        List<allClubDTO> allClubList;
        //현재 관리하는 동아리 이름 -> Member일 경우 null
        String currentClubName;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClubDTO{
        //가입한 동아리 (개별)
        String clubName;
        IsConfirmed isConfirmed;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class allClubDTO{
        String clubName;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WaitingClubMemberDTO{
        String name;
        String email;
        String studentId;
        IsConfirmed isConfirmed;
    }

}
