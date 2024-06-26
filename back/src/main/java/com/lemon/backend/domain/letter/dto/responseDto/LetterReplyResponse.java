package com.lemon.backend.domain.letter.dto.responseDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterReplyResponse {

    @NotBlank(message = "편지 내용은 공백, 띄어쓰기로 만들 수 없습니다.")
    private String content;
    private Long characterMotionId;
    private Integer userId;

}
