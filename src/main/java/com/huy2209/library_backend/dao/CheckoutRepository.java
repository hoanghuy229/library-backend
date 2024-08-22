package com.huy2209.library_backend.dao;

import com.huy2209.library_backend.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout,Long> {
    Checkout findByUserEmailAndBookId(String userEmail,Long bookId);

    List<Checkout> findBooksByUserEmail(String userEmail);

}
