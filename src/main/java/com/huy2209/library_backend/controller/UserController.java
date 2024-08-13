package com.huy2209.library_backend.controller;

import com.huy2209.library_backend.dto.UserResponse;
import com.huy2209.library_backend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService iUserService;

    @GetMapping("/login")
    public ResponseEntity<UserResponse> home(@AuthenticationPrincipal OAuth2User principal,
                                             @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        try{
            UserResponse result = iUserService.login(principal,authorizedClient);
            return ResponseEntity.ok().body(result);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
