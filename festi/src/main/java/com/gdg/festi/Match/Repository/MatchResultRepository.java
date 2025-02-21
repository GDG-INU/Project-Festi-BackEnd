package com.gdg.festi.Match.Repository;

import com.gdg.festi.Match.Domain.MatchInfo;
import com.gdg.festi.Match.Domain.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {

    @Query("SELECT mi2 FROM MatchResult mr " +
            "JOIN MatchInfo mi1 ON mr.matchInfoId1 = mi1.matchInfoId OR mr.matchInfoId2 = mi1.matchInfoId " +
            "JOIN MatchInfo mi2 ON (mr.matchInfoId1 = mi2.matchInfoId OR mr.matchInfoId2 = mi2.matchInfoId) " +
            "WHERE mi1.userId = :userId AND mi1.matchDate = :matchDate " +
            "AND mi2.matchInfoId <> mi1.matchInfoId")
    Optional<MatchInfo> findMatchedInfo(@Param("userId") Long userId, @Param("matchDate") LocalDate matchDate);
}
