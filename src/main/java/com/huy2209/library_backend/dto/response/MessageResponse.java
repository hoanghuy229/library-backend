package com.huy2209.library_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageResponse {

    private Long id;

    @JsonProperty("user_email")
    private String userEmail;

    private String title;

    private String question;

    @JsonProperty("admin_email")
    private String adminEmail;

    private String response;

    private boolean closed;
}
