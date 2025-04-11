package com.gdg.festi.match.Controller;

import com.gdg.festi.match.Dto.request.MatchInfoEnrollRequest;
import com.gdg.festi.match.Dto.request.MatchInfoUpdateRequest;
import com.gdg.festi.match.Dto.response.MatchInfoResponse;
import com.gdg.festi.match.Service.MatchService;
import com.gdg.festi.alarm.AlarmService;
import com.gdg.festi.global.Api.ApiResponse;
import com.gdg.festi.global.Api.ApiResponseMessages;
import com.gdg.festi.global.annotation.LoginUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class MatchController {

    private final MatchService matchService;
    private final AlarmService alarmService;

    // 매칭 등록 내역 조회
    @GetMapping("/match/{day}")
    public ApiResponse<MatchInfoResponse> getMatch(@LoginUser UserDetails userDetails, @PathVariable String day) {
        return matchService.getMatchInfo(userDetails, LocalDate.parse(day));
    }

    // 매칭 정보 등록
    @PostMapping("/match")
    public ApiResponse<?> postMatch(@LoginUser UserDetails userDetails, @RequestBody MatchInfoEnrollRequest matchInfoEnrollRequest) {
        Long matchId = matchService.enrollMatchInfo(userDetails, matchInfoEnrollRequest);
        alarmService.register("주점팟 매칭을 등록했어요.", "MATCHING");
        return ApiResponse.ok(ApiResponseMessages.ENROLL_STATUS, matchId);
    }

    // 매칭 등록 내역 수정
    @PutMapping("/match/{day}")
    public ApiResponse<?> putMatch(@LoginUser UserDetails userDetails, @PathVariable String day, @RequestBody MatchInfoUpdateRequest matchInfoUpdateRequest) {
        MatchInfoResponse matchInfoResponse = matchService.updateMatchInfo(userDetails, LocalDate.parse(day), matchInfoUpdateRequest);
        alarmService.register("주점팟 매칭을 수정했어요.", "MATCHING");
        return ApiResponse.ok(ApiResponseMessages.UPDATE_STATUS, matchInfoResponse);
    }

    // 매칭 등록 취소
    @DeleteMapping("/match/{day}")
    public ApiResponse<?> cancelMatch(@LoginUser UserDetails userDetails, @PathVariable String day) {
        return matchService.cancelMatchInfo(userDetails, LocalDate.parse(day));
    }

    // 매칭 결과 조회
    @GetMapping("/match/result/{day}")
    public ApiResponse<?> getMatchResult(@LoginUser UserDetails userDetails, @PathVariable String day) {
        return matchService.getMatchResult(userDetails, LocalDate.parse(day));
    }
}
