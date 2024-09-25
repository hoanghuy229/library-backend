package com.huy2209.library_backend.service;

import com.huy2209.library_backend.dto.request.AddBookRequest;
import com.huy2209.library_backend.dto.response.HistoriesResponse;
import com.huy2209.library_backend.dto.response.ShelfCurrentLoansResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public interface IUserService {
    void login(OAuth2User principal, OAuth2AuthorizedClient authorizedClient, HttpServletResponse response) throws Exception;

    List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception;

    void returnBook(String userEmail,Long bookId) throws Exception;

    void renewLoan(String userEmail,Long bookId) throws Exception;

    Page<HistoriesResponse> getAllHistories(String getEmail, PageRequest pageRequest) throws Exception;

    void addNewBook(AddBookRequest addBookRequest);

    void adminIncreaseBookQuantity(Long bookId) throws Exception;

    void adminDecreaseBookQuantity(Long bookId) throws Exception;

    void changeBookStatus(Long bookId,String status) throws Exception;
}
