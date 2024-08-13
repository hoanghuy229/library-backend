package com.huy2209.library_backend.dao;

import com.huy2209.library_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    User findByProviderAndProviderId(String provider, String providerId);
}
