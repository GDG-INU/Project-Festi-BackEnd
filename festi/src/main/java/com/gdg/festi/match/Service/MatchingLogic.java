package com.gdg.festi.match.Service;

import com.gdg.festi.global.exception.ResourceNotFoundException;
import com.gdg.festi.match.Enums.*;
import com.gdg.festi.match.Domain.MatchInfo;
import com.gdg.festi.match.Domain.MatchResult;
import com.gdg.festi.match.Repository.MatchInfoRepository;
import com.gdg.festi.match.Repository.MatchResultRepository;
import com.gdg.festi.alarm.Alarm;
import com.gdg.festi.alarm.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingLogic {

    private static final String MATCHING_SUCCESS_MESSAGE = "주점팟 매칭이 완료되었어요.";
    private static final String MATCHING_FAIL_MESSAGE = "주점팟 매칭에 실패하였습니다. 다음 기회를 노려보세요!";

    private static final int SCORE_THRESHOLD = 60;

    private final MatchInfoRepository matchInfoRepository;

    private final MatchResultRepository matchResultRepository;

    private final AlarmRepository alarmRepository;

    private final MatchingScoreCalculator scoreCalculator;

    @Transactional
    public void match(LocalDate localDate) {

        List<MatchInfo> matchInfoList = getMatchInfoListBy(localDate);

        HashSet<MatchInfo> failedMatches = new HashSet<>();

        TreeMap<Pair<Long, Long>, Integer> matchScore = calculateMatchScores(matchInfoList);

        List<Long> matchedIdList = new ArrayList<>();

        saveMatchResults(matchScore, matchedIdList, failedMatches, matchInfoList);

        updateStatus(matchedIdList, failedMatches);

        sendFailureAlarms(failedMatches);
    }

    private TreeMap<Pair<Long, Long>, Integer> calculateMatchScores(List<MatchInfo> matchInfoList) {

        TreeMap<Pair<Long, Long>, Integer> matchScore = new TreeMap<>((pair1, pair2) -> {
            int firstComparison = pair1.getLeft().compareTo(pair2.getLeft());
            return (firstComparison != 0) ? firstComparison : pair1.getRight().compareTo(pair2.getRight());
        });

        for (int i = 0; i < matchInfoList.size(); i++) {
            for (int j = i + 1; j < matchInfoList.size(); j++) {
                MatchInfo match1 = matchInfoList.get(i);
                MatchInfo match2 = matchInfoList.get(j);

                if (isInvalidPair(match1, match2)) continue;

                Pair<Long, Long> infoResult = Pair.of(
                        Math.min(match1.getMatchInfoId(), match2.getMatchInfoId()),
                        Math.max(match1.getMatchInfoId(), match2.getMatchInfoId())
                );

                int score = scoreCalculator.calculateScore(match1, match2);
                if (score >= SCORE_THRESHOLD) {
                    matchScore.put(infoResult, score);
                }
            }
        }
        return matchScore;
    }

    private void saveMatchResults(
            TreeMap<Pair<Long, Long>, Integer> matchScore, List<Long> matchedIdList,
            HashSet<MatchInfo> failedMatches, List<MatchInfo> matchInfoList) {

        for (Pair<Long, Long> pair : matchScore.keySet()) {
            Long id1 = pair.getLeft();
            Long id2 = pair.getRight();

            if (matchedIdList.contains(id1) || matchedIdList.contains(id2)) {
                log.info("이미 매칭된 사용자입니다. 매칭을 건너뜁니다. {}, {}", id1, id2);
                continue;
            }

            MatchInfo matchInfo1 = findById(id1);
            MatchInfo matchInfo2 = findById(id2);

            matchResultRepository.save(MatchResult.of(matchInfo1, matchInfo2));

            matchedIdList.add(id1);
            matchedIdList.add(id2);

            sendAlarm(matchInfo1, matchInfo2);
        }

        Set<Long> matchedIdSet = new HashSet<>(matchedIdList);

        failedMatches.clear();

        for (MatchInfo matchInfo : matchInfoList) {
            if (!matchedIdSet.contains(matchInfo.getMatchInfoId())) {
                failedMatches.add(matchInfo);
            }
        }
    }

    private void updateStatus(List<Long> matchedIdList, HashSet<MatchInfo> failedMatches) {

        matchInfoRepository.updateStatusMatchedForIds(matchedIdList);

        for (MatchInfo failedMatch : failedMatches) {
            failedMatch.updateStatus(Status.FAILED);
        }
    }

    /// 알림 전송

    private void sendAlarm(MatchInfo matchInfo1, MatchInfo matchInfo2) {
        // 매칭 성공 알림 등록
        alarmRepository.save(buildAlarm(MATCHING_SUCCESS_MESSAGE, matchInfo1));
        alarmRepository.save(buildAlarm(MATCHING_SUCCESS_MESSAGE, matchInfo2));
        log.info("매칭 성공 알림을 전송합니다. {} {}", matchInfo1.getUser().getNickname(), matchInfo2.getUser().getNickname());
    }

    private void sendFailureAlarms(HashSet<MatchInfo> failedMatches) {
        for (MatchInfo failedMatch : failedMatches) {
            alarmRepository.save(buildAlarm(MATCHING_FAIL_MESSAGE, failedMatch));
            log.info("매칭 실패 알림을 전송합니다. {}", failedMatch.getUser().getNickname());
        }
        log.info("매칭 실패 알림이 등록되었습니다.");
    }

    /// 로직

    // 같은 날 매칭 정보 조회
    private List<MatchInfo> getMatchInfoListBy(LocalDate match_date) {
        List<MatchInfo> matchInfos = matchInfoRepository.findAllByMatchDate(match_date);

        if (matchInfos.isEmpty()) {
            throw new ResourceNotFoundException("해당 날짜에 매칭 정보가 없습니다.");
        } else {
            return matchInfos;
        }
    }

    private boolean isInvalidPair(MatchInfo match1, MatchInfo match2) {
        return isSameUser(match1, match2) || !isNotSatisfyGender(match1, match2);
    }

    private boolean isSameUser(MatchInfo matchInfo1, MatchInfo matchInfo2) {
        return matchInfo1.getUser().equals(matchInfo2.getUser());
    }

    private boolean isNotSatisfyGender(MatchInfo matchInfo1, MatchInfo matchInfo2) {
        return !matchInfo1.getDesiredGender().contains(matchInfo2.getGender()) || !matchInfo2.getDesiredGender().contains(matchInfo1.getGender());
    }

    private MatchInfo findById(Long match_info_id_1) {
        return matchInfoRepository.findById(match_info_id_1)
                .orElseThrow(() -> new ResourceNotFoundException("해당 매칭 정보가 없습니다."));
    }

    private static Alarm buildAlarm(String message, MatchInfo matchInfo) {
        return Alarm.builder()
                .alarmMsg(message)
                .alarmType("MATCHING")
                .isRead(false)
                .user(matchInfo.getUser())
                .build();
    }
}
