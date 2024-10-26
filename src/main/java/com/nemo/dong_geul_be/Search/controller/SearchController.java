package com.nemo.dong_geul_be.Search.controller;

import com.nemo.dong_geul_be.Search.domain.SearchLog;
import com.nemo.dong_geul_be.Search.request.SearchLogSaveRequest;
import com.nemo.dong_geul_be.Search.response.SearchResponse;
import com.nemo.dong_geul_be.Search.service.SearchLogService;
import com.nemo.dong_geul_be.Search.service.SearchService;
import com.nemo.dong_geul_be.authentication.util.SecurityContextUtil;
import com.nemo.dong_geul_be.board.domain.dto.response.PostDTO;
import com.nemo.dong_geul_be.board.domain.entity.Post;
import com.nemo.dong_geul_be.global.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

    private final SearchLogService searchLogService;
    private final SearchService searchService;
    private final SecurityContextUtil securityContextUtil;

    @Operation(summary = "최근 검색 기록 조회", description = "최근 검색 기록을 조회합니다.")
    @GetMapping("/searchLogs")
    public ResponseEntity<SearchResponse> findRecentSearchLog() {
        List<SearchLog> recentSearchLogList = searchLogService.findRecentSearchLogs(securityContextUtil.getContextMemberInfo().getMemberId());
        return ResponseEntity.ok(new SearchResponse(recentSearchLogList, "최근 검색 기록 조회 완료"));
    }



    @Operation(summary = "검색 후 게시물 조회", description = "검색 키워드를 바탕으로 제목, 내용 및 해시태그가 일치하는 게시물 조회 후, 검색 기록 저장")
    @GetMapping("/search")
    public Response<List<PostDTO>> searchPosts(@RequestParam String keyword) {
        searchLogService.saveRecentSearchLog(securityContextUtil.getContextMemberInfo().getMemberId(), keyword); // 1L은 임시 userId

        List<Post> posts = searchService.searchPosts(keyword);

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> new PostDTO(post.getTitle(), post.getContent(), post.getCreatedAt(), post.getCommentCount(), post.getIsExternal()))
                .collect(Collectors.toList());

        return Response.ok(postDTOs);
    }
}
