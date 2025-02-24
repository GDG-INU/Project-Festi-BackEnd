package com.gdg.festi.user.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KakaoAuthService {

    @Value("${kakao.client-id}")
    private String kakaoApiKey;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    private final String KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final String KAKAO_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";
    private final String KAKAO_UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";

    private final RestTemplate restTemplate = new RestTemplate();

    // 카카오에서 액세스 토큰 가져오기
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoApiKey);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_AUTH_URL, HttpMethod.POST, request, String.class);

        JsonElement element = JsonParser.parseString(response.getBody());
        return element.getAsJsonObject().get("access_token").getAsString();
    }

    public Map<String, String> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USER_INFO_URL, HttpMethod.POST, entity, String.class);

        // 응답 출력 (디버깅 용도)
        log.info("카카오 API 응답: {}", response.getBody());

        JsonElement element = JsonParser.parseString(response.getBody());
        JsonObject responseObject = element.getAsJsonObject();

        // "id"가 있는지 확인
        String id = responseObject.has("id") ? responseObject.get("id").getAsString() : null;
        if (id == null) {
            throw new RuntimeException("카카오 API 응답에서 ID를 찾을 수 없음");
        }

        JsonObject kakaoAccount = responseObject.has("kakao_account") ? responseObject.getAsJsonObject("kakao_account") : null;
        JsonObject profile = (kakaoAccount != null && kakaoAccount.has("profile")) ? kakaoAccount.getAsJsonObject("profile") : null;
        String nickname = (profile != null && profile.has("nickname")) ? profile.get("nickname").getAsString() : "사용자";

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("id", id);
        userInfo.put("nickname", nickname);

        return userInfo;
    }

    // 카카오 로그아웃
    public void kakaoLogout(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange(KAKAO_LOGOUT_URL, HttpMethod.POST, entity, String.class);
    }

}
