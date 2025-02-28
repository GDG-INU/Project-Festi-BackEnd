package com.gdg.festi.alarm;

import com.gdg.festi.alarm.dto.response.SearchResponseDTO;
import com.gdg.festi.common.response.ApiResponse;
import com.gdg.festi.common.response.resEnum.SuccessCode;
import com.gdg.festi.user.entity.User;
import com.gdg.festi.user.repository.UserRepository;
import com.gdg.festi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public ApiResponse<?> search() {
        // 사용자 정보 받아오기
        String kakaoId = userService.getCurrentKakaoId();
        Optional<User> userInfo = userRepository.findByKakaoId(kakaoId);

        // 사용자 정보 기준으로 알림 정보 조회
        List<Alarm> alarms = alarmRepository.findByUser(userInfo.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")));
        List<SearchResponseDTO> alarmList = new ArrayList<>();

        // DTO 변경
        if (alarms.isEmpty()) {
            return ApiResponse.SUCCESS(SuccessCode.NO_CONTENT);
        } else {
            for (Alarm alarm : alarms) {
                alarmList.add(new SearchResponseDTO(
                        alarm.getAlarmId(),
                        alarm.getUser().getKakaoId(),
                        alarm.getAlarmMsg(),
                        alarm.getSendTime().toString(),
                        alarm.getAlarmType(),
                        alarm.getIsRead().toString(),
                        alarm.getLinkPath()
                ));
            }
            return ApiResponse.SUCCESS(SuccessCode.FOUND_IT, alarmList);
        }
    }
}
