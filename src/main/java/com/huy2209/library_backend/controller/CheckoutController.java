package com.huy2209.library_backend.controller;

import com.huy2209.library_backend.component.JwtUtil;
import com.huy2209.library_backend.entity.Book;
import com.huy2209.library_backend.service.ICheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/checkouts")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class CheckoutController {
    private final ICheckoutService iCheckoutService;
    private final JwtUtil jwtUtil;

    @GetMapping("/current-loans-count")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Integer> currentLoansCount(@RequestHeader("Authorization") String token){
        String getToken = token.substring(7);
        String getEmail = jwtUtil.extractEmail(getToken);
        int rs = iCheckoutService.currentLoansCount(getEmail);
        return ResponseEntity.ok().body(rs);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Boolean> checkoutByUser(@RequestParam("bookId") Long bookId,
                                                  @RequestHeader("Authorization") String token){
        String getToken = token.substring(7);
        String getEmail = jwtUtil.extractEmail(getToken);
        Boolean result = iCheckoutService.checkoutByUser(getEmail,bookId);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> checkoutBook(@RequestParam("bookId") Long bookId,
                                             @RequestHeader("Authorization") String token) throws Exception{
        try{
            String getToken = token.substring(7);
            String getEmail = jwtUtil.extractEmail(getToken);
            iCheckoutService.checkoutBook(getEmail,bookId);
            return ResponseEntity.ok().body("check out success");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


