package com.lemon.backend.domain.letter.controller;

import com.lemon.backend.domain.letter.dto.requestDto.LetterGetListDto;
import com.lemon.backend.domain.letter.dto.requestDto.LetterGetRecentListDto;
import com.lemon.backend.domain.letter.dto.requestDto.LetterCreateDto;
import com.lemon.backend.domain.letter.dto.responseDto.LetterReplyResponse;
import com.lemon.backend.domain.letter.service.LetterService;
import com.lemon.backend.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lemon.backend.global.response.CommonResponseEntity.getResponseEntity;

@Tag(name = "Letter 컨트롤러", description = "Letter Controller API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/letter")
public class LetterController {

    private final LetterService letterService;

    @Operation(summary = "편지 목록 조회", description = "편지 목록 조회, sketchbookId 필요")
    @GetMapping("/list")
    public ResponseEntity<?> getLetterList(@RequestParam(value = "sketchbookId") Long sketchbookId){
        List<LetterGetListDto> letterList = letterService.getLetterList(sketchbookId);

        return getResponseEntity(SuccessCode.OK, letterList);
    }

    @Operation
    @GetMapping("/recent")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getRecentLetter(Authentication authentication){
        Integer senderId = (Integer) authentication.getPrincipal();
        List<LetterGetRecentListDto> letterList = letterService.getLetterThree(senderId);
        return getResponseEntity(SuccessCode.OK, letterList);
    }


    @Operation(summary = "편지 생성", description = "편지 생성, sketchbookId, characterId 필요")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createLetter(Authentication authentication, @Valid @RequestBody LetterCreateDto letterDto){
        Integer senderId = (Integer) authentication.getPrincipal();
        Long createLetterId = letterService.createLetter(senderId, letterDto);
        return getResponseEntity(SuccessCode.CREATED, createLetterId);
    }

    @Operation(summary = "답장 생성", description = "답장 생성, 상대 userId, characterId 필요")
    @PostMapping("/reply")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> replyLetter(Authentication authentication, @Valid @RequestBody LetterReplyResponse letterDto){
        Integer senderId = (Integer) authentication.getPrincipal();
        Long createLetterId = letterService.replyLetter(senderId, letterDto);
        return getResponseEntity(SuccessCode.CREATED, createLetterId);
    }

    @Operation(summary = "편지 삭제", description = "편지 삭제, letterId 필요")
    @DeleteMapping("/{letterId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteLetter(Authentication authentication, @PathVariable Long letterId){
        Integer senderId = (Integer) authentication.getPrincipal();
        letterService.deleteLetter(senderId, letterId);
        return getResponseEntity(SuccessCode.OK);
    }

    @PutMapping("/changepublic")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePublic(Authentication authentication, @RequestParam(value = "letterId") Long letterId){
        Integer senderId = (Integer) authentication.getPrincipal();
        Boolean status = letterService.changePublicStatus(senderId, letterId);
        return getResponseEntity(SuccessCode.OK, status);
    }

}
