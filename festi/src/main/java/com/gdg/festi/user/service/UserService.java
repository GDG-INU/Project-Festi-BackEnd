package com.gdg.festi.user.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.gdg.festi.user.config.JWTUtil;
import com.gdg.festi.user.dto.response.KakaoLoginResponse;
import com.gdg.festi.user.entity.User;
import com.gdg.festi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil; // JWT 유틸 추가

    private final String KAKAO_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";

    private final RestTemplate restTemplate = new RestTemplate();


    public KakaoLoginResponse loginWithKakao(String kakaoId, String nickname, String kakaoAccessToken) {
        // 1. 기존 회원 찾기
        Optional<User> userOptional = userRepository.findByKakaoId(kakaoId);
        boolean isNewUser;
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.setKakaoAccessToken(kakaoAccessToken);
            isNewUser = false;

        } else {
            // 2. 신규 유저면 저장
            user = new User(kakaoId, "사용자", kakaoAccessToken);
            isNewUser = true;
        }

        userRepository.save(user);

        // jwt 토큰 발급
        String jwtToken = jwtUtil.generateToken(user.getKakaoId());

        return new KakaoLoginResponse(jwtToken, isNewUser);
    }

    public void logoutWithKakao(String kakaoId){

        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String kakaoAccessToken = user.getKakaoAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + kakaoAccessToken); // 직접 Bearer 추가
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(null, headers); // 본문을 명확하게 null로 설정
        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_LOGOUT_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            // 로그아웃 후 액세스 토큰 삭제
            user.setKakaoAccessToken(null);
            userRepository.save(user);
        }

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


    // userName 정보 가져오기
    public String getCurrentKakaoId() {
        // 현재 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나 익명 사용자일 경우 예외 발생
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }

        // UserDetails에서 kakaoID 추출
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }


    public String getUserNickname(String kakaoId) {
        Optional<User> userOptional = userRepository.findByKakaoId(kakaoId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            return user.getNickname();
        }

        return null;
    }

    // 닉네임 중복 체크
    public boolean isNicknameAvailable(String nickname) {

        return !userRepository.existsByNickname(nickname);
    }



}
