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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
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

    // 매칭 정보 등록
    public ApiResponse<?> enrollMatchInfo(MatchInfoEnrollRequest matchInfoEnrollRequest){

        if (isEnrolled(matchInfoEnrollRequest.getUserId(), matchInfoEnrollRequest.getMatchDate())) {
            return ApiResponse.fail(500, "이미 매칭을 신청한 내역이 있어요", null);
        }

        MatchInfo matchInfo = buildMatchInfo(matchInfoEnrollRequest);

        matchInfoRepository.save(matchInfo);

        return ApiResponse.ok(ApiResponseMessages.ENROLL_STATUS, "매칭 정보 등록 성공");
    }

    // 매칭 등록 내역 조회
    public ApiResponse<MatchInfoResponse> getMatchInfo(Long user_id, LocalDate match_date){

        log.info("user_id : {}", user_id);
        log.info("date : {}", match_date);

        MatchInfo matchInfo = matchInfoRepository.findByUserIdAndMatchDate(user_id, match_date)
                .orElseThrow(() -> new IllegalArgumentException("매칭 등록 내역이 없어요."));

        return ApiResponse.ok(ApiResponseMessages.SUCCESS_STATUS, buildMatchInfoResponse(matchInfo));
    }

    // 매칭 정보 수정
    public ApiResponse<MatchInfoResponse> updateMatchInfo(Long match_info_id, MatchInfoUpdateRequest matchInfoUpdateRequest){

        MatchInfo matchInfo = matchInfoRepository.findById(match_info_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 매칭 정보가 없습니다."));

        updateInfo(matchInfo, matchInfoUpdateRequest);

        return ApiResponse.ok(ApiResponseMessages.UPDATE_STATUS, buildMatchInfoResponse(matchInfo));
    }

    // 매칭 등록 취소
    public ApiResponse<?> cancelMatchInfo(Long match_info_id){

        MatchInfo matchInfo = matchInfoRepository.findById(match_info_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 매칭 정보가 없습니다."));

        matchInfo.updateStatus(Status.CANCELED);

        return ApiResponse.ok(ApiResponseMessages.CANCEL_STATUS, "매칭 요청 취소 성공");
    }

    public ApiResponse<MatchInfoResponse> getMatchResult(Long user_id, LocalDate match_date) {
        MatchInfo otherMatchInfo = matchResultRepository.findMatchedInfo(user_id, match_date)
                .orElseThrow(() -> new ResourceNotFoundException("매칭 결과가 나오지 않았어요."));

        return ApiResponse.ok(ApiResponseMessages.SUCCESS_STATUS, buildMatchInfoResponse(otherMatchInfo));
    }


    /// 비즈니스 로직

    // 매칭 정보 수정
    private void updateInfo(MatchInfo matchInfo, MatchInfoUpdateRequest matchInfoUpdateRequest) {

        if (!matchInfo.getGroupName().equals(matchInfoUpdateRequest.getGroupName())) {
            matchInfo.updateGroupName(matchInfoUpdateRequest.getGroupName());
        }

        if (!matchInfo.getGroupInfo().equals(matchInfoUpdateRequest.getGroupInfo())) {
            matchInfo.updateGroupInfo(matchInfoUpdateRequest.getGroupInfo());
        }

        if (!matchInfo.getPeople().equals(matchInfoUpdateRequest.getPeople())) {
            matchInfo.updatePeople(matchInfoUpdateRequest.getPeople());
        }

        if (!matchInfo.getMatchDate().equals(matchInfoUpdateRequest.getMatchDate())) {
            matchInfo.updateMatchDate(matchInfoUpdateRequest.getMatchDate());
        }

        if (!matchInfo.getStartTime().equals(matchInfoUpdateRequest.getStartTime())) {
            matchInfo.updateStartTime(matchInfoUpdateRequest.getStartTime());
        }

        if (!matchInfo.getGender().equals(matchInfoUpdateRequest.getGender())) {
            matchInfo.updateGender(matchInfoUpdateRequest.getGender());
        }

        if (!matchInfo.getDesiredGender().equals(matchInfoUpdateRequest.getDesiredGender())) {
            matchInfo.updateDesired_gender(matchInfoUpdateRequest.getDesiredGender());
        }

        if (!matchInfo.getDrink().equals(matchInfoUpdateRequest.getDrink())) {
            matchInfo.updateDrink(matchInfoUpdateRequest.getDrink());
        }

        if (!matchInfo.getMood().equals(matchInfoUpdateRequest.getMood())) {
            matchInfo.updateMood(matchInfoUpdateRequest.getMood());
        }

        if (!matchInfo.getContact().equals(matchInfoUpdateRequest.getContact())) {
            matchInfo.updateContact(matchInfoUpdateRequest.getContact());
        }

        if (!matchInfo.getGroupImg().equals(matchInfoUpdateRequest.getGroupImg())) {
            matchInfo.updateGroupImg(matchInfoUpdateRequest.getGroupImg());
        }

        matchInfo.updateModifiedAt(LocalDateTime.now());

    }

    private boolean isEnrolled(Long userId, LocalDate matchDate) {
        return matchInfoRepository.existsByUserIdAndMatchDate(userId, matchDate);
    }


    /// 빌더
    // 매칭 생성 빌더
    private MatchInfo buildMatchInfo(MatchInfoEnrollRequest matchInfoEnrollRequest) {
        return MatchInfo.builder()
                .userId(matchInfoEnrollRequest.getUserId())
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
