package com.huy2209.library_backend.service;

import com.huy2209.library_backend.dao.BookRepository;
import com.huy2209.library_backend.dao.CheckoutRepository;
import com.huy2209.library_backend.dao.PaymentRepository;
import com.huy2209.library_backend.dao.UserRepository;
import com.huy2209.library_backend.entity.Book;
import com.huy2209.library_backend.entity.Checkout;
import com.huy2209.library_backend.entity.Payment;
import com.huy2209.library_backend.entity.User;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutService implements ICheckoutService{
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CheckoutRepository checkoutRepository;
    private final PaymentRepository paymentRepository;
    private final IUserService iUserService;

    @Override
    public Book checkoutBook(String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        Optional<User> existUser = userRepository.findByEmail(userEmail);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        Payment payment = Payment
                .builder()
                .userEmail(userEmail)
                .bookId(bookId)
                .amount(100000)
                .build();

        if(existUser.isEmpty() || book.isEmpty() || book.get().getCopiesAvailable() <=0 || !book.get().isActived()){
            throw new Exception("already checked out by user or unavailable");
        }
        if(validateCheckout != null){
            iUserService.renewLoan(userEmail,bookId);
            paymentRepository.save(payment);
            return book.get();
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

        paymentRepository.save(payment);
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
