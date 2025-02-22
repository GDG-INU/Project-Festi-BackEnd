package com.gdg.festi.user.service;

import com.gdg.festi.user.config.JWTUtil;
import com.gdg.festi.user.entity.User;
import com.gdg.festi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;
    private final JWTUtil jwtUtil; // JWT 유틸 추가

    public String loginWithKakao(String kakaoId, String nickname) {
        // 1. 기존 회원 찾기
        Optional<User> userOptional = userRepository.findByKakaoId(kakaoId);

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            // 2. 신규 유저면 저장
            user = new User(kakaoId, nickname);
            userRepository.save(user);
        }

        // 3. JWT 토큰 발급
        return jwtUtil.generateToken(user.getKakaoId());
    }

    // 닉네임 변경
    @Transactional
    public boolean updateNickname(String kakaoId, String newNickname) {
        Optional<User> userOptional = userRepository.findByKakaoId(kakaoId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setNickname(newNickname);
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
