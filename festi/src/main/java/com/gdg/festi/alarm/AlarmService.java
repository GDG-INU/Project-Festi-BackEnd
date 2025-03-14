package com.gdg.festi.alarm;

import com.gdg.festi.alarm.dto.response.SearchResponseDTO;
import com.gdg.festi.common.response.ApiResponse;
import com.gdg.festi.common.response.resEnum.SuccessCode;
import com.gdg.festi.user.entity.User;
import com.gdg.festi.user.repository.UserRepository;
import com.gdg.festi.user.service.UserService;
import jakarta.transaction.Transactional;
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

    /**
     * 사용자별 알림 조회
     * @return 조회된 알림 리스트
     */
    public ApiResponse<?> search() {
        // 사용자 정보 받아오기
        Optional<User> userInfo = userRepository.findByKakaoId(userService.getCurrentKakaoId());

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

    /**
     * 알림 확인
     * @param id 확인한 알림 id
     * @return msg
     */
    @Transactional
    public ApiResponse<?> read(Long id) {
        Alarm alarmInfo = alarmRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("알림 정보를 찾을 수 없습니다."));

        // 알림 ID 로 알림 정보 조회 후 읽음 처리
        if (!alarmInfo.getIsRead()) {
            alarmRepository.save(Alarm.builder()
                    .alarmId(alarmInfo.getAlarmId())
                    .alarmMsg(alarmInfo.getAlarmMsg())
                    .alarmType(alarmInfo.getAlarmType())
                    .sendTime(alarmInfo.getSendTime())
                    .linkPath(alarmInfo.getLinkPath())
                    .user(alarmInfo.getUser())
                    .isRead(true)
                    .build());
            log.info("알림을 확인했습니다. {}", id);
        }
        return ApiResponse.SUCCESS(SuccessCode.SUCCESS_READ);
    }
}
