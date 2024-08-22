package com.huy2209.library_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddBookRequest {
    private String title;
    private String category;
    private String image;
    private String author;
    private String description;
    private int copies;
}
