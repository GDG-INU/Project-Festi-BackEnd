package com.gdg.festi.common.response.resEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 4xx
    LOGIN_FAIL(400, HttpStatus.BAD_REQUEST, "로그인에 실패했습니다."),
    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "액세스 권한이 없습니다."),
    PASSWORD_INCORRECT(401, HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    SESSION_EXPIRED(403, HttpStatus.FORBIDDEN, "세션이 만료되었습니다."),
    RESOURCE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Resource not found"),
    IMG_NOT_FOUND(400, HttpStatus.BAD_REQUEST, "파일을 찾을 수 없습니다."),
    // 5xx
    CREATE_FAIL(500, HttpStatus.INTERNAL_SERVER_ERROR, "객체 생성에 실패했습니다.")
    ;

    private final Integer code;
    private final HttpStatus status;
    private final String message;
}
