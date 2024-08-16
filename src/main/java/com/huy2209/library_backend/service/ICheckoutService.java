package com.huy2209.library_backend.service;

import com.huy2209.library_backend.entity.Book;

public interface ICheckoutService {
    Book checkoutBook(String userEmail,Long bookId) throws Exception;

    Boolean checkoutByUser(String userEmail,Long bookId);

    int currentLoansCount(String userEmail);
}
