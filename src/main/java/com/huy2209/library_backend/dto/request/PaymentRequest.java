package com.huy2209.library_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentRequest {

    private int amount;

    private String currency;

    @JsonProperty("receipt_email")
    private String receiptEmail;
}
