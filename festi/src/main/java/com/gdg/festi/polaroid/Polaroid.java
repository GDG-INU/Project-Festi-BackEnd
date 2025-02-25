package com.gdg.festi.polaroid;

import com.gdg.festi.common.BaseEntity;
import com.gdg.festi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "polaroid")
public class Polaroid extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long polaroidId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String imgLink; // 폴라로이드 img 링크
}
