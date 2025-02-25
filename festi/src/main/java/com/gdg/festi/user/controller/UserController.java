package com.gdg.festi.user.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.gdg.festi.user.service.JwtService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final KakaoAuthService kakaoAuthService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestParam String code) {
        // 1. 카카오에서 액세스 토큰 가져오기
        String accessToken = kakaoAuthService.getAccessToken(code);
        log.info("카카오 인가 코드: {}", code);

        // 2. 카카오 사용자 정보 가져오기
        Map<String, String> userInfo = kakaoAuthService.getUserInfo(accessToken);
        log.info("카카오 액세스 토큰: {}", accessToken);

        // 3. JWT 발급 및 반환
        String jwtToken = userService.loginWithKakao(userInfo.get("id"), userInfo.get("nickname"), accessToken);
        log.info("발급된 JWT: {}", jwtToken);

        return ResponseEntity.ok(jwtToken);
    }

    @PatchMapping("/user/nickname")
    public ResponseEntity<String> updateNickname(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody Map<String, String> requestData) {

        String kakaoId = jwtService.getUserIdFromToken(jwtToken.replace("Bearer ", ""));
        System.out.println(kakaoId);
        String newNickname = requestData.get("newNickname");

        boolean isUpdated = userService.updateNickname(kakaoId, newNickname);
        if (isUpdated) {
            return ResponseEntity.ok("닉네임 변경 완료");
        } else {
            return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String jwtToken) {

        // JWT에서 kakaoId를 가져옴
        String kakaoId = jwtService.getUserIdFromToken(jwtToken.replace("Bearer ", ""));

        userService.logoutWithKakao(kakaoId);
        return ResponseEntity.ok("로그아웃 성공");
    }
}
