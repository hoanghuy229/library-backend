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
public class HistoriesResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("checkout_date")
    private String checkoutDate;

    @JsonProperty("return_date")
    private String returnDate;

    @JsonProperty("author")
    private String author;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image")
    private String image;
}
