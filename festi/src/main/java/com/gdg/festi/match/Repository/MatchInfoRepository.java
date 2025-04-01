package com.gdg.festi.match.Repository;

import com.gdg.festi.match.Domain.MatchInfo;
import com.gdg.festi.match.Enums.Status;
import com.gdg.festi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long> {

    Optional<MatchInfo> findByUserAndMatchDateAndStatus(User user, LocalDate matchDate, Status status);

    Optional<List<MatchInfo>> findAllByMatchDate(LocalDate matchDate);

    boolean existsByUserAndMatchDate(User user, LocalDate matchDate);

    @Modifying
    @Query("UPDATE MatchInfo m SET m.status = com.gdg.festi.match.Enums.Status.MATCHED WHERE m.matchInfoId IN :ids")
    void updateStatusMatchedForIds(@Param("ids") List<Long> ids);
}
