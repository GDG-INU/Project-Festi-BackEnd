package com.gdg.festi.user.controller;

import com.gdg.festi.user.dto.request.KakaoLoginRequest;
import com.gdg.festi.user.dto.response.ApiResponse;
import com.gdg.festi.user.dto.response.KakaoLoginResponse;
import com.gdg.festi.user.dto.response.SuccessResponse;
import com.gdg.festi.user.service.JwtService;
import com.gdg.festi.user.service.KakaoAuthService;
import com.gdg.festi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoAuthService kakaoAuthService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<KakaoLoginResponse> login(@RequestBody KakaoLoginRequest request) {
        // 1. 카카오에서 액세스 토큰 가져오기
        String accessToken = kakaoAuthService.getAccessToken(request.getCode());

        // 2. 카카오 사용자 정보 가져오기
        Map<String, String> userInfo = kakaoAuthService.getUserInfo(accessToken);

        // 3. JWT 발급 및 반환
        KakaoLoginResponse response = userService.loginWithKakao(userInfo.get("id"), userInfo.get("nickname"), accessToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(@RequestHeader("Authorization") String jwtToken) {

        // JWT에서 kakaoId를 가져옴
        String kakaoId = jwtService.getUserIdFromToken(jwtService.extractBearerToken(jwtToken));

        userService.logoutWithKakao(kakaoId);
        return ResponseEntity.ok(new SuccessResponse(true));
    }

}
