package com.gdg.festi.match.Service;

import com.gdg.festi.match.Domain.MatchInfo;
import com.gdg.festi.match.Enums.Drink;
import com.gdg.festi.match.Enums.Mood;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class MatchingScoreCalculator {

    private static final int FAILED_SCORE = -1000;

    public int calculateScore(MatchInfo info1, MatchInfo info2) {
        return startTimeScore(info1.getStartTime(), info2.getStartTime()) +
                peopleScore(info1.getPeople(), info2.getPeople()) +
                drinkScore(info1.getDrink(), info2.getDrink()) +
                moodScore(info1.getMood(), info2.getMood());
    }

    private int startTimeScore(LocalDateTime time1, LocalDateTime time2) {
        long diffMinutes = Duration.between(time1, time2).toMinutes();

        if (diffMinutes < 30) return 35;
        if (diffMinutes < 60) return 25;
        if (diffMinutes < 90) return 15;
        if (diffMinutes < 120) return 10;
        if (diffMinutes < 150) return 5;

        return FAILED_SCORE;
    }

    private int peopleScore(int people1, int people2) {
        int diff = Math.abs(people1 - people2);

        if (diff == 0) return 35;
        if (diff == 1) return 25;
        if (diff == 2) return 15;
        if (diff == 3) return 0;

        return FAILED_SCORE;
    }

    private int drinkScore(Drink drink1, Drink drink2) {
        double diff = Math.abs(drinkToDouble(drink1) - drinkToDouble(drink2));

        if (diff == 0.0) return 20;
        if (diff <= 0.5) return 15;
        if (diff <= 1.0) return 10;
        if (diff <= 1.5) return 5;

        return FAILED_SCORE;
    }

    private double drinkToDouble(Drink drink) {
        return switch (drink) {
            case ONE -> 1.0;
            case ONE_AND_HALF -> 1.5;
            case TWO -> 2.0;
            case TWO_AND_HALF -> 2.5;
            case THREE -> 3.0;
            case THREE_AND_HALF -> 3.5;
        };
    }

    private int moodScore(Mood mood1, Mood mood2) {
        return mood1.equals(mood2) ? 10 : 0;
    }
}
