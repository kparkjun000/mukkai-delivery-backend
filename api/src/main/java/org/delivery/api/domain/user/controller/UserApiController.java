package org.delivery.api.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.UserSession;
import org.delivery.api.common.api.Api;
import org.delivery.api.domain.user.business.UserBusiness;
import org.delivery.api.domain.user.controller.model.UserResponse;
import org.delivery.api.domain.user.model.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private final UserBusiness userBusiness;

    @GetMapping("/me")
    public Api<UserResponse> me(
        @UserSession User user
    ){
        var response = userBusiness.me(user);
        return Api.OK(response);
    }

    /**
     * 계정 삭제 API
     * Apple App Store 가이드라인 5.1.1(v) 준수를 위해 추가
     */
    @DeleteMapping("/delete-account")
    public Api<String> deleteAccount(
        @UserSession User user
    ){
        userBusiness.deleteAccount(user);
        return Api.OK("계정이 성공적으로 삭제되었습니다.");
    }
}
