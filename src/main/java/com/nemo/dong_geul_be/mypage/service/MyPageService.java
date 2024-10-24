package com.nemo.dong_geul_be.mypage.service;


import com.nemo.dong_geul_be.clubAndHeadmem.ClubAndHeadMem;
import com.nemo.dong_geul_be.clubAndHeadmem.ClubRepository;
import com.nemo.dong_geul_be.global.exception.BusinessException;
import com.nemo.dong_geul_be.global.exception.GlobalExceptionHandler;
import com.nemo.dong_geul_be.global.response.Code;
import com.nemo.dong_geul_be.member.domain.entity.Member;
import com.nemo.dong_geul_be.member.domain.entity.Role;
import com.nemo.dong_geul_be.member.repository.MemberRepository;
import com.nemo.dong_geul_be.mypage.MyPageConverter;
import com.nemo.dong_geul_be.mypage.domain.IsConfirmed;
import com.nemo.dong_geul_be.mypage.domain.MyClub;
import com.nemo.dong_geul_be.mypage.dto.MyPageRequest;
import com.nemo.dong_geul_be.mypage.dto.MyPageResponse;
import com.nemo.dong_geul_be.mypage.repository.MyClubRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private static final Logger logger = LoggerFactory.getLogger(MyPageService.class);

    private final MemberRepository memberRepository;
    private final MyClubRepository myClubRepository;
    private final ClubRepository clubAndHeadMemRepository;

    public MyPageResponse.MyPageDTO getMyPageInfo(Long memberId) {
        //회원 정보 조회
        Member member= memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        List<MyClub> clubRequests = new ArrayList<>(); // 기본값 null로 초기화
        logger.info("Member role: {}", member.getRole());
        // member가 운영진이면 운영진 정보 조회
        if (member.getRole() == Role.MANAGER) {
            logger.info("MANAGER: 운영진 정보 조회");
            clubRequests = getWaitingRequests(member);
        }

        return MyPageConverter.toMyPageDTO(member, clubRequests, clubAndHeadMemRepository.findAll());
    }

    public List<MyClub> getWaitingRequests(Member member) {
        // 동아리 관리자로 등록된 동아리의 대기 중인 요청을 가져오는 쿼리
        logger.info("Fetching waiting requests for member with email: {}", member.getEmail());
        return myClubRepository.findByClubAndHeadMemAndIsConfirmed(member.getEmail(), IsConfirmed.WAITING);
    }

    @Transactional
    // 동아리 가입 요청
    public void requestClubJoin(Long memberId,MyPageRequest.MyClubRequest clubRequest){
        // 동아리 가입 요청
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(Code.MEMBER_NOT_FOUND));
        ClubAndHeadMem clubAndHeadMem = clubAndHeadMemRepository.findClubAndHeadMemByClubName(clubRequest.getClubName())
                .orElseThrow(() ->  new IllegalStateException("해당 동아리가 존재하지 않습니다."));

        //운영진 계정 있는지 확인
        Member headMember = memberRepository.findMemberByEmail(clubAndHeadMem.getManagerEmail())
                .orElseThrow(() ->  new IllegalStateException("운영진 계정이 없습니다."));

        // 이미 요청이 있는지 확인
        if (myClubRepository.existsByMemberAndClub(member, clubAndHeadMem)) {
            throw new IllegalStateException("이미 처리된 동아리입니다.");
        }

        MyClub myClub = MyPageConverter.toMyClub(member, clubAndHeadMem, clubRequest);
        myClubRepository.save(myClub);

    }

    @Transactional
    // 동아리 가입 요청 승인
    public void acceptClubJoin(MyPageRequest.ConfirmOrRejectRequest confirmRequest){

        Member member = memberRepository.findMemberByEmail(confirmRequest.getEmail())
                .orElseThrow(() -> new BusinessException(Code.MEMBER_NOT_FOUND));

        ClubAndHeadMem clubAndHeadMem = clubAndHeadMemRepository.findClubAndHeadMemByClubName(confirmRequest.getClubName())
                .orElseThrow(() ->  new IllegalStateException("해당 동아리가 존재하지 않습니다."));

        MyClub myClub = myClubRepository.findByMemberAndClubAndHeadMem(member, clubAndHeadMem)
                .orElseThrow(RuntimeException::new);

        //승인 처리
        myClub.confirmMember();
        myClubRepository.save(myClub);

    }

    @Transactional
    // 동아리 가입 요청 거절
    public void rejectClubJoin(MyPageRequest.ConfirmOrRejectRequest rejectRequest){

        Member member = memberRepository.findMemberByEmail(rejectRequest.getEmail())
                .orElseThrow(() -> new BusinessException(Code.MEMBER_NOT_FOUND));
        //동아리 이름으로 동아리 조회
        ClubAndHeadMem clubAndHeadMem = clubAndHeadMemRepository.findClubAndHeadMemByClubName(rejectRequest.getClubName())
                .orElseThrow(() ->  new IllegalStateException("해당 동아리가 존재하지 않습니다."));

        //회원, 동아리, 운영진으로 MyClub 조회
        MyClub myClub = myClubRepository.findByMemberAndClubAndHeadMem(member, clubAndHeadMem)
                .orElseThrow(RuntimeException::new);
        //거절 처리
        myClub.cancelMember();
        myClubRepository.save(myClub);

    }
}
