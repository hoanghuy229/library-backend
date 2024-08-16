package com.huy2209.library_backend.service;

import com.huy2209.library_backend.dao.BookRepository;
import com.huy2209.library_backend.dao.CheckoutRepository;
import com.huy2209.library_backend.dao.UserRepository;
import com.huy2209.library_backend.entity.Book;
import com.huy2209.library_backend.entity.Checkout;
import com.huy2209.library_backend.entity.User;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutService implements ICheckoutService{
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CheckoutRepository checkoutRepository;

    @Override
    public Book checkoutBook(String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        Optional<User> existUser = userRepository.findByEmail(userEmail);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(existUser.isEmpty() || book.isEmpty() || validateCheckout != null || book.get().getCopiesAvailable() <=0){
            throw new Exception("already checked out by user or unavailable");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());

        Checkout checkout = Checkout
                .builder()
                .checkoutDate(LocalDate.now().toString())
                .bookId(bookId)
                .userEmail(userEmail)
                .returnDate(LocalDate.now().plusDays(7).toString())
                .build();
        checkoutRepository.save(checkout);
        return book.get();
    }

    @Override
    public Boolean checkoutByUser(String userEmail, Long bookId) {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);
        return validateCheckout != null;
    }

    @Override
    public int currentLoansCount(String userEmail) {
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }
}
