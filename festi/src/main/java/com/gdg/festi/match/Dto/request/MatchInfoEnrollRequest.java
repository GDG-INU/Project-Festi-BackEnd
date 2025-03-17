package com.gdg.festi.match.Dto.request;

import com.gdg.festi.match.Enums.Drink;
import com.gdg.festi.match.Enums.Gender;
import com.gdg.festi.match.Enums.Mood;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class MatchInfoEnrollRequest {

    @NotNull
    private String groupName;

    @NotNull
    private String groupInfo;

    @NotNull
    private Integer people;

    @NotNull
    private LocalDate matchDate;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private Gender gender;

    @NotNull
    private Gender desiredGender;

    @NotNull
    private Drink drink;

    @NotNull
    private Mood mood;

    @NotNull
    private String contact;

    @NotNull
    private String groupImg;

}
