package com.nemo.dong_geul_be.Search.controller;

import com.nemo.dong_geul_be.Search.domain.SearchLog;
import com.nemo.dong_geul_be.Search.request.SearchLogSaveRequest;
import com.nemo.dong_geul_be.Search.response.SearchResponse;
import com.nemo.dong_geul_be.Search.service.SearchLogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

    private final SearchLogService searchLogService;

    @Operation(summary = "검색 기록 저장", description = "최근 검색 기록을 저장합니다.")
    @PostMapping("/searching")
    public ResponseEntity<SearchResponse> saveRecentSearchLog(@RequestBody SearchLogSaveRequest request){
        searchLogService.saveRecentSearchLog(1L,request);
        return ResponseEntity.ok(new SearchResponse<>("최근 기록 검색 저장 완료"));
    }

    @Operation(summary = "최근 검색 기록 조회", description = "최근 검색 기록을 조회합니다.")
    @GetMapping("/searchLogs")
    public ResponseEntity<SearchResponse> findRecentSearchLog(@RequestParam(required = false) Long userId) {
        // 만약 userId가 필요하다면 쿼리 파라미터로 받는 것이 좋습니다.
        List<SearchLog> recentSearchLogList = searchLogService.findRecentSearchLogs(userId);
        return ResponseEntity.ok(new SearchResponse(recentSearchLogList, "최근 검색 기록 조회 완료"));
    }
}
