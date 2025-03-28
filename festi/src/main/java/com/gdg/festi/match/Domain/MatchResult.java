package com.gdg.festi.match.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchResultId;

    @ManyToOne
    private MatchInfo matchInfo1;

    @ManyToOne
    private MatchInfo matchInfo2;

    private MatchResult(MatchInfo matchInfo1, MatchInfo matchInfo2) {
        this.matchInfo1 = matchInfo1;
        this.matchInfo2 = matchInfo2;
    }

    public static MatchResult of(MatchInfo matchInfo1, MatchInfo matchInfo2) {
        return new MatchResult(matchInfo1, matchInfo2);
    }
}
