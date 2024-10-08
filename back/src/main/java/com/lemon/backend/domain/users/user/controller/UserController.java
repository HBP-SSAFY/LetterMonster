package com.lemon.backend.domain.users.user.controller;

import com.lemon.backend.domain.users.user.dto.request.ChangeNicknameRequest;
import com.lemon.backend.domain.users.user.dto.response.UserGetDto;
import com.lemon.backend.domain.users.user.dto.response.UserSearchAndFriendResponse;
import com.lemon.backend.domain.users.user.dto.response.UserSearchGetDto;
import com.lemon.backend.domain.users.user.service.UserService;
import com.lemon.backend.global.exception.CustomException;
import com.lemon.backend.global.exception.ErrorCode;
import com.lemon.backend.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.lemon.backend.global.response.CommonResponseEntity.getResponseEntity;

@Tag(name = "User 컨트롤러", description = "User Controller API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "액세스 토큰 재발급", description = "Header에 리프레시토큰 필요")
    @PostMapping("/token")
    public ResponseEntity<?> recreateToken(@RequestHeader(value = "Authorization", required = false) String bearerToken){
        return getResponseEntity(SuccessCode.OK, userService.recreateToken(bearerToken));
    }

    @Operation(summary = "로그아웃", description = "Header의 액세스 토큰을 이용하여 로그아웃을 합니다.")
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> recreateToken(Authentication authentication){
        Integer userId = (Integer) authentication.getPrincipal();
        userService.logout(userId);
        return getResponseEntity(SuccessCode.OK);
    }

    @Operation(summary = "닉네임 변경", description = "Header의 액세스 토큰을 이용하여 닉네임 변경을 합니다.")
    @PostMapping("/nickname")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changeNickname(Authentication authentication, @Validated @RequestBody ChangeNicknameRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new CustomException(ErrorCode.INVALID_USER_NICKNAME);

        Integer userId = (Integer) authentication.getPrincipal();
        return ResponseEntity.ok(userService.changeNickname(userId, request));
    }

    @Operation(summary = "회원탈퇴", description = "회원 탈퇴를 진행합니다.")
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> withdrawUser(Authentication authentication){
        Integer userId = (Integer) authentication.getPrincipal();
        userService.withdrawUser(userId);
        return getResponseEntity(SuccessCode.ACCEPTED);
    }

    @Operation(summary = "유저 정보 조회", description = "로그인된 유저 정보를 조회합니다.")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        Integer userId = (Integer) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @Operation(summary = "비회원 전용 유저 닉네임 검색", description = "비회원이 유저를 닉네임으로 검색합니다.")
    @GetMapping("/public/search/{nickname}")
    public ResponseEntity<?> searchNickname(@PathVariable("nickname") String nickname){
        List<UserSearchGetDto> users = userService.searchNickname(nickname);
        return getResponseEntity(SuccessCode.OK, users);
    }


    @Operation(summary = "회원 전용 유저 닉네임 검색", description = "회원이 유저를 닉네임으로 검색합니다.")
    @GetMapping("/search/{nickname}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> searchNickname(Authentication authentication, @PathVariable("nickname") String nickname){
        Integer userId = (Integer) authentication.getPrincipal();
        List<UserSearchAndFriendResponse> users = userService.userSearchUserByNickname(userId, nickname);
        return getResponseEntity(SuccessCode.OK, users);
    }
}
