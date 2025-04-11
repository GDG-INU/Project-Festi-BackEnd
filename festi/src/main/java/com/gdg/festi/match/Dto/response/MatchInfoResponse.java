package com.gdg.festi.match.Dto.response;

import com.gdg.festi.match.Domain.MatchInfo;
import com.gdg.festi.match.Enums.Drink;
import com.gdg.festi.match.Enums.Gender;
import com.gdg.festi.match.Enums.Mood;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchInfoResponse {

    private Long matchInfoId;

    private String groupName;

    private String groupImg;

    private Gender gender;

    private List<Gender> desiredGender;

    private LocalDateTime matchDateTime;

    private Drink drink;

    private int people;

    private Mood mood;

    private List<String> contact;

    @Builder
    private MatchInfoResponse(Long matchInfoId, String groupName, String groupImg, Gender gender, List<Gender> desiredGender, LocalDateTime matchDateTime, Drink drink, int people, Mood mood, List<String> contact) {
        this.matchInfoId = matchInfoId;
        this.groupName = groupName;
        this.groupImg = groupImg;
        this.gender = gender;
        this.desiredGender = desiredGender;
        this.matchDateTime = matchDateTime;
        this.drink = drink;
        this.people = people;
        this.mood = mood;
        this.contact = contact;
    }

    public static MatchInfoResponse of(MatchInfo matchInfo) {
        return MatchInfoResponse.builder()
                .matchInfoId(matchInfo.getMatchInfoId())
                .groupName(matchInfo.getGroupName())
                .groupImg(matchInfo.getGroupImg())
                .gender(matchInfo.getGender())
                .desiredGender(matchInfo.getDesiredGender())
                .matchDateTime(matchInfo.getStartTime())
                .drink(matchInfo.getDrink())
                .people(matchInfo.getPeople())
                .mood(matchInfo.getMood())
                .contact(matchInfo.getContact())
                .build();
    }
}
