package com.gdg.festi.match.Dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdg.festi.match.Enums.Drink;
import com.gdg.festi.match.Enums.Gender;
import com.gdg.festi.match.Enums.Mood;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    private List<Gender> desiredGender;

    @NotNull
    private Drink drink;

    @NotNull
    private Mood mood;

    @NotNull
    private List<String> contact;

    @NotNull
    private String groupImg;

    @JsonCreator
    public MatchInfoEnrollRequest(
            @JsonProperty("groupName") String groupName,
            @JsonProperty("groupInfo") String groupInfo,
            @JsonProperty("people") Integer people,
            @JsonProperty("matchDate") LocalDate matchDate,
            @JsonProperty("startTime") LocalDateTime startTime,
            @JsonProperty("gender") Gender gender,
            @JsonProperty("desiredGender") List<Gender> desiredGender,
            @JsonProperty("drink") Drink drink,
            @JsonProperty("mood") Mood mood,
            @JsonProperty("contact") List<String> contact,
            @JsonProperty("groupImg") String groupImg) {
        this.groupName = groupName;
        this.groupInfo = groupInfo;
        this.people = people;
        this.matchDate = matchDate;
        this.startTime = startTime;
        this.gender = gender;
        this.desiredGender = desiredGender;
        this.drink = drink;
        this.mood = mood;
        this.contact = contact;
        this.groupImg = groupImg;
    }

}
