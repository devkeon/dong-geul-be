package com.nemo.dong_geul_be.mypage.controller;


import com.nemo.dong_geul_be.global.response.Response;
import com.nemo.dong_geul_be.mypage.dto.MyPageRequest;
import com.nemo.dong_geul_be.mypage.dto.MyPageResponse;
import com.nemo.dong_geul_be.mypage.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService mypageService;

    @Operation(summary = "마이 페이지", description = "마이페이지 필요 정보를 반환합니다.")
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponse> getMyPageInfo(){
        MyPageResponse mypageInfo = mypageService.getMyPageInfo();

        return ResponseEntity.ok(mypageInfo);
    }

    @Operation(summary = "동아리 요청", description = "동아리 가입 요청을 합니다.")
    @PostMapping("/mypage/club-request")
    public ResponseEntity<Void> requestClubJoin(@RequestBody MyPageRequest.MyClubRequest clubRequest){
        // 동아리 가입 요청
        mypageService.requestClubJoin(clubRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "동아리 승인", description = "동아리 가입을 승인합니다.")
    @PostMapping("/mypage/club-accept")
    public ResponseEntity<Void> confirmClubJoin(@RequestBody MyPageRequest.ConfirmOrRejectRequest clubRequest){
        // 동아리 가입
        mypageService.acceptClubJoin(clubRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "동아리 거절", description = "동아리 가입을 거절합니다.")
    @DeleteMapping("/mypage/club-reject")
    public ResponseEntity<Void> rejectClubJoin(@RequestBody MyPageRequest.ConfirmOrRejectRequest clubRequest){
        // 동아리 가입 거절
        mypageService.rejectClubJoin(clubRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "운영진 계정 인증", description = "운영진 계정으로 인증을 요청합니다")
    @PostMapping("/mypage/club-manager")
    public ResponseEntity<Void> requestClubManager(@RequestBody MyPageRequest.ConfirmOrRejectRequest clubManagerRequest){
        // 운영진 계정 인증 요청
        mypageService.requestClubManager(clubManagerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
