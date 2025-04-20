package com.gdg.festi.match.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class MatchingScheduler {

    private final MatchingLogic matchingLogic;

    private final LocalDate day1 = LocalDate.of(2025, 2, 20);
    private final LocalDate day2 = LocalDate.of(2025, 2, 21);
    private final LocalDate day3 = LocalDate.of(2025, 2, 22);

    @Scheduled(cron = "${matchingDay1.cron}")
    public void executeMatchingDay1() {

        log.info("===== 축제 1일차 매칭 실행 시작 =====");

        // 매칭 로직 호출 (2025년 2월 20일 기준)
        matchingLogic.match(day1);

        log.info("===== 축제 1일차 매칭 실행 완료 =====");
    }

    @Scheduled(cron = "${matchingDay2.cron}")
    public void executeMatchingDay2() {

        log.info("===== 축제 2일차 매칭 실행 시작 =====");

        // 매칭 로직 호출 (2025년 2월 20일 기준)
        matchingLogic.match(day2);

        log.info("===== 축제 2일차 매칭 실행 완료 =====");
    }

    @Scheduled(cron = "${matchingDay3.cron}")
    public void executeMatchingDay3() {

        log.info("===== 축제 3일차 매칭 실행 시작 ======");

        // 매칭 로직 호출 (2025년 2월 20일 기준)
        matchingLogic.match(day3);

        log.info("===== 축제 3일차 매칭 실행 완료 =====");
    }

    @Scheduled(cron = "0 0/1 * * * *") // 매 1분 0초에 실행
    public void executeMatchingTest() {
        log.info("===== 테스트 매칭 실행 시작 =====");
        matchingLogic.match(LocalDate.of(2025, 2, 20)); // 테스트용 날짜 (필요시 수정)
        log.info("===== 테스트 매칭 실행 완료 =====");
    }
}
