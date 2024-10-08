package com.huy2209.library_backend.controller;

import com.huy2209.library_backend.component.JwtUtil;
import com.huy2209.library_backend.dto.request.AddBookRequest;
import com.huy2209.library_backend.dto.response.HistoriesResponse;
import com.huy2209.library_backend.dto.response.PageHistoriesResponse;
import com.huy2209.library_backend.dto.response.ShelfCurrentLoansResponse;
import com.huy2209.library_backend.service.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@CrossOrigin("${front-end.baseUrl}")
public class UserController {

    private final IUserService iUserService;
    private final JwtUtil jwtUtil;

    @GetMapping("/user-histories")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<PageHistoriesResponse> getHistories(@RequestHeader("Authorization") String token,
                                                              @RequestParam("page") int page,
                                                              @RequestParam("size") int size) throws Exception{
        try{
            String getToken = token.substring(7);
            String getEmail = jwtUtil.extractEmail(getToken);
            PageRequest pageRequest = PageRequest.of(page,size, Sort.by("id").descending());
            Page<HistoriesResponse> historiesResponses = iUserService.getAllHistories(getEmail,pageRequest);
            int totalPages = historiesResponses.getTotalPages();
            List<HistoriesResponse> historiesResponseList = historiesResponses.getContent();
            return ResponseEntity.ok().body(PageHistoriesResponse.builder()
                    .totalPages(totalPages).historiesResponse(historiesResponseList).build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/current-loans")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<ShelfCurrentLoansResponse>> currentLoans(@RequestHeader("Authorization") String token) throws Exception {
       try{
           String getToken = token.substring(7);
           String getEmail = jwtUtil.extractEmail(getToken);
           List<ShelfCurrentLoansResponse> rs = iUserService.currentLoans(getEmail);
           return ResponseEntity.ok().body(rs);
       }
       catch (Exception e){
           return ResponseEntity.badRequest().build();
       }
    }

    @PutMapping("/renew-loan")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> renewLoan(@RequestHeader("Authorization") String token,
                                            @RequestParam Long bookId) throws Exception {
        try{
            String getToken = token.substring(7);
            String getEmail = jwtUtil.extractEmail(getToken);
            iUserService.renewLoan(getEmail,bookId);
            return ResponseEntity.ok().body("renew loan success");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/return-book")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> returnBook(@RequestHeader("Authorization") String token,
                                             @RequestParam Long bookId) throws Exception {
        try{
            String getToken = token.substring(7);
            String getEmail = jwtUtil.extractEmail(getToken);
            iUserService.returnBook(getEmail,bookId);
            return ResponseEntity.ok().body("return book success");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addNewBook(@RequestBody AddBookRequest addBookRequest) throws Exception{
        try{
            iUserService.addNewBook(addBookRequest);
            return ResponseEntity.ok("add book success");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void changeStatus(@RequestParam("bookId") Long bookId,@RequestParam("status") String status) throws Exception {
        try{
            iUserService.changeBookStatus(bookId,status);
        }
        catch (Exception e){
            throw new Exception();
        }
    }

    @PutMapping("/admin/increase-book")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void increaseBookQuantity(@RequestParam Long bookId) throws Exception {
        try{
            iUserService.adminIncreaseBookQuantity(bookId);
        }
        catch (Exception e){
            throw new Exception();
        }
    }

    @PutMapping("/admin/decrease-book")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void decreaseBookQuantity(@RequestParam Long bookId) throws Exception {
        try{
            iUserService.adminDecreaseBookQuantity(bookId);
        }
        catch (Exception e){
            throw new Exception();
        }
    }

    @GetMapping("/login")
    public void login(@AuthenticationPrincipal OAuth2User principal,
                                              @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                                              HttpServletResponse response) {
        try{
            iUserService.login(principal,authorizedClient,response);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
