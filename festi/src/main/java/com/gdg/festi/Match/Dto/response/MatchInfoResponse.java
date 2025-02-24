package com.gdg.festi.Match.Dto.response;

import com.gdg.festi.Match.Enums.Drink;
import com.gdg.festi.Match.Enums.Gender;
import com.gdg.festi.Match.Enums.Mood;
import com.gdg.festi.Match.Enums.Status;
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
