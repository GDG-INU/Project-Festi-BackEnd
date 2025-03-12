package com.gdg.festi.Match.Domain;

import com.gdg.festi.Match.Dto.request.MatchInfoUpdateRequest;
import com.gdg.festi.Match.Enums.Drink;
import com.gdg.festi.Match.Enums.Gender;
import com.gdg.festi.Match.Enums.Mood;
import com.gdg.festi.Match.Enums.Status;
import com.gdg.festi.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MatchInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

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

    private Status status;

    private String groupImg;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateGroupName(String groupName){
        this.groupName = groupName;
    }

    public void updateGroupInfo(String groupInfo){
        this.groupInfo = groupInfo;
    }

    public void updatePeople(Integer people){
        this.people = people;
    }

    public void updateMatchDate(LocalDate matchDate){
        this.matchDate = matchDate;
    }

    public void updateStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }

    public void updateDesired_gender(Gender gender) {
        this.desiredGender = gender;
    }

    public void updateDrink(Drink drink) {
        this.drink = drink;
    }

    public void updateMood(Mood mood) {
        this.mood = mood;
    }

    public void updateContact(String contact) {
        this.contact = contact;
    }

    public void updateGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updateModifiedAt(LocalDateTime modified_at) {
        this.modifiedAt = modified_at;
    }

    public void updateMatch(MatchInfoUpdateRequest matchInfoUpdateRequest) {
        if (!this.groupName.equals(matchInfoUpdateRequest.getGroupName())) {
            this.updateGroupName(matchInfoUpdateRequest.getGroupName());
        }

        if (!this.groupInfo.equals(matchInfoUpdateRequest.getGroupInfo())) {
            this.updateGroupInfo(matchInfoUpdateRequest.getGroupInfo());
        }

        if (!this.people.equals(matchInfoUpdateRequest.getPeople())) {
            this.updatePeople(matchInfoUpdateRequest.getPeople());
        }

        if (!this.matchDate.equals(matchInfoUpdateRequest.getMatchDate())) {
            this.updateMatchDate(matchInfoUpdateRequest.getMatchDate());
        }

        if (!this.startTime.equals(matchInfoUpdateRequest.getStartTime())) {
            this.updateStartTime(matchInfoUpdateRequest.getStartTime());
        }

        if (!this.gender.equals(matchInfoUpdateRequest.getGender())) {
            this.updateGender(matchInfoUpdateRequest.getGender());
        }

        if (!this.desiredGender.equals(matchInfoUpdateRequest.getDesiredGender())) {
            this.updateDesired_gender(matchInfoUpdateRequest.getDesiredGender());
        }

        if (!this.drink.equals(matchInfoUpdateRequest.getDrink())) {
            this.updateDrink(matchInfoUpdateRequest.getDrink());
        }

        if (!this.mood.equals(matchInfoUpdateRequest.getMood())) {
            this.updateMood(matchInfoUpdateRequest.getMood());
        }

        if (!this.contact.equals(matchInfoUpdateRequest.getContact())) {
            this.updateContact(matchInfoUpdateRequest.getContact());
        }

        if (!this.groupImg.equals(matchInfoUpdateRequest.getGroupImg())) {
            this.updateGroupImg(matchInfoUpdateRequest.getGroupImg());
        }

        this.updateModifiedAt(LocalDateTime.now());
    }

}
