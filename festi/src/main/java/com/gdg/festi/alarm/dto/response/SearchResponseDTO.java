package com.gdg.festi.alarm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponseDTO {
    private Long alarmId;
    private String userId;
    private String alarmMsg;
    private String alarmType;
    private String isRead;
}
