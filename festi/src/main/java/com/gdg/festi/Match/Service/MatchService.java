package com.gdg.festi.Match.Service;

import com.gdg.festi.Match.Domain.MatchInfo;
import com.gdg.festi.Match.Dto.request.MatchInfoEnrollRequest;
import com.gdg.festi.Match.Dto.request.MatchInfoUpdateRequest;
import com.gdg.festi.Match.Dto.response.MatchInfoResponse;
import com.gdg.festi.Match.Enums.Status;
import com.gdg.festi.Match.Repository.MatchInfoRepository;
import com.gdg.festi.Match.Repository.MatchResultRepository;
import com.gdg.festi.global.Api.ApiResponse;
import com.gdg.festi.global.Api.ApiResponseMessages;
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
import java.time.LocalDateTime;

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
    public ApiResponse<?> enrollMatchInfo(UserDetails userDetails, MatchInfoEnrollRequest matchInfoEnrollRequest){

        User user = getLoginUser(userDetails);

        if (isEnrolled(user, matchInfoEnrollRequest.getMatchDate())) {
            return ApiResponse.fail(500, "이미 매칭을 신청한 내역이 있어요", null);
        }

        Long matchId = matchInfoRepository.save(buildMatchInfo(user, matchInfoEnrollRequest)).getMatchInfoId();

        return ApiResponse.ok(ApiResponseMessages.ENROLL_STATUS, matchId);
    }

    // 매칭 등록 내역 조회
    public ApiResponse<MatchInfoResponse> getMatchInfo(UserDetails userDetails, LocalDate match_date){

        User loginUser = getLoginUser(userDetails);

        MatchInfo matchInfo = matchInfoRepository.findByUserAndMatchDate(loginUser, match_date)
                .orElseThrow(() -> new IllegalArgumentException("매칭 등록 내역이 없어요."));

        return ApiResponse.ok(ApiResponseMessages.SUCCESS_STATUS, buildMatchInfoResponse(matchInfo));
    }

    // 매칭 정보 수정
    public ApiResponse<MatchInfoResponse> updateMatchInfo(UserDetails userDetails, LocalDate match_date, MatchInfoUpdateRequest matchInfoUpdateRequest){

        User loginUser = getLoginUser(userDetails);

        MatchInfo matchInfo = matchInfoRepository.findByUserAndMatchDate(loginUser, match_date)
                .orElseThrow(() -> new IllegalArgumentException("해당 매칭 정보가 없습니다."));

        updateInfo(matchInfo, matchInfoUpdateRequest);

        return ApiResponse.ok(ApiResponseMessages.UPDATE_STATUS, buildMatchInfoResponse(matchInfo));
    }

    // 매칭 등록 취소
    public ApiResponse<?> cancelMatchInfo(UserDetails userDetails, LocalDate match_date){

        User loginUser = getLoginUser(userDetails);

        MatchInfo matchInfo = matchInfoRepository.findByUserAndMatchDate(loginUser, match_date)
                .orElseThrow(() -> new IllegalArgumentException("해당 매칭 정보가 없습니다."));

        matchInfo.updateStatus(Status.CANCELED);

        return ApiResponse.ok(ApiResponseMessages.CANCEL_STATUS, "매칭 요청 취소 성공");
    }

    public ApiResponse<MatchInfoResponse> getMatchResult(UserDetails userDetails, LocalDate match_date) {

        User user = getLoginUser(userDetails);

        MatchInfo otherMatchInfo = matchResultRepository.findMatchedInfo(user, match_date)
                .orElseThrow(() -> new ResourceNotFoundException("매칭 결과가 나오지 않았어요."));

        return ApiResponse.ok(ApiResponseMessages.SUCCESS_STATUS, buildMatchInfoResponse(otherMatchInfo));
    }

    /// 비즈니스 로직

    // 매칭 정보 수정
    private void updateInfo(MatchInfo matchInfo, MatchInfoUpdateRequest matchInfoUpdateRequest) {
        matchInfo.updateMatch(matchInfoUpdateRequest);
    }

    private boolean isEnrolled(User user, LocalDate matchDate) {
        return matchInfoRepository.existsByUserAndMatchDate(user, matchDate);
    }

    private User getLoginUser(UserDetails userDetails) {
        return userRepository.findByKakaoId(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저가 존재하지 않습니다."));
    }

    /// 빌더
    // 매칭 생성 빌더
    private MatchInfo buildMatchInfo(User user, MatchInfoEnrollRequest matchInfoEnrollRequest) {

        return MatchInfo.builder()
                .user(user)
                .groupName(matchInfoEnrollRequest.getGroupName())
                .groupInfo(matchInfoEnrollRequest.getGroupInfo())
                .people(matchInfoEnrollRequest.getPeople())
                .matchDate(matchInfoEnrollRequest.getMatchDate())
                .startTime(matchInfoEnrollRequest.getStartTime())
                .gender(matchInfoEnrollRequest.getGender())
                .desiredGender(matchInfoEnrollRequest.getDesiredGender())
                .drink(matchInfoEnrollRequest.getDrink())
                .mood(matchInfoEnrollRequest.getMood())
                .contact(matchInfoEnrollRequest.getContact())
                .groupImg(matchInfoEnrollRequest.getGroupImg())
                .status(Status.WAITING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private MatchInfoResponse buildMatchInfoResponse(MatchInfo matchInfo) {
        return MatchInfoResponse.builder()
                .matchInfoId(matchInfo.getMatchInfoId())
                .groupName(matchInfo.getGroupName())
                .groupImg(matchInfo.getGroupImg())
                .gender(matchInfo.getGender())
                .desiredGender(matchInfo.getDesiredGender())
                .matchDateTime(matchInfo.getStartTime())
                .drink(matchInfo.getDrink())
                .people(matchInfo.getPeople())
                .mood(matchInfo.getMood())
                .contact(matchInfo.getContact())
                .build();
    }
}
