package com.gdg.festi.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "kakao_id", nullable = false, unique = true)
    private String kakaoId; //

    @Column(nullable = true)
    private String nickname;

    @Column(name = "kakao_access_token", nullable = true)
    private String kakaoAccessToken;

    public User(String kakaoId, String nickname, String kakaoAccessToken) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.kakaoAccessToken = kakaoAccessToken;
    }

}