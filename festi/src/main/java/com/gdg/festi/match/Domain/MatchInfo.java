package com.gdg.festi.match.Domain;

import com.gdg.festi.match.Dto.request.MatchInfoEnrollRequest;
import com.gdg.festi.match.Dto.request.MatchInfoUpdateRequest;
import com.gdg.festi.match.Enums.Drink;
import com.gdg.festi.match.Enums.Gender;
import com.gdg.festi.match.Enums.Mood;
import com.gdg.festi.match.Enums.Status;
import com.gdg.festi.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MatchInfo {

    @Id
    @Column(name = "matchInfoId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String groupName;

    private String groupInfo;

    private Integer people;

    private LocalDate matchDate;

    private LocalDateTime startTime;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Gender desiredGender;

    @Enumerated(EnumType.STRING)
    private Drink drink;

    @Enumerated(EnumType.STRING)
    private Mood mood;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "contact", joinColumns = @JoinColumn(name = "matchInfoId"))
    @Column(name = "contact")
    private List<String> contact;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    private String groupImg;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    private MatchInfo(User user, String groupInfo, String groupName,
                      Integer people, LocalDate matchDate, LocalDateTime startTime,
                      Gender gender, Gender desiredGender, Drink drink, Mood mood,
                      List<String> contact, Status status, LocalDateTime createdAt, String groupImg) {
        this.user = user;
        this.groupInfo = groupInfo;
        this.groupName = groupName;
        this.people = people;
        this.matchDate = matchDate;
        this.startTime = startTime;
        this.gender = gender;
        this.desiredGender = desiredGender;
        this.drink = drink;
        this.mood = mood;
        this.contact = contact;
        this.status = status;
        this.createdAt = createdAt;
        this.groupImg = groupImg;
    }

    public static MatchInfo of(User user, MatchInfoEnrollRequest enrollRequest) {
        return MatchInfo.builder()
                .user(user)
                .groupInfo(enrollRequest.getGroupInfo())
                .groupName(enrollRequest.getGroupName())
                .people(enrollRequest.getPeople())
                .matchDate(enrollRequest.getMatchDate())
                .startTime(enrollRequest.getStartTime())
                .gender(enrollRequest.getGender())
                .desiredGender(enrollRequest.getDesiredGender())
                .drink(enrollRequest.getDrink())
                .mood(enrollRequest.getMood())
                .contact(enrollRequest.getContact())
                .status(Status.WAITING)
                .groupImg(enrollRequest.getGroupImg())
                .build();

    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updateMatch(MatchInfoUpdateRequest matchInfoUpdateRequest) {

        this.groupName = matchInfoUpdateRequest.getGroupName();
        this.groupInfo = matchInfoUpdateRequest.getGroupInfo();
        this.people = matchInfoUpdateRequest.getPeople();
        this.matchDate = matchInfoUpdateRequest.getMatchDate();
        this.gender = matchInfoUpdateRequest.getGender();
        this.startTime = matchInfoUpdateRequest.getStartTime();
        this.desiredGender = matchInfoUpdateRequest.getDesiredGender();
        this.drink = matchInfoUpdateRequest.getDrink();
        this.mood = matchInfoUpdateRequest.getMood();
        this.contact = matchInfoUpdateRequest.getContact();
        this.groupImg = matchInfoUpdateRequest.getGroupImg();
        this.modifiedAt = LocalDateTime.now();
    }

}


