package com.gdg.festi.polaroid.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadResponseDTO {
    private Long polaroidId;
    private String imgLink;
}