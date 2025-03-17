package com.gdg.festi.match.Dto.response;

import com.gdg.festi.match.Enums.Drink;
import com.gdg.festi.match.Enums.Gender;
import com.gdg.festi.match.Enums.Mood;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MatchInfoResponse {

    private Long matchInfoId;

    private String groupName;

    private String groupImg;

    private Gender gender;

    private Gender desiredGender;

    private LocalDateTime matchDateTime;

    private Drink drink;

    private int people;

    private Mood mood;

    private String contact;

}
