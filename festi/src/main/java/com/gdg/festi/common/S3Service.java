package com.gdg.festi.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    // S3 이미지 업로드
    public String uploadFile(MultipartFile imgFile, String filename) throws IOException {

        // 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imgFile.getSize());
        objectMetadata.setContentType(imgFile.getContentType());

        // S3에 업로드
        try {
            InputStream inputStream = imgFile.getInputStream();
            amazonS3.putObject(new PutObjectRequest(bucket, filename, inputStream, objectMetadata));
        } catch (IOException e) {
            throw new IOException("이미지 업로드에 실패했습니다.");
        }

        // 객체 URL 받아오기
        return amazonS3.getUrl(bucket, filename).toString();
    }

    // S3 이미지 삭제
    public String deleteFile(String imgLink) {
        String fileName;

        // 1. URL 에서 파일명 추출
        try {
            URL url = new URL(imgLink);
            fileName = url.getPath().substring(1); // 파일명 추출 (슬래시 제거)
        } catch (Exception e) {
            throw new RuntimeException("파일 URL에서 이름을 추출하는 중 오류 발생", e);
        }

        // 2. S3에서 이미지 삭제
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));

        return fileName;
    }
}
