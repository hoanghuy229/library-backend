package com.huy2209.library_backend.service;

import com.huy2209.library_backend.dto.response.PaymentResponse;
import com.huy2209.library_backend.dto.response.TransactionResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface IPaymentService {
    PaymentResponse createPayment(String inputAmount, HttpServletRequest request,int bookId) throws UnsupportedEncodingException;
}
