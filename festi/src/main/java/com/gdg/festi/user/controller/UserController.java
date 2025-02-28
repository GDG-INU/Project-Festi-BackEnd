package com.gdg.festi.user.controller;

import com.gdg.festi.user.dto.request.KakaoLoginRequest;
import com.gdg.festi.user.dto.request.NicknameRequest;
import com.gdg.festi.user.dto.response.ApiResponse;
import com.gdg.festi.user.dto.response.KakaoLoginResponse;
import com.gdg.festi.user.dto.response.NicknameResponse;
import com.gdg.festi.user.dto.response.SuccessResponse;
import com.gdg.festi.user.service.JwtService;
import com.gdg.festi.user.service.KakaoAuthService;
import com.gdg.festi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;


    @PatchMapping("/nickname")
    public ResponseEntity<SuccessResponse> updateNickname(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody NicknameRequest request) {

        String kakaoId = jwtService.getUserIdFromToken(jwtService.extractBearerToken(jwtToken));
        boolean isUpdated = userService.updateNickname(kakaoId, request.getNickname());

        return ResponseEntity.ok(new SuccessResponse(isUpdated));
    }



    @GetMapping("/info")
    public ResponseEntity<NicknameResponse> getUserInfo(@RequestHeader("Authorization") String jwtToken) {
        String kakaoId = jwtService.getUserIdFromToken(jwtService.extractBearerToken(jwtToken));
        String nickname = userService.getUserNickname(kakaoId);

        if (nickname != null) {
            return ResponseEntity.ok(new NicknameResponse(nickname, true));
        } else {
            return ResponseEntity.ok(new NicknameResponse(null, false));
        }
    }

    // 닉네임 중복 체크 (JWT 인증 불필요)
    @GetMapping("/nickname/check")
    public ResponseEntity<SuccessResponse> checkNickname(@RequestParam String nickname) {
        boolean isAvailable = userService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(new SuccessResponse(isAvailable));
    }



}
