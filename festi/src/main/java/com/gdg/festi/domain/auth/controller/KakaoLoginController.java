package com.gdg.festi.domain.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class KakaoLoginController {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-url}")
    private String tokenUrl;

    @Value("${kakao.user-info-url}")
    private String userInfoUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; // Jackson ObjectMapper

    public KakaoLoginController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/user/loginTestPage")
    public String loginTestPage(@RequestParam("code") String code, Model model) {
        // 카카오 토큰 요청 URL 생성
        String tokenRequestUrl = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code)
                .toUriString();

        // 액세스 토큰 요청
        ResponseEntity<String> tokenResponse = restTemplate.exchange(tokenRequestUrl, HttpMethod.POST, null, String.class);
        String accessToken = extractAccessToken(tokenResponse.getBody());

        // 사용자 정보 가져오기
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET,
                new org.springframework.http.HttpEntity<>(headers), String.class);

        try {
            JsonNode userInfoJson = objectMapper.readTree(userInfoResponse.getBody());
            // 사용자 정보를 모델에 추가
            model.addAttribute("userInfo", userInfoJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "사용자 정보를 가져오는 데 실패했습니다.");
        }

        return "user/loginSuccess";
    }

    // 토큰 응답에서 access_token을 추출하는 메소드
    private String extractAccessToken(String response) {
        try {
            JsonNode responseJson = objectMapper.readTree(response);
            return responseJson.path("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}