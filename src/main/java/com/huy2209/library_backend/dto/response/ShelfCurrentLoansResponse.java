package com.huy2209.library_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huy2209.library_backend.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShelfCurrentLoansResponse {
    @JsonProperty("book")
    private Book book;

    @JsonProperty("days_left")
    private int daysLeft;
}
