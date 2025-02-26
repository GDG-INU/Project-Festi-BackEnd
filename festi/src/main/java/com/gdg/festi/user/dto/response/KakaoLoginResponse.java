package com.gdg.festi.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoLoginResponse {
    private String jwtToken;

    @JsonProperty("isNewUser")
    private boolean isNewUser;
}
