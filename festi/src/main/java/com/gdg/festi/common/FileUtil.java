package com.gdg.festi.common;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class FileUtil {
    private FileUtil() { }

    public static String generateFileName(MultipartFile file) {
        // 현재 날짜 및 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

        // 원본 파일명 가져오기
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new IllegalArgumentException("잘못된 파일명입니다.");
        }

        // 확장자 추출
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 난수 생성
        Random random = new Random();
        int randomNumber = random.nextInt(10000); // 0 ~ 9999
        String randomStr = String.format("%04d", randomNumber);

        return now.format(formatter) + randomStr + fileExtension;
    }
}
