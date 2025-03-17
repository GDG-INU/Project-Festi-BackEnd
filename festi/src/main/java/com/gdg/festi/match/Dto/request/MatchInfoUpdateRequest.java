package com.gdg.festi.match.Dto.request;

import com.gdg.festi.match.Enums.Drink;
import com.gdg.festi.match.Enums.Gender;
import com.gdg.festi.match.Enums.Mood;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class MatchInfoUpdateRequest {

    private String groupName;

    private String groupInfo;

    private Integer people;

    private LocalDate matchDate;

    private LocalDateTime startTime;

    private Gender gender;

    private Gender desiredGender;

    private Drink drink;

    private Mood mood;

    private String contact;

    private String groupImg;

}
