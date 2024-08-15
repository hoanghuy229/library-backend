package com.huy2209.library_backend.controller;

import com.huy2209.library_backend.dto.UserResponse;
import com.huy2209.library_backend.service.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService iUserService;

    @GetMapping("/login")
    public void login(@AuthenticationPrincipal OAuth2User principal,
                                              @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                                              HttpServletResponse response) {
        try{
            iUserService.login(principal,authorizedClient,response);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
