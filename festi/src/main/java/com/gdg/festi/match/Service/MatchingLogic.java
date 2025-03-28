package com.gdg.festi.match.Service;

import com.gdg.festi.match.Domain.MatchInfo;
import com.gdg.festi.match.Domain.MatchResult;
import com.gdg.festi.match.Enums.Drink;
import com.gdg.festi.match.Enums.Mood;
import com.gdg.festi.match.Repository.MatchInfoRepository;
import com.gdg.festi.match.Repository.MatchResultRepository;
import com.gdg.festi.alarm.Alarm;
import com.gdg.festi.alarm.AlarmRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Slf4j
@Service
@AllArgsConstructor
public class MatchingLogic {

    private final MatchInfoRepository matchInfoRepository;

    private final MatchResultRepository matchResultRepository;

    private final AlarmRepository alarmRepository;

    private static final int FAILED = -1000;

    @Transactional
    public void match(LocalDate localDate) {

        // 같은 날 매칭을 원하는 매칭 정보 불러오기
        List<MatchInfo> matchInfoList = sameDateMatchInfoList(localDate);

        // 매칭Id가 작은쪽이 왼쪽에 위치하도록 Pair(쌍) 생성
        TreeMap<Pair<Long, Long>, Integer> matchScore = new TreeMap<>((pair1, pair2) -> {
            int firstComparison = pair1.getLeft().compareTo(pair2.getLeft());
            return (firstComparison != 0) ? firstComparison : pair1.getRight().compareTo(pair2.getRight());
        });


        for (MatchInfo matchInfo1 : matchInfoList) {
            for (MatchInfo matchInfo2 : matchInfoList) {
                // 같은 매칭 정보와 원하는 성별이 다른 매칭은 제외
                if (isSameMatchInfo(matchInfo1, matchInfo2) && checkGender(matchInfo1, matchInfo2)) {
                    continue;
                }
                // 매칭 정보 id 순서 정렬
                Pair<Long, Long> infoResult = Pair.of(
                        Math.min(matchInfo1.getMatchInfoId(), matchInfo2.getMatchInfoId()),
                        Math.max(matchInfo1.getMatchInfoId(), matchInfo2.getMatchInfoId())
                );
                int score = evaluateScore(matchInfo1, matchInfo2);
                // 매칭 점수가 50 이상인 경우에만 매칭 결과 저장
                if (isFailed(score)) {
                    continue;
                }
                matchScore.put(infoResult, score);
            }
        }

        List<Long> matchedId = new ArrayList<>();

        // 매칭 결과 저장
        for (Pair<Long, Long> matchPair : matchScore.keySet()) {
            Long match_info_id_1 = matchPair.getLeft();
            Long match_info_id_2 = matchPair.getRight();

            if (isMatched(matchedId, match_info_id_1, match_info_id_2)) {
                continue;
            }
            MatchInfo matchInfo1 = findById(match_info_id_1);
            MatchInfo matchInfo2 = findById(match_info_id_2);
            matchResultRepository.save(MatchResult.of(matchInfo1, matchInfo2));

            matchedId.add(match_info_id_1);
            matchedId.add(match_info_id_2);
            sendAlarm(matchInfo1, matchInfo2);
        }
        // 매칭된 매칭 정보 상태 변경
        matchInfoRepository.updateStatusMatchedForIds(matchedId);
    }

    private void sendAlarm(MatchInfo matchInfo1, MatchInfo matchInfo2) {
        // 매칭 성공 알림 등록
        alarmRepository.save(Alarm.builder()
                .alarmMsg("주점팟 매칭이 완료되었어요.")
                .alarmType("MATCHING")
                .isRead(false)
                .user(matchInfo1.getUser())
                .build());

        alarmRepository.save(Alarm.builder()
                .alarmMsg("주점팟 매칭이 완료되었어요.")
                .alarmType("MATCHING")
                .isRead(false)
                .user(matchInfo2.getUser())
                .build());

        log.info("알림이 등록되었습니다.");
    }


    /// 로직

    // 같은 날 매칭 정보 조회
    private List<MatchInfo> sameDateMatchInfoList(LocalDate match_date) {
        return matchInfoRepository.findAllByMatchDate(match_date)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 매칭 정보가 없습니다."));
    }

    /// 점수 계산 로직

    // 시작시간에 따른 점수 계산
    private int startTimeScore(LocalDateTime startTime1, LocalDateTime startTime2) {
        // 시간 차이 계산
        long diff = Duration.between(startTime1, startTime2).toMinutes();

        if (diff < 30) {
            return 30;
        } else if (diff < 60) {
            return 25;
        } else if (diff < 90) {
            return 20;
        } else if (diff < 120) {
            return 15;
        } else if (diff < 150) {
            return 10;
        } else {
            return FAILED;
        }
    }


    // 팀 인원에 따른 점수 계산
    private int peopleScore(int people1, int people2) {
        int diff = Math.abs(people1 - people2);

        if (diff == 0) {
            return 30;
        } else if (diff == 1) {
            return 20;
        } else if (diff == 2) {
            return 15;
        } else if (diff == 3) {
            return 5;
        } else {
            return FAILED;
        }
    }

    // 평균 주량에 따른 계산
    private int drinkScore(Drink drink1, Drink drink2) {
        Double drink1Double = drinkTransToDouble(drink1);
        Double drink2Double = drinkTransToDouble(drink2);

        double diff = Math.abs(drink1Double - drink2Double);

        if (diff == 0) {
            return 30;
        } else if (diff == 0.5) {
            return 20;
        } else if (diff == 1) {
            return 15;
        } else if (diff == 1.5) {
            return 10;
        } else if (diff == 2) {
            return 5;
        } else {
            return FAILED;
        }
    }

    // 분위기에 따른 점수 계산
    private int moodScore(Mood mood1, Mood mood2) {
        if (mood1.equals(mood2)) {
            return 10;
        } else {
            return 0;
        }
    }

    private Double drinkTransToDouble(Drink drink) {
        return switch (drink) {
            case ONE -> 1.0;
            case ONE_AND_HALF -> 1.5;
            case TWO -> 2.0;
            case TWO_AND_HALF -> 2.5;
            case THREE -> 3.0;
            case THREE_AND_HALF -> 3.5;
        };
    }

    private int evaluateScore(MatchInfo matchInfo1, MatchInfo matchInfo2) {
        return startTimeScore(matchInfo1.getStartTime(), matchInfo2.getStartTime()) +
                peopleScore(matchInfo1.getPeople(), matchInfo2.getPeople()) +
                drinkScore(matchInfo1.getDrink(), matchInfo2.getDrink()) +
                moodScore(matchInfo1.getMood(), matchInfo2.getMood());
    }

    private boolean isSameMatchInfo(MatchInfo matchInfo1, MatchInfo matchInfo2) {
        return matchInfo1.getUser().equals(matchInfo2.getUser());
    }

    private boolean checkGender(MatchInfo matchInfo1, MatchInfo matchInfo2) {
        return matchInfo1.getGender().equals(matchInfo2.getDesiredGender()) && matchInfo2.getGender().equals(matchInfo1.getDesiredGender());
    }

    private boolean isFailed(int score) {
        return score < 50;
    }

    private boolean isMatched (List<Long> matchedId, Long match_info_id_1, Long match_info_id_2) {
        return matchedId.contains(match_info_id_1) && matchedId.contains(match_info_id_2);
    }

    private MatchInfo findById(Long match_info_id_1) {
        return matchInfoRepository.findById(match_info_id_1)
                .orElseThrow(() -> new IllegalArgumentException("해당 매칭 정보가 없습니다."));
    }
}
