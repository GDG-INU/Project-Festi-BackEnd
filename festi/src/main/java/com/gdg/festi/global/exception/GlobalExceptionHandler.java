package com.gdg.festi.global.exception;

import com.gdg.festi.global.Api.ApiResponse;
import com.gdg.festi.match.Enums.Drink;
import com.gdg.festi.match.Enums.Gender;
import com.gdg.festi.match.Enums.Mood;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getBindingResult().getFieldErrors().forEach(error -> {
//            String fieldName = error.getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//
//        return ResponseEntity.badRequest().body(ApiResponse.fail(400, "Validation failed", errors));
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = getCustomErrorMessage(error);
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(ApiResponse.fail(400, "유효성 검사 실패", errors));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(503).body(ApiResponse.fail(503, ex.getMessage(), null));
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ApiResponse<String>> handleDuplicatedException(DuplicatedException ex) {
        return ResponseEntity.status(400).body(ApiResponse.fail(400, ex.getMessage(), null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(ApiResponse.fail(400, ex.getMessage(), null));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<String>> handleNullPointerException(NullPointerException ex) {
        return ResponseEntity.status(400).body(ApiResponse.fail(400, ex.getMessage(), null));
    }

    
    /**
     * 필드 에러 메시지를 커스터마이징하는 메서드
     */
    private String getCustomErrorMessage(FieldError error) {
        Object rejectedValue = error.getRejectedValue(); // 잘못된 값
        String defaultMessage = error.getDefaultMessage(); // 기본 에러 메시지

        // Enum 타입 필드에 대한 커스텀 메시지 처리
        if ("typeMismatch".equals(error.getCode())) {
            String fieldName = error.getField();
            Class<?> enumClass = getEnumClassForField(fieldName); // 필드 이름으로 Enum 클래스 확인

            if (enumClass != null && enumClass.isEnum()) {
                String validValues = String.join(", ", getEnumValues(enumClass));
                return String.format("'%s'은 %s에 입력 가능한 유효한 값이 아닙니다. 입력 가능한 값 목록 : [%s]",
                        rejectedValue, fieldName, validValues);
            }
        }

        // 기본 메시지 반환 (기본적으로 @NotNull 등의 메시지 사용)
        return defaultMessage != null ? defaultMessage : "유효한 값이 아닙니다!";
    }

    /**
     * 필드 이름에 해당하는 Enum 클래스를 가져오는 메서드
     */
    private Class<?> getEnumClassForField(String fieldName) {
        return switch (fieldName) {
            case "gender", "desiredGender" -> Gender.class;
            case "drink" -> Drink.class;
            case "mood" -> Mood.class;
            default -> null; // Enum 필드가 아닌 경우
        };
    }

    /**
     * Enum 클래스에서 모든 값을 가져오는 메서드
     */
    private String[] getEnumValues(Class<?> enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("제공된 클래스는 Enum 타입이 아닙니다.");
        }
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Object::toString)
                .toArray(String[]::new);
    }
}
