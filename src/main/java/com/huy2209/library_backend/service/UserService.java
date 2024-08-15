package com.huy2209.library_backend.service;

import com.huy2209.library_backend.component.JwtUtil;
import com.huy2209.library_backend.dao.UserRepository;
import com.huy2209.library_backend.dto.UserResponse;
import com.huy2209.library_backend.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void login(OAuth2User principal, OAuth2AuthorizedClient authorizedClient, HttpServletResponse response) throws Exception {
        String provider = authorizedClient.getClientRegistration().getRegistrationId();
        String providerId = null;
        if(provider.equals("google")){
            providerId = principal.getAttribute("sub").toString();
        }
        else{
            providerId = principal.getAttribute("id").toString();
        }
        String saveEmail = provider + "_" + principal.getAttribute("email");
        User existedUser = userRepository.findByProviderAndProviderId(provider, providerId);
        if(existedUser == null){
            User newUser = User
                            .builder()
                            .role("ROLE_USER")
                            .email(saveEmail)
                            .fullName(principal.getAttribute("name"))
                            .accountStatus(true)
                            .provider(provider)
                            .providerId(providerId)
                            .build();
            userRepository.save(newUser);
            String token = jwtUtil.createToken(newUser);
            redirect(response,token);
            return;
        }
        String token = jwtUtil.createToken(existedUser);
        redirect(response,token);
    }

    private void redirect(HttpServletResponse response, String token) throws IOException {
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(false);
        jwtCookie.setMaxAge(2592000);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        response.sendRedirect("http://localhost:3000");
    }

    private UserResponse buildUserResponse(String token,User user){
        return UserResponse
                .builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .accountStatus(user.getAccountStatus())
                .role(user.getRole())
                .build();
    }
}
