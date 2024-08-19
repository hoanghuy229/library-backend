package com.huy2209.library_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PageMessageResponse {
    @JsonProperty("message_responses")
    private List<MessageResponse> messageResponses;

    @JsonProperty("total_pages")
    private int totalPages;
}
