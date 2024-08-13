package com.huy2209.library_backend.service;

import com.huy2209.library_backend.component.JwtUtil;
import com.huy2209.library_backend.dao.UserRepository;
import com.huy2209.library_backend.dto.UserResponse;
import com.huy2209.library_backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public UserResponse login(OAuth2User principal, OAuth2AuthorizedClient authorizedClient) throws Exception {
        User user = new User();
        if(authorizedClient.getClientRegistration().getRegistrationId().equals("google")){
            user = userRepository
                    .findByProviderAndProviderId("google",principal.getAttribute("sub"));
        }
        else if(!authorizedClient.getClientRegistration().getRegistrationId().equals("google")){
            user = userRepository
                    .findByProviderAndProviderId(authorizedClient.getClientRegistration().getRegistrationId(),principal.getAttribute("id"));
        }

        if(user == null){
            User newUser = User
                            .builder()
                            .role("ROLE_USER")
                            .email(principal.getAttribute("email"))
                            .fullName(principal.getAttribute("name"))
                            .accountStatus(true)
                            .provider(authorizedClient.getClientRegistration().getRegistrationId())
                            .build();
            if(authorizedClient.getClientRegistration().getRegistrationId().equals("google")){
                newUser.setProviderId(principal.getAttribute("sub"));
            }
            else if(!authorizedClient.getClientRegistration().getRegistrationId().equals("google")){
                newUser.setProviderId(principal.getAttribute("id"));
            }

            userRepository.save(newUser);

            String token = jwtUtil.createToken(newUser);

            return UserResponse
                    .builder()
                    .fullName(newUser.getFullName())
                    .email(newUser.getEmail())
                    .phoneNumber(newUser.getPhoneNumber())
                    .address(newUser.getAddress())
                    .accountStatus(newUser.getAccountStatus())
                    .role(newUser.getRole())
                    .token(token)
                    .build();
        }
        else{
            String token = jwtUtil.createToken(user);
            return UserResponse
                    .builder()
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .address(user.getAddress())
                    .accountStatus(user.getAccountStatus())
                    .role(user.getRole())
                    .token(token)
                    .build();
        }

    }
}
