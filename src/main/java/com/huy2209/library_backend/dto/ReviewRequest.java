package com.huy2209.library_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

    @JsonProperty("rating")
    private double rating;

    @JsonProperty("book_id")
    private Long bookId;

    @JsonProperty("review_description")
    private String reviewDescription;
}
