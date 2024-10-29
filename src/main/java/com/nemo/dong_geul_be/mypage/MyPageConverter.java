package com.nemo.dong_geul_be.mypage;

import com.nemo.dong_geul_be.clubAndHeadmem.ClubAndHeadMem;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import com.nemo.dong_geul_be.mypage.domain.IsConfirmed;
import com.nemo.dong_geul_be.mypage.domain.MyClub;
import com.nemo.dong_geul_be.mypage.dto.MyPageRequest;
import com.nemo.dong_geul_be.mypage.dto.MyPageResponse;

import java.util.List;

public class MyPageConverter {


    public static MyPageResponse.MyPageDTO toMyPageDTO(Member member,List<MyClub> clubRequests, List<ClubAndHeadMem> clubAndHeadMems, ClubAndHeadMem clubAndHeadMem) {
            //마이페이지 정보 변환
            return MyPageResponse.MyPageDTO.builder()
                    .nickName(member.getNickname())
                    .email(member.getEmail())
                    .role(member.getRole().name())
                    .myClubList(toMyClubDTOList(member.getMyClubs()))
                    .waitingMemberList(toWaitingClubMemberDTOList(clubRequests))
                    .allClubList(toAllClubDTOList(clubAndHeadMems))
                    .currentClubName(clubAndHeadMem == null ? null : clubAndHeadMem.getClubName())
                    .build();
    }

    private static List<MyPageResponse.ClubDTO> toMyClubDTOList(List<MyClub> myClubs) {
        // 가입한 동아리 이름 리스트 응답 정보 변환
        return myClubs.stream()
                .map(myClub -> MyPageResponse.ClubDTO.builder()
                        .clubName(myClub.getClubAndHeadMem().getClubName())
                        .isConfirmed(myClub.getIsConfirmed().name())
                        .build())
                .toList();
    }

    private static List<MyPageResponse.allClubDTO> toAllClubDTOList(List<ClubAndHeadMem> clubAndHeadMems){
        // 전체 동아리 이름 리스트 응답 정보 변환
        return clubAndHeadMems.stream()
                .map(clubAndHeadMem -> MyPageResponse.allClubDTO.builder()
                        .clubName(clubAndHeadMem.getClubName())
                        .build())
                .toList();
    }

    private static List<MyPageResponse.WaitingClubMemberDTO> toWaitingClubMemberDTOList(List<MyClub> clubRequests) {
        // 요청 수락 대기중인 회원 리스트 응답 정보 변환

        return clubRequests.stream()
                .map(myClub -> MyPageResponse.WaitingClubMemberDTO.builder()
                        .name(myClub.getName())
                        .email(myClub.getMember().getEmail()) //
                        .isConfirmed(myClub.getIsConfirmed().name())
                        .studentId(myClub.getStudentId())
                        .build())
                .toList();
    }

    public static MyClub toMyClub(Member member, ClubAndHeadMem clubAndHeadMem, MyPageRequest.MyClubRequest request) {
        // 가입 요청
        return MyClub.builder()
                .isConfirmed(IsConfirmed.WAITING) // 가입 대기 상태로 설정
                .clubAndHeadMem(clubAndHeadMem)
                .name(request.getName()) // 이름 설정
                .member(member)
                .studentId(request.getStudentId())
                .build();
    }

}
