package com.gdg.festi.Match.Domain;

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
}
