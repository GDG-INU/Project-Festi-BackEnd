package com.gdg.festi.match.Repository;

import com.gdg.festi.match.Domain.MatchInfo;
import com.gdg.festi.match.Domain.MatchResult;
import com.gdg.festi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {

    @Query("SELECT DISTINCT mi2 FROM MatchResult mr " +
            "JOIN mr.matchInfo1 mi1 " +
            "JOIN mr.matchInfo2 mi2 " +
            "WHERE mi1.user = :user AND mi1.matchDate = :matchDate " +
            "AND mi2.matchInfoId <> mi1.matchInfoId")
    Optional<MatchInfo> findMatchedInfo(@Param("user") User user, @Param("matchDate") LocalDate matchDate);
}
