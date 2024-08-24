package com.huy2209.library_backend.controller;

import com.huy2209.library_backend.component.JwtUtil;
import com.huy2209.library_backend.dto.request.ReviewRequest;
import com.huy2209.library_backend.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
@CrossOrigin("${front-end.baseUrl}")
public class ReviewController {
    private final IReviewService iReviewService;
    private final JwtUtil jwtUtil;

    @GetMapping("/by-user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Boolean> reviewBookByUser(@RequestHeader("Authorization") String token,
                                                    @RequestParam("bookId") Long bookId) throws Exception{
        try{
            String getToken = token.substring(7);
            String getEmail = jwtUtil.extractEmail(getToken);
            Boolean rs = iReviewService.userReviewListed(getEmail,bookId);
            return ResponseEntity.ok().body(rs);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/create-review")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> postReview(@RequestHeader("Authorization") String token, @RequestBody ReviewRequest reviewRequest) throws Exception{
        try{
            String getToken = token.substring(7);
            String getEmail = jwtUtil.extractEmail(getToken);
            iReviewService.postReview(getEmail,reviewRequest);
            return ResponseEntity.ok().body("review success");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
