package com.nemo.dong_geul_be.mypage.service;


import com.nemo.dong_geul_be.authentication.util.SecurityContextUtil;
import com.nemo.dong_geul_be.clubAndHeadmem.ClubAndHeadMem;
import com.nemo.dong_geul_be.clubAndHeadmem.ClubRepository;
import com.nemo.dong_geul_be.global.exception.BusinessException;
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

    private final SecurityContextUtil securityContextUtil;

    public MyPageResponse getMyPageInfo() {
        Long memberId= securityContextUtil.getContextMemberInfo().getMemberId();
        //회원 정보 조회
        Member member= memberRepository.findById(memberId).orElseThrow(
                () -> new BusinessException(Code.MEMBER_NOT_FOUND));
        List<MyClub> clubRequests = new ArrayList<>(); // 기본값 null로 초기화
        ClubAndHeadMem clubAndHeadMem = null;
        logger.info("Member role: {}", member.getRole());
        // member가 운영진이면 대기 중인 요청 조회와 자신이 관리 중인 동아리 조회
        if (member.getRole() == Role.MANAGER) {
            logger.info("MANAGER: 운영진 정보 조회");
            clubRequests = getWaitingRequests(member);
            // 운영진이 관리하는 동아리 이름 조회
            clubAndHeadMem = clubAndHeadMemRepository.findClubAndHeadMemByManagerEmail(member.getEmail())
                    .orElseThrow(() -> new BusinessException(Code.CLUB_NOT_FOUND));
        }

        MyPageResponse.MyPageDTO myPageDTO=MyPageConverter.toMyPageDTO(member, clubRequests, clubAndHeadMemRepository.findAll(), clubAndHeadMem);

        return MyPageResponse.builder()
                .message("마이페이지 정보 조회 성공")
                .data(List.of(myPageDTO))
                .build();
    }

    public List<MyClub> getWaitingRequests(Member member) {
        // 동아리 관리자로 등록된 동아리의 대기 중인 요청을 가져오는 쿼리
        logger.info("Fetching waiting requests for member with email: {}", member.getEmail());
        return myClubRepository.findByClubAndHeadMemAndIsConfirmed(member.getEmail(), IsConfirmed.WAITING);
    }

    @Transactional
    // 동아리 가입 요청
    public void requestClubJoin(MyPageRequest.MyClubRequest clubRequest){
        Long memberId= securityContextUtil.getContextMemberInfo().getMemberId();
        // 동아리 가입 요청
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(Code.MEMBER_NOT_FOUND));
        ClubAndHeadMem clubAndHeadMem = clubAndHeadMemRepository.findClubAndHeadMemByClubName(clubRequest.getClubName())
                .orElseThrow(() ->  new BusinessException(Code.CLUB_NOT_FOUND));

        //운영진 계정 있는지 확인
        Member headMember = memberRepository.findMemberByEmail(clubAndHeadMem.getManagerEmail())
                .orElseThrow(() ->  new BusinessException(Code.CLUB_HEADMEM_NOT_FOUND));

        // 이미 요청이 있는지 확인
        if (myClubRepository.existsByMemberAndClub(member, clubAndHeadMem)) {
            throw new BusinessException(Code.CLUB_REQUEST_ALREADY_EXISTS);
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
                .orElseThrow(() -> new BusinessException(Code.CLUB_NOT_FOUND));

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
                .orElseThrow(() ->  new BusinessException(Code.CLUB_NOT_FOUND));

        //회원, 동아리, 운영진으로 MyClub 조회
        MyClub myClub = myClubRepository.findByMemberAndClubAndHeadMem(member, clubAndHeadMem)
                .orElseThrow(()-> new BusinessException(Code.CLUB_NOT_FOUND));

        //거절 처리
        myClubRepository.delete(myClub);

    }

    // 운영진 계정 인증 요청
    @Transactional
    public void requestClubManager(MyPageRequest.ConfirmOrRejectRequest clubManagerRequest){
        //해당 멤버의 계정과 동아리 이름이 있는 ClubAndHeadMem 조회
        //매칭되는 게 없으면 ClubAndHeadMemAndNameNotFound 예외 발생
        ClubAndHeadMem clubAndHeadMem = clubAndHeadMemRepository.findClubAndHeadMemByManagerEmailAndClubName(clubManagerRequest.getEmail(), clubManagerRequest.getClubName())
                .orElseThrow(() -> new BusinessException(Code.CLUB_HEADMEMANDNAME_NOT_FOUND));
        //계정 role 변경
        Member member = memberRepository.findMemberByEmail(clubManagerRequest.getEmail())
                .orElseThrow(() -> new BusinessException(Code.MEMBER_NOT_FOUND));
        memberRepository.updateToManager(Role.MANAGER, member.getId()) ;
    }

}
