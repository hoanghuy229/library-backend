package com.huy2209.library_backend.service;

import com.huy2209.library_backend.component.JwtUtil;
import com.huy2209.library_backend.dao.BookRepository;
import com.huy2209.library_backend.dao.CheckoutRepository;
import com.huy2209.library_backend.dao.HistoryRepository;
import com.huy2209.library_backend.dao.UserRepository;
import com.huy2209.library_backend.dto.request.AddBookRequest;
import com.huy2209.library_backend.dto.response.HistoriesResponse;
import com.huy2209.library_backend.dto.response.ShelfCurrentLoansResponse;
import com.huy2209.library_backend.dto.response.UserResponse;
import com.huy2209.library_backend.entity.Book;
import com.huy2209.library_backend.entity.Checkout;
import com.huy2209.library_backend.entity.History;
import com.huy2209.library_backend.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService{
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CheckoutRepository checkoutRepository;
    private final BookRepository bookRepository;
    private final HistoryRepository historyRepository;
    private final ModelMapper modelMapper;

    @Override
    public void login(OAuth2User principal, OAuth2AuthorizedClient authorizedClient, HttpServletResponse response) throws Exception {
        String provider = authorizedClient.getClientRegistration().getRegistrationId();
        String providerId = null;
        if(provider.equals("google")){
            providerId = principal.getAttribute("sub").toString();
        }
        else{
            providerId = principal.getAttribute("id").toString();
        }
        String saveEmail = provider + "_" + principal.getAttribute("email");
        User existedUser = userRepository.findByProviderAndProviderId(provider, providerId);
        if(existedUser == null){
            User newUser = User
                            .builder()
                            .role("ROLE_USER")
                            .email(saveEmail)
                            .fullName(principal.getAttribute("name"))
                            .accountStatus(true)
                            .provider(provider)
                            .providerId(providerId)
                            .build();
            userRepository.save(newUser);
            String token = jwtUtil.createToken(newUser);
            redirect(response,token,newUser.getRole());
            return;
        }
        String token = jwtUtil.createToken(existedUser);
        redirect(response,token,existedUser.getRole());
    }

    @Override
    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
        List<ShelfCurrentLoansResponse> currentLoansResponses = new ArrayList<>();
        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);
        List<Long> bookIdList = new ArrayList<>();

        for(Checkout checkout : checkoutList){
            bookIdList.add(checkout.getBookId());
        }

        List<Book> books = bookRepository.findBookByBookIds(bookIdList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for(Book book : books){
            Optional<Checkout> checkout = checkoutList.stream().filter(x -> x.getBookId() == book.getId()).findFirst();
            if(checkout.isPresent()){
                Date d1 = sdf.parse(checkout.get().getReturnDate());
                Date d2 = sdf.parse(LocalDate.now().toString());

                TimeUnit time = TimeUnit.DAYS;

                long diffirence_In_Time = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);
                currentLoansResponses.add(new ShelfCurrentLoansResponse(book,(int) diffirence_In_Time));
            }
        }
        return currentLoansResponses;
    }

    @Override
    public void returnBook(String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if(book.isEmpty() || validCheckout == null){
            throw new Exception("invalid information");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        bookRepository.save(book.get());
        checkoutRepository.deleteById(validCheckout.getId());

        History history = History
                .builder()
                .userEmail(userEmail)
                .returnDate(LocalDate.now().toString())
                .checkoutDate(validCheckout.getCheckoutDate())
                .author(book.get().getAuthor())
                .description(book.get().getDescription())
                .title(book.get().getTitle())
                .image(book.get().getImage())
                .build();

        historyRepository.save(history);
    }

    @Override
    public void renewLoan(String userEmail, Long bookId) throws Exception {
        Checkout validCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if(validCheckout == null){
            throw new Exception("invalid information");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(validCheckout.getReturnDate());
        Date d2 = sdf.parse(LocalDate.now().toString());

        if(d1.compareTo(d2) > 0 || d1.compareTo(d2) == 0){
            validCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(validCheckout);
        }
    }

    @Override
    public Page<HistoriesResponse> getAllHistories(String getEmail, PageRequest pageRequest) throws Exception {
        Page<History> histories = historyRepository.findByUserEmail(getEmail,pageRequest);
        return histories.map(history -> modelMapper.map(history,HistoriesResponse.class));
    }

    @Override
    public void adminAddBook(AddBookRequest addBookRequest){
        Book book = Book
                .builder()
                .title(addBookRequest.getTitle())
                .author(addBookRequest.getAuthor())
                .description(addBookRequest.getDescription())
                .copies(addBookRequest.getCopies())
                .copiesAvailable(addBookRequest.getCopies())
                .image(addBookRequest.getImage())
                .category(addBookRequest.getCategory())
                .isActived(true)
                .build();

        bookRepository.save(book);
    }

    @Override
    public void adminIncreaseBookQuantity(Long bookId) throws Exception {
        Optional<Book> existBook = bookRepository.findById(bookId);
        if(existBook.isEmpty()){
            throw new Exception("book not found");
        }
        existBook.get().setCopiesAvailable(existBook.get().getCopiesAvailable() + 1);
        existBook.get().setCopies(existBook.get().getCopies() + 1);

        bookRepository.save(existBook.get());
    }

    @Override
    public void adminDecreaseBookQuantity(Long bookId) throws Exception {
        Optional<Book> existBook = bookRepository.findById(bookId);
        if(existBook.isEmpty() || existBook.get().getCopies() <= 0 || existBook.get().getCopiesAvailable() <= 0){
            throw new Exception("book not found");
        }
        existBook.get().setCopiesAvailable(existBook.get().getCopiesAvailable() - 1);
        existBook.get().setCopies(existBook.get().getCopies() - 1);

        bookRepository.save(existBook.get());
    }

    @Override
    public void changeBookStatus(Long bookId,String status) throws Exception {
        Optional<Book> existBook = bookRepository.findById(bookId);

        if(existBook.isEmpty()){
            throw new Exception("Error");
        }

        if(status.equals("true") && !existBook.get().isActived()){
            existBook.get().setActived(true);
            bookRepository.save(existBook.get());
        }
        else{
            existBook.get().setActived(false);
            bookRepository.save(existBook.get());
        }
    }

    private void redirect(HttpServletResponse response, String token,String role) throws IOException {
        String getRole = role.substring(5);

        Cookie userRole = new Cookie("role",getRole);
        userRole.setHttpOnly(false);
        userRole.setMaxAge(2592000);
        userRole.setPath("/");

        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(false);
        jwtCookie.setMaxAge(2592000);
        jwtCookie.setPath("/");

        response.addCookie(jwtCookie);
        response.addCookie(userRole);
        response.sendRedirect("http://localhost:3000");
    }

    private UserResponse buildUserResponse(String token,User user){
        return UserResponse
                .builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .accountStatus(user.getAccountStatus())
                .role(user.getRole())
                .build();
    }
}
