package com.huy2209.library_backend.dao;

import com.huy2209.library_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Payment findByUserEmail(String userEmail);
}
