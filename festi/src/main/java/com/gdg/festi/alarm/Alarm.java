package com.gdg.festi.alarm;

import com.gdg.festi.common.BaseEntity;
import com.gdg.festi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "alarm")
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String alarmMsg;
    private LocalDateTime sendTime;
    private String alarmType;
    private Boolean isRead;
    private String linkPath;
}
