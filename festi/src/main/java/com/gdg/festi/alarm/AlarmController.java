package com.gdg.festi.alarm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    // 알림 조회
    @GetMapping("/search")
    public ResponseEntity<?> search() {
        return ResponseEntity.ok().body(alarmService.search());
    }

    // 알림 확인
    @PostMapping("/{id}/read")
    public ResponseEntity<?> read(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(alarmService.read(id));
    }
}
