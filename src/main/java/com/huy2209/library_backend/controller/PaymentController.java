package com.huy2209.library_backend.controller;

import com.huy2209.library_backend.component.JwtUtil;
import com.huy2209.library_backend.dto.response.PaymentResponse;
import com.huy2209.library_backend.dto.response.TransactionResponse;
import com.huy2209.library_backend.service.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("api/payments")
@RequiredArgsConstructor
@CrossOrigin("${front-end.baseUrl}")
public class PaymentController {
    private final IPaymentService iPaymentService;
    private final JwtUtil jwtUtil;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> create(@RequestParam("amount") String inputAmount,
                                    @RequestParam("bookId") String bookId,
                                    HttpServletRequest request) throws UnsupportedEncodingException {

        int idOfBook = Integer.parseInt(bookId);
        PaymentResponse paymentResponse = iPaymentService.createPayment(inputAmount,request,idOfBook);
        return ResponseEntity.ok().body(paymentResponse);
    }

}
