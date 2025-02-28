package com.gdg.festi.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NicknameResponse {

    private String nickname;
    private boolean success;
}
