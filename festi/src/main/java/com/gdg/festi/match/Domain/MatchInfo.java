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

//    @ElementCollection(fetch = FetchType.LAZY)
//    @CollectionTable(name = "contact", joinColumns = @JoinColumn(name = "matchInfoId"))
    @Column(name = "contact")
    private String contact;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String groupImg;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private MatchInfo(User user, String groupInfo, String groupName,
                      Integer people, LocalDate matchDate, LocalDateTime startTime,
                      Gender gender, Gender desiredGender, Drink drink, Mood mood,
                      List<String> contact, Status status, String groupImg) {
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
        this.groupImg = groupImg;
    }

    public static MatchInfo of(User user, MatchInfoEnrollRequest enrollRequest, Status status) {
        return new MatchInfo(user, enrollRequest.getGroupInfo(), enrollRequest.getGroupName(),
                enrollRequest.getPeople(), enrollRequest.getMatchDate(), enrollRequest.getStartTime(),
                enrollRequest.getGender(), enrollRequest.getDesiredGender(), enrollRequest.getDrink(), enrollRequest.getMood(),
                enrollRequest.getContact(), status, enrollRequest.getGroupImg());
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


