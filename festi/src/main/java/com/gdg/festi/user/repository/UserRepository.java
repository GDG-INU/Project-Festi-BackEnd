package com.gdg.festi.user.repository;

import com.gdg.festi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface

UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoId(String kakaoId);

}
