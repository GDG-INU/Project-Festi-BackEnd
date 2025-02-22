package com.gdg.festi.user.controller;

import com.gdg.festi.user.service.KakaoAuthService;
import com.gdg.festi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final KakaoAuthService kakaoAuthService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String code) {
        // 1. 카카오에서 액세스 토큰 가져오기
        String accessToken = kakaoAuthService.getAccessToken(code);
        log.info("카카오 인가 코드: {}", code);

        // 2. 카카오 사용자 정보 가져오기
        Map<String, String> userInfo = kakaoAuthService.getUserInfo(accessToken);
        log.info("카카오 액세스 토큰: {}", accessToken);


        // 3. JWT 발급 및 반환
        String jwtToken = userService.loginWithKakao(userInfo.get("id"), userInfo.get("nickname"));
        log.info("발급된 JWT: {}", jwtToken);

        return ResponseEntity.ok(jwtToken);

    }

    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@RequestBody Map<String, String> requestData) {
        String kakaoId = requestData.get("kakaoId");
        String newNickname = requestData.get("newNickname");

        if (kakaoId == null || newNickname == null) {
            return ResponseEntity.badRequest().body("필수 값이 없습니다.");
        }

        boolean isUpdated = userService.updateNickname(kakaoId, newNickname);
        if (isUpdated) {
            return ResponseEntity.ok("닉네임 변경 완료");
        } else {
            return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken) {
        kakaoAuthService.kakaoLogout(accessToken.replace("Bearer ", ""));
        return ResponseEntity.ok("로그아웃 성공");
    }
}
