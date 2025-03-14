package com.gdg.festi.common.response.resEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    // COMMON
    FOUND_NO_SEARCH_RESULT(200, "조회결과가 없습니다."),
    FOUND_IT(201, "조회가 완료되었습니다."),
    FOUND_LIST(201, "목록 조회가 완료되었습니다."),
    SUCCESS_UPLOAD(200, "폴라로이드가 업로드되었습니다."),
    SUCCESS_DOWNLOAD(200, "폴라로이드 다운로드되었습니다."),
    SUCCESS_UPDATE(200, "폴라로이드 업데이트되었습니다."),
    SUCCESS_DELETE(200, "폴라로이드가 삭제되었습니다."),
    NO_CONTENT(200, "검색된 결과가 없습니다."),
    SUCCESS_READ(200, "알림을 확인했습니다.")
    ;

    private final Integer code;
    private final String message;
}
