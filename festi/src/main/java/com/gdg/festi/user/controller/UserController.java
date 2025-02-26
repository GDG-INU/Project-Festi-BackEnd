package com.gdg.festi.user.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.gdg.festi.user.dto.request.KakaoLoginRequest;
import com.gdg.festi.user.dto.request.UpdateNicknameRequest;
import com.gdg.festi.user.dto.response.ApiResponse;
import com.gdg.festi.user.dto.response.KakaoLoginResponse;
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
    public ResponseEntity<KakaoLoginResponse> login(@RequestBody KakaoLoginRequest request) {
        // 1. 카카오에서 액세스 토큰 가져오기
        String accessToken = kakaoAuthService.getAccessToken(request.getCode());

        // 2. 카카오 사용자 정보 가져오기
        Map<String, String> userInfo = kakaoAuthService.getUserInfo(accessToken);

        // 3. JWT 발급 및 반환
        KakaoLoginResponse response = userService.loginWithKakao(userInfo.get("id"), userInfo.get("nickname"), accessToken);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/user/nickname")
    public ResponseEntity<ApiResponse> updateNickname(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody UpdateNicknameRequest request) {

        String kakaoId = jwtService.getUserIdFromToken(jwtToken.replace("Bearer ", ""));
        boolean isUpdated = userService.updateNickname(kakaoId, request.getNewNickname());

        if (isUpdated) {
            return ResponseEntity.ok(new ApiResponse("닉네임 변경 완료"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse("사용자를 찾을 수 없습니다."));
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String jwtToken) {

        // JWT에서 kakaoId를 가져옴
        String kakaoId = jwtService.getUserIdFromToken(jwtToken.replace("Bearer ", ""));

        userService.logoutWithKakao(kakaoId);
        return ResponseEntity.ok(new ApiResponse("로그아웃 성공"));
    }
}
