package com.huy2209.library_backend.service;

import com.huy2209.library_backend.dto.UserResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface IUserService {
    UserResponse login(OAuth2User principal, OAuth2AuthorizedClient authorizedClient) throws Exception ;
}
