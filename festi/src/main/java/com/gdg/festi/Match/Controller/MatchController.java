package com.gdg.festi.Match.Controller;

import com.gdg.festi.Match.Dto.request.MatchInfoEnrollRequest;
import com.gdg.festi.Match.Dto.request.MatchInfoUpdateRequest;
import com.gdg.festi.Match.Dto.response.MatchInfoResponse;
import com.gdg.festi.Match.Service.MatchService;
import com.gdg.festi.global.Api.ApiResponse;
import com.gdg.festi.global.annotation.LoginUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class MatchController {

    private final MatchService matchService;

    // 매칭 등록 내역 조회
    @GetMapping("/match/{day}")
    public ApiResponse<MatchInfoResponse> getMatch(@LoginUser UserDetails userDetails, @PathVariable String day) {
        return matchService.getMatchInfo(userDetails, LocalDate.parse(day));
    }

    // 매칭 정보 등록
    @PostMapping("/match")
    public ApiResponse<?> postMatch(@LoginUser UserDetails userDetails, @RequestBody MatchInfoEnrollRequest matchInfoEnrollRequest) {
        return matchService.enrollMatchInfo(userDetails, matchInfoEnrollRequest);
    }

    // 매칭 등록 내역 수정
    @PutMapping("/match/{matchId}")
    public ApiResponse<?> putMatch(@PathVariable Long matchId, @RequestBody MatchInfoUpdateRequest matchInfoUpdateRequest) {
        return matchService.updateMatchInfo(matchId, matchInfoUpdateRequest);
    }

    // 매칭 등록 취소
    @DeleteMapping("/match/{matchId}")
    public ApiResponse<?> cancelMatch(@PathVariable Long matchId) {
        return matchService.cancelMatchInfo(matchId);
    }

    // 매칭 결과 조회
    @GetMapping("/match/result/{day}")
    public ApiResponse<MatchInfoResponse> getMatchResult(@LoginUser UserDetails userDetails, @PathVariable String day) {
        return matchService.getMatchResult(userDetails, LocalDate.parse(day));
    }
}
