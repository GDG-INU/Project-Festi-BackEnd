package com.gdg.festi.Match.Repository;

import com.gdg.festi.Match.Domain.MatchInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long> {

    Optional<MatchInfo> findByUserIdAndMatchDate(Long user_id, LocalDate matchDate);

    Optional<List<MatchInfo>> findAllByMatchDate(LocalDate matchDate);

    boolean existsByUserIdAndMatchDate(Long userId, LocalDate matchDate);

    @Modifying
    @Query("UPDATE MatchInfo m SET m.status = com.gdg.festi.Match.Enums.Status.MATCHED WHERE m.matchInfoId IN :ids")
    void updateStatusMatchedForIds(@Param("ids") List<Long> ids);
}
