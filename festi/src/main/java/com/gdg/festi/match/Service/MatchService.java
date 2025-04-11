package com.gdg.festi.match.Service;

import com.gdg.festi.match.Domain.MatchInfo;
import com.gdg.festi.match.Dto.request.MatchInfoEnrollRequest;
import com.gdg.festi.match.Dto.request.MatchInfoUpdateRequest;
import com.gdg.festi.match.Dto.response.MatchInfoResponse;
import com.gdg.festi.match.Enums.Status;
import com.gdg.festi.match.Repository.MatchInfoRepository;
import com.gdg.festi.match.Repository.MatchResultRepository;
import com.gdg.festi.global.Api.ApiResponse;
import com.gdg.festi.global.Api.ApiResponseMessages;
import com.gdg.festi.global.exception.DuplicatedException;
import com.gdg.festi.global.exception.ResourceNotFoundException;
import com.gdg.festi.user.entity.User;
import com.gdg.festi.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@Builder
@AllArgsConstructor
@Transactional
public class MatchService {

    private final MatchInfoRepository matchInfoRepository;

    private final MatchResultRepository matchResultRepository;

    private final MatchResultRepository matchInfoEnrollRepository;

    private final UserRepository userRepository;

    // 매칭 정보 등록
    public Long enrollMatchInfo(UserDetails userDetails, MatchInfoEnrollRequest matchInfoEnrollRequest){
        User user = loginUser(userDetails);
        // 매칭 중복 검사
        if (isEnrolled(user, matchInfoEnrollRequest.getMatchDate())) {
            throw new DuplicatedException("이미 매칭 등록이 되어 있습니다.");
        }
        MatchInfo matchInfo = MatchInfo.of(user, matchInfoEnrollRequest);
        return matchInfoRepository.save(matchInfo).getMatchInfoId();
    }

    // 매칭 등록 내역 조회
    public ApiResponse<MatchInfoResponse> getMatchInfo(UserDetails userDetails, LocalDate match_date){
        User loginUser = loginUser(userDetails);
        MatchInfo matchInfo = findByUserAndMatchDate(match_date, loginUser);
        return ApiResponse.ok(ApiResponseMessages.SUCCESS_STATUS, MatchInfoResponse.of(matchInfo));
    }

    // 매칭 정보 수정
    public MatchInfoResponse updateMatchInfo(UserDetails userDetails, LocalDate match_date, MatchInfoUpdateRequest matchInfoUpdateRequest){
        User loginUser = loginUser(userDetails);
        MatchInfo matchInfo = findByUserAndMatchDate(match_date, loginUser);
        matchInfo.updateMatch(matchInfoUpdateRequest);
        return MatchInfoResponse.of(matchInfo);
    }

    // 매칭 등록 취소
    public ApiResponse<?> cancelMatchInfo(UserDetails userDetails, LocalDate match_date){
        User loginUser = loginUser(userDetails);
        MatchInfo matchInfo = findByUserAndMatchDate(match_date, loginUser);
        matchInfoRepository.delete(matchInfo);
        return ApiResponse.ok(ApiResponseMessages.CANCEL_STATUS, "매칭 요청 취소 성공");
    }

    // 매칭 결과 조회
    public ApiResponse<?> getMatchResult(UserDetails userDetails, LocalDate match_date) {
        User user = loginUser(userDetails);
        MatchInfo otherMatchInfo = findResultBy(match_date, user);
        if (isMatchFailed(otherMatchInfo)) {
            return ApiResponse.ok(ApiResponseMessages.SUCCESS_STATUS, "적절한 매칭 상대를 찾지 못해 매칭에 실패했어요.");
        }
        return ApiResponse.ok(ApiResponseMessages.SUCCESS_STATUS, MatchInfoResponse.of(otherMatchInfo));
    }

    /// 비즈니스 로직
    private boolean isEnrolled(User user, LocalDate matchDate) {
        return matchInfoRepository.existsByUserAndMatchDate(user, matchDate);
    }

    private User loginUser(UserDetails userDetails) {
        return userRepository.findByKakaoId(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저가 존재하지 않습니다."));
    }

    private MatchInfo findByUserAndMatchDate(LocalDate match_date, User loginUser) {
        return matchInfoRepository.findByUserAndMatchDateAndStatus(loginUser, match_date, Status.WAITING)
                .orElseThrow(() -> new IllegalArgumentException("매칭 등록 내역이 없어요."));
    }

    private MatchInfo findResultBy(LocalDate match_date, User user) {
        return matchResultRepository.findMatchResultByUserAndDate(user, match_date)
                .map(matchResult -> {
                    // MatchResult가 존재할 경우 관련된 MatchInfo 반환
                    if (matchResult.getMatchInfo1().getUser().equals(user)) {
                        return matchResult.getMatchInfo2();
                    } else {
                        return matchResult.getMatchInfo1();
                    }
                })
                // MatchResult가 없을 경우 match_date와 user를 바탕으로 MatchInfo 조회
                .orElseGet(() -> matchInfoRepository.findByUserAndMatchDateAndStatus(user, match_date, Status.FAILED)
                        .orElseThrow(() -> new ResourceNotFoundException("매칭 정보가 존재하지 않습니다.")));
    }

    private boolean isMatchFailed(MatchInfo MatchInfo) {
        log.info("매칭 상태 {}", MatchInfo.getStatus());
        return MatchInfo.getStatus() == Status.FAILED;
    }
}
